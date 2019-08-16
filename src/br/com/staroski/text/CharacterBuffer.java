package br.com.staroski.text;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.Arrays;

/**
 * This class is an alternative to Java's {@link StringBuilder} and {@link StringBuffer} classes.<br>
 * Instances of this class are created using an <i>builder design pattern</i> through the factory method {@link CharacterBuffer#with(int)}.<br>
 * <b>Example:</b><br>
 * Instantiating a {@link CharacterBuffer} with memory pages of {@code 16KB} in 4 different ways:
 * 
 * <pre>
 * CharacterBuffer a = CharacterBuffer.with(16384).bytes();
 * CharacterBuffer b = CharacterBuffer.with(8192).chars();
 * CharacterBuffer c = CharacterBuffer.with(16).kilo().bytes();
 * CharacterBuffer d = CharacterBuffer.with(8).kilo().chars();
 * </pre>
 * 
 * The {@link CharacterBuffer}'s memory allocation strategy differs in the following way:<br>
 * - When {@link StringBuilder} and {@link StringBuffer} reach the current capacity, they double the size of the internal {@code char} array.<br>
 * - When {@link CharacterBuffer} reach the current capacity, it allocates a new memory page that is a fixed size {@code char} array.<br>
 * With this strategy the {@link CharacterBuffer} prevents {@code OutOfMemoryError}s when dealing with huge {@link String} concatenations.<br>
 * 
 * @author Ricardo Artur Staroski
 */
public final class CharacterBuffer implements Appendable, CharSequence, Comparable<CharSequence>, Serializable {

    /**
     * Builder used to determine the {@link CharacterBuffer}'s memory page sizes.<br>
     * <b>Factory methods:</b><br>
     * - {@link #bytes()}: returns a new {@link CharacterBuffer} whith page sizes that have this amount of {@code byte}s.<br>
     * - {@link #chars()}: returns a new {@link CharacterBuffer} whith page sizes that have this amount of {@code char}s.<br>
     * <b>Multipliers:</b><br>
     * - {@link #kilo()}: returns this amount multiplied by 2<sup>10</sup>.<br>
     * - {@link #mega()}: returns this amount multiplied by 2<sup>20</sup>.<br>
     * - {@link #giga()}: returns this amount multiplied by 2<sup>30</sup>.
     */
    public static final class Amount {

        private final int amount;

        /**
         * Private constructor, se {@link CharacterBuffer#with(int)} method
         */
        private Amount(int amount) {
            if (amount < 1) {
                throw new IllegalArgumentException("amount must be greater than zero");
            }
            this.amount = amount;
        }

        /**
         * @return an {@link CharacterBuffer} with memory page sizes that that have this {@link Amount amount} of bytes.
         */
        public final CharacterBuffer bytes() {
            return new CharacterBuffer(amount >> 1);
        }

        /**
         * @return an {@link CharacterBuffer} with memory page sizes that have this {@link Amount amount} of chars.
         */
        public final CharacterBuffer chars() {
            return new CharacterBuffer(amount);
        }

        /**
         * @return this {@link Amount amount} multiplied by 2<sup>30</sup>.
         */
        public final Amount giga() {
            return new Amount(amount * 1073741824);
        }

        /**
         * @return this {@link Amount amount} multiplied by 2<sup>10</sup>.
         */
        public final Amount kilo() {
            return new Amount(amount * 1024);
        }

        /**
         * @return this {@link Amount amount} multiplied by 2<sup>20</sup>.
         */
        public final Amount mega() {
            return new Amount(amount * 1048576);
        }
    }

    private static final long serialVersionUID = 1;

    /**
     * Prepares the {@link Amount amount} of memory that each memory page will allocate.
     * 
     * @param amount
     *            The amount of memory that will be allocated for each new memory page.
     * 
     * @return An {@link Amount} object to build the instance of {@link CharacterBuffer}.
     */
    public static final Amount with(int amount) {
        return new Amount(amount);
    }

    private int pageSize;

    private char[][] memory;
    private int size;
    private int page;
    private int offset;

    private transient SoftReference<String> toStringCache;

    /**
     * Private constructor, see {@link CharacterBuffer#with(int)} method.
     */
    private CharacterBuffer(int pageSize) {
        this.pageSize = pageSize;
        allocate();
    }

    /**
     * Appends the {@link String} representation of the {@code boolean} argument to this {@link CharacterBuffer}.
     * 
     * @param value
     *            the {@code boolean} to append.
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer append(boolean value) {
        return append(String.valueOf(value));
    }

    /**
     * Appends the {@link String} representation of the {@code char} argument to this {@link CharacterBuffer}.
     * 
     * @param character
     *            the {@code char} to append.
     * 
     * @return a reference to this object.
     */
    @Override
    public final CharacterBuffer append(char character) {
        toStringCache = null;
        memory[page][offset] = character;
        if ((offset = (offset + pageSize + 1) % pageSize) == 0) {
            allocateNext();
        }
        ++size;
        return this;
    }

    /**
     * Appends the {@link String} representation of the {@code char} array argument to this {@link CharacterBuffer}.
     * 
     * @param characters
     *            the {@code char} array to append.
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer append(char[] characters) {
        return append(String.valueOf(characters));
    }

    /**
     * Appends the {@link String} representation of a subarray of the {@code char} array argument to this {@link CharacterBuffer}.
     * 
     * @param characters
     *            the {@code char} array to append.
     * 
     * @param offset
     *            the index of the first {@code char} to append.
     * 
     * @param length
     *            the number of {@code char}s to append.
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer append(char[] characters, int offset, int length) {
        return append(String.valueOf(characters, offset, length));
    }

    /**
     * Appends the specified {@code CharSequence} to this {@link CharacterBuffer}.
     * 
     * @param text
     *            the {@code CharSequence} to append.
     * 
     * @return a reference to this object.
     */
    @Override
    public final CharacterBuffer append(CharSequence text) {
        if (text == null) {
            text = "null";
        }
        int remaining = text.length();
        if (remaining < 1) {
            return this;
        }
        toStringCache = null;
        char[] chars = text.toString().toCharArray();
        int index = 0;
        int availableOnPage = pageSize - offset;
        while (remaining > 0) {
            int length = Math.min(availableOnPage, remaining);
            System.arraycopy(chars, index, memory[page], offset, length);
            if ((offset = (offset + pageSize + length) % pageSize) == 0) {
                allocateNext();
            }
            availableOnPage = pageSize - offset;
            remaining -= length;
            index += length;
            size += length;
        }
        return this;
    }

    /**
     * Appends a subsequence of the specified {@code CharSequence} to this {@link CharacterBuffer}.
     * 
     * @param text
     *            the {@code CharSequence} to append.
     * 
     * @param start
     *            the starting index of the subsequence to append.
     * 
     * @param end
     *            the end index of the subsequence to append.
     * 
     * @return a reference to this object.
     */
    @Override
    public final CharacterBuffer append(CharSequence text, int start, int end) {
        return append(text.subSequence(start, end));
    }

    /**
     * Appends the {@link String} representation of the {@code double} argument to this {@link CharacterBuffer}.
     * 
     * @param value
     *            the {@code double} to append.
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer append(double value) {
        return append(String.valueOf(value));
    }

    /**
     * Appends the {@link String} representation of the {@code float} argument to this {@link CharacterBuffer}.
     * 
     * @param value
     *            the {@code float} to append.
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer append(float value) {
        return append(String.valueOf(value));
    }

    /**
     * Appends the {@link String} representation of the {@code int} argument to this {@link CharacterBuffer}.
     * 
     * @param value
     *            the {@code int} to append.
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer append(int value) {
        return append(String.valueOf(value));
    }

    /**
     * Appends the {@link String} representation of the {@code long} argument to this {@link CharacterBuffer}.
     * 
     * @param value
     *            the {@code long} to append.
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer append(long value) {
        return append(String.valueOf(value));
    }

    /**
     * Appends the {@link String} representation of the {@link Object} argument to this {@link CharacterBuffer}.
     * 
     * @param value
     *            the {@link Object} to append.
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer append(Object object) {
        return append(String.valueOf(object));
    }

    /**
     * Returns the {@code char} value in this {@link CharacterBuffer} at the specified index.
     * 
     * @param index
     *            the index of the desired {@code char} value.
     * 
     * @return the {@code char} value at the specified index.
     */
    @Override
    public final char charAt(int index) {
        return memory[index / pageSize][index % pageSize];
    }

    /**
     * Compares this {@link CharacterBuffer} with the {@link CharSequence} argument.
     * 
     * @return {@code 0} if {@code this} is equals to the {@link CharSequence} argument.<br>
     *         A positive number if {@code this} is greater than the {@link CharSequence} argument.<br>
     *         A negative number if {@code this} is less than the {@link CharSequence} argument.
     */
    @Override
    public final int compareTo(CharSequence that) {
        int diff = this.length() - that.length();
        if (diff != 0) {
            return diff;
        }
        for (int i = 0; i < length(); i++) {
            diff = this.charAt(i) - that.charAt(i);
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }

    /**
     * Removes the characters in a substring of this {@link CharacterBuffer}.<br>
     * The substring begins at the specified {@code start} and extends to the character at index {@code end - 1}.<br>
     * If {@code start} is equal to {@code end}, no changes are made. *
     * 
     * @param start
     *            The beginning index, inclusive.
     * 
     * @param end
     *            The ending index, exclusive.
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer delete(int start, int end) {
        if (start == end) {
            return this;
        }
        toStringCache = null;
        int last = end > size ? size - 1 : end - 1;
        int destinyPage = start / pageSize;
        int destinyOffset = start % pageSize;

        int lastPage = last / pageSize;
        int lastOffset = last % pageSize;

        int remaining = size - last - 1;
        int sourcePage = lastPage;
        int sourceOffset = (lastOffset + pageSize + 1) % pageSize;
        while (remaining > 0) {
            int length = Math.min(pageSize - sourceOffset, pageSize - destinyOffset);
            length = Math.min(length, pageSize - offset);
            System.arraycopy(memory[sourcePage], sourceOffset, memory[destinyPage], destinyOffset, length);
            if ((destinyOffset = (destinyOffset + pageSize + length) % pageSize) == 0) {
                ++destinyPage;
            }
            if ((sourceOffset = (sourceOffset + pageSize + length) % pageSize) == 0) {
                ++sourcePage;
            }
            remaining -= length;
        }
        size -= (last - start + 1);
        page = size / pageSize;
        offset = size % pageSize;
        Arrays.fill(memory[page], destinyOffset, destinyOffset + (pageSize - destinyOffset), '\u0000');
        for (int i = page + 1; i < memory.length; i++) {
            memory[i] = null;
        }
        return this;
    }

    /**
     * Removes all the characters from this {@link CharacterBuffer}.
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer deleteAll() {
        allocate();
        size = 0;
        page = 0;
        offset = 0;
        return this;
    }

    /**
     * Removes the the {@code char} at the specified position in this {@link CharacterBuffer}.
     * 
     * @param index
     *            Index of {@code char} to remove
     * 
     * @return a reference to this object.
     */
    public final CharacterBuffer deleteCharAt(int index) {
        return delete(index, index + 1);
    }

    @Override
    public final boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CharacterBuffer) {
            return this.toString().equals(object.toString());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return toString().hashCode();
    }

    /**
     * Returns the length (character count) of this {@link CharacterBuffer}.
     *
     * @return the length of the sequence of characters currently represented by this object.
     */
    @Override
    public final int length() {
        return size;
    }

    /**
     * Returns a new {@link CharacterBuffer} that is a subsequence of this character sequence.
     * 
     * @param start
     *            the start index, inclusive.
     * 
     * @param end
     *            the end index, exclusive.
     * 
     * @return a new {@link CharacterBuffer} with the specified subsequence.
     */
    @Override
    public final CharacterBuffer subSequence(int start, int end) {
        CharacterBuffer subSequence = CharacterBuffer.with(pageSize).chars();
        int length = end - start;
        for (int i = 0; i < length; i++) {
            subSequence.append(charAt(start + i));
        }
        return subSequence;
    }

    /**
     * Returns a new {@link String} that contains a subsequence of characters currently contained in this {@link CharacterBuffer}.<br>
     * The substring begins at the specified index and extends to the end of this {@link CharacterBuffer}.
     *
     * @param start
     *            The beginning index, inclusive.
     * 
     * @return The new {@link String}.
     */
    public final String substring(int start) {
        return substring(start, size);
    }

    /**
     * Returns a new {@link String} that contains a subsequence of characters currently contained in this {@link CharacterBuffer}.<br>
     * The substring begins at the specified {@code start} and extends to the character at index {@code end - 1}.
     *
     * @param start
     *            The beginning index, inclusive.
     * @param end
     *            The ending index, exclusive.
     * 
     * @return The new {@link String}.
     */
    public final String substring(int start, int end) {
        return subSequence(start, end).toString();
    }

    /**
     * Returns a {@link String} representing the data in this sequence.<br>
     * A new {@link String} object is allocated and initialized to contain the character sequence currently represented by this object.<br>
     * This {@link String} is then returned.<br>
     * Subsequent changes to this sequence do not affect the contents of the {@link String}.
     *
     * @return a {@link String} representation of this {@link CharacterBuffer}.
     */
    @Override
    public final String toString() {
        if (toStringCache == null || toStringCache.get() == null) {
            int index = 0;
            char[] chars = new char[size];
            int last = size / pageSize;
            if (last > -1) {
                for (int page = 0; page < last; page++) {
                    System.arraycopy(memory[page], 0, chars, index, pageSize);
                    index += pageSize;
                }
                System.arraycopy(memory[last], 0, chars, index, offset);
            }
            toStringCache = new SoftReference<String>(String.valueOf(chars));
        }
        return toStringCache.get();
    }

    /**
     * Allocates the first memory page for the character sequence storage.
     */
    private final void allocate() {
        memory = new char[1][pageSize];
    }

    /**
     * Allocates the next memory page for the character sequence storage.
     */
    private final void allocateNext() {
        char[][] moreMemory = new char[++page + 1][];
        System.arraycopy(memory, 0, moreMemory, 0, page);
        moreMemory[page] = new char[pageSize];
        memory = moreMemory;
    }

    /**
     * Custom serialization implementation.<br>
     * Read the {@link Serializable} interface documentation for more info.
     */
    private final void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        pageSize = in.readInt();
        size = in.readInt();
        int pages = (size / pageSize) + 1;
        page = size / pageSize;
        offset = size % pageSize;
        memory = new char[pages][];
        InputStreamReader reader = new InputStreamReader(in);
        for (int page = 0; page < pages; page++) {
            memory[page] = new char[pageSize];
            reader.read(memory[page], 0, pageSize);
        }
    }

    /**
     * Custom serialization implementation.<br>
     * Read the {@link Serializable} interface documentation for more info.
     */
    private final void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(pageSize);
        out.writeInt(size);
        int pages = (size / pageSize) + 1;
        int lastPage = pages - 1;
        OutputStreamWriter writer = new OutputStreamWriter(out);
        for (int page = 0; page < pages; page++) {
            writer.write(memory[page], 0, page == lastPage ? offset : pageSize);
        }
        writer.flush();
    }
}