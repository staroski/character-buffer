package br.com.staroski.text;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Arrays;

public final class CharacterBuffer implements Appendable, CharSequence, Comparable<CharSequence>, Serializable {

    private static final long serialVersionUID = 1;

    public final static CharacterBuffer standard() {
        return withPageSize(8192);
    }

    public final static CharacterBuffer withPageSize(int pageSize) {
        if (pageSize < 1) {
            throw new IllegalArgumentException("page size must be greater than zero");
        }
        return new CharacterBuffer(pageSize);
    }

    private int pageSize;

    private char[][] memory;
    private int size;
    private int page;
    private int offset;

    private CharacterBuffer(int pageSize) {
        this.pageSize = pageSize;
        allocate();
    }

    public final CharacterBuffer append(boolean value) {
        return append(String.valueOf(value));
    }

    @Override
    public final CharacterBuffer append(char character) {
        memory[page][offset] = character;
        if ((offset = (offset + pageSize + 1) % pageSize) == 0) {
            allocateMore();
        }
        ++size;
        return this;
    }

    public final CharacterBuffer append(char[] characters) {
        return append(String.valueOf(characters));
    }

    public final CharacterBuffer append(char[] characters, int offset, int length) {
        return append(String.valueOf(characters, offset, length));
    }

    @Override
    public final CharacterBuffer append(CharSequence text) {
        if (text == null) {
            text = "null";
        }
        int remaining = text.length();
        if (remaining < 1) {
            return this;
        }
        char[] chars = text.toString().toCharArray();
        int index = 0;
        int availableOnPage = pageSize - offset;
        while (remaining > 0) {
            int length = Math.min(availableOnPage, remaining);
            System.arraycopy(chars, index, memory[page], offset, length);
            if ((offset = (offset + pageSize + length) % pageSize) == 0) {
                allocateMore();
            }
            availableOnPage = pageSize - offset;
            remaining -= length;
            index += length;
            size += length;
        }
        return this;
    }

    @Override
    public final CharacterBuffer append(CharSequence text, int start, int end) {
        return append(text.subSequence(start, end));
    }

    public final CharacterBuffer append(double value) {
        return append(String.valueOf(value));
    }

    public final CharacterBuffer append(float value) {
        return append(String.valueOf(value));
    }

    public final CharacterBuffer append(int value) {
        return append(String.valueOf(value));
    }

    public final CharacterBuffer append(long value) {
        return append(String.valueOf(value));
    }

    public final CharacterBuffer append(Object object) {
        return append(String.valueOf(object));
    }

    @Override
    public final char charAt(int index) {
        return memory[index / pageSize][index % pageSize];
    }

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

    public final CharacterBuffer delete(int start, int end) {
        end--;
        int destinyPage = start / pageSize;
        int destinyOffset = start % pageSize;

        int endPage = end / pageSize;
        int endOffset = end % pageSize;

        int remaining = size - end - 1;
        int sourcePage = endPage;
        int sourceOffset = (endOffset + pageSize + 1) % pageSize;
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
        size -= (end - start + 1);
        page = size / pageSize;
        offset = size % pageSize;
        Arrays.fill(memory[page], destinyOffset, destinyOffset + (pageSize - destinyOffset), '\u0000');
        for (int i = page + 1; i < memory.length; i++) {
            memory[i] = null;
        }
        return this;
    }

    public final CharacterBuffer deleteAll() {
        allocate();
        return this;
    }

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

    @Override
    public final int length() {
        return size;
    }

    @Override
    public final CharacterBuffer subSequence(int start, int end) {
        CharacterBuffer subSequence = CharacterBuffer.withPageSize(pageSize);
        int length = end - start;
        for (int i = 0; i < length; i++) {
            subSequence.append(charAt(start + i));
        }
        return subSequence;
    }

    public final String substring(int start) {
        return substring(start, size);
    }

    public final String substring(int start, int end) {
        return subSequence(start, end).toString();
    }

    @Override
    public final String toString() {
        int index = 0;
        char[] chars = new char[size];
        int last = size / pageSize;
        if (last > -1) {
            for (int i = 0; i < last; i++) {
                System.arraycopy(memory[i], 0, chars, index, pageSize);
                index += pageSize;
            }
            System.arraycopy(memory[last], 0, chars, index, offset);
        }
        return String.valueOf(chars);
    }

    private void allocate() {
        memory = new char[1][pageSize];
        size = 0;
        page = 0;
        offset = 0;
    }

    private void allocateMore() {
        char[][] moreMemory = new char[++page + 1][];
        System.arraycopy(memory, 0, moreMemory, 0, page);
        moreMemory[page] = new char[pageSize];
        memory = moreMemory;
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
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

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
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
