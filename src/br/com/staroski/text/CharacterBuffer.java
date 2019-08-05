package br.com.staroski.text;

import java.io.Serializable;

public final class CharacterBuffer implements Appendable, CharSequence, Serializable {

    private static final long serialVersionUID = 1;

    public static CharacterBuffer pageSize(int pageSize) {
        if (pageSize < 1) {
            throw new IllegalArgumentException("page size must be greater than zero");
        }
        return new CharacterBuffer(pageSize);
    }

    public static CharacterBuffer standard() {
        return pageSize(8192);
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

    @Override
    public Appendable append(char character) {
        memory[page][offset] = character;
        if ((offset = (offset + pageSize + 1) % pageSize) == 0) {
            allocate();
        }
        ++size;
        return this;
    }

    @Override
    public Appendable append(CharSequence text) {
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
    public Appendable append(CharSequence text, int start, int end) {
        return append(text.subSequence(start, end));
    }

    @Override
    public char charAt(int index) {
        return memory[index / size][index % size];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CharacterBuffer) {
            return this.toString().equals(obj.toString());
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
    public CharSequence subSequence(int start, int end) {
        int length = end - start;
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = charAt(start + i);
        }
        return new String(chars);
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
        return new String(chars);
    }

    private void allocate() {
        char[][] alloc = new char[++page + 1][];
        System.arraycopy(memory, 0, alloc, 0, page);
        alloc[page] = new char[pageSize];
        memory = alloc;
    }
}
