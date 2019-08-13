package br.com.staroski.text;

import java.io.Serializable;

public final class CharacterBuffer implements Appendable, CharSequence, Serializable {

    private static final long serialVersionUID = 1;

    public static CharacterBuffer standard() {
        return withPageSize(8192);
    }

    public static CharacterBuffer withPageSize(int pageSize) {
        if (pageSize < 1) {
            throw new IllegalArgumentException("page size must be greater than zero");
        }
        return new CharacterBuffer(pageSize);
    }

    private final int pageSize;
    private char[][] memory;
    private int page;
    private int offset;
    private int size;

    private CharacterBuffer(int pageSize) {
        this.pageSize = pageSize;
        this.memory = new char[1][pageSize];
    }

    public CharacterBuffer append(boolean value) {
        return append(String.valueOf(value));
    }

    @Override
    public CharacterBuffer append(char character) {
        memory[page][offset] = character;
        if ((offset = (offset + pageSize + 1) % pageSize) == 0) {
            allocate();
        }
        ++size;
        return this;
    }

    public CharacterBuffer append(char[] characters) {
        return append(String.valueOf(characters));
    }

    public CharacterBuffer append(char[] characters, int offset, int length) {
        return append(String.valueOf(characters, offset, length));
    }

    @Override
    public CharacterBuffer append(CharSequence text) {
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
                allocate();
            }
            availableOnPage = pageSize - offset;
            remaining -= length;
            index += length;
            size += length;
        }
        return this;
    }

    @Override
    public CharacterBuffer append(CharSequence text, int start, int end) {
        return append(text.subSequence(start, end));
    }

    public CharacterBuffer append(double value) {
        return append(String.valueOf(value));
    }

    public CharacterBuffer append(float value) {
        return append(String.valueOf(value));
    }

    public CharacterBuffer append(int value) {
        return append(String.valueOf(value));
    }

    public CharacterBuffer append(long value) {
        return append(String.valueOf(value));
    }

    public CharacterBuffer append(Object object) {
        return append(String.valueOf(object));
    }

    @Override
    public char charAt(int index) {
        return memory[index / size][index % size];
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CharacterBuffer) {
            return this.toString().equals(object.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public CharacterBuffer subSequence(int start, int end) {
        CharacterBuffer subSequence = CharacterBuffer.withPageSize(pageSize);
        int length = end - start;
        for (int i = 0; i < length; i++) {
            subSequence.append(charAt(start + i));
        }
        return subSequence;
    }

    @Override
    public String toString() {
        int index = 0;
        char[] chars = new char[size];
        int last = memory.length - 1;
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
        char[][] alloc = new char[++page + 1][];
        System.arraycopy(memory, 0, alloc, 0, page);
        alloc[page] = new char[pageSize];
        memory = alloc;
    }
}
