package br.com.staroski.text;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CharacterBufferTest {

    private static final int CHARACTER_COUNT = 10485760; // 10 M
    private static final int CUSTOM_PAGE_SIZE = 16384;

    @Test
    public void testAppendBoolean() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        boolean value = true;

        a.append(value);
        b.append(value);
        c.append(value);
        d.append(value);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
    }

    @Test
    public void testAppendChar() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        char character = 'A';

        a.append(character);
        b.append(character);
        c.append(character);
        d.append(character);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
        assertEquals("StringBuilder <> StringBuffer", c.toString(), d.toString());
    }

    @Test
    public void testAppendCharacters() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        char[] characters = new char[] { 'a', 'b', 'c', 'd', 'e' };

        a.append(characters);
        b.append(characters);
        c.append(characters);
        d.append(characters);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
    }

    @Test
    public void testAppendCharactersOffsetLength() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        char[] characters = new char[] { 'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd' };
        int offset = 3;
        int length = 5;

        a.append(characters, offset, length);
        b.append(characters, offset, length);
        c.append(characters, offset, length);
        d.append(characters, offset, length);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
    }

    @Test
    public void testAppendCharSequence() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        CharSequence charSequence = "Hello World";

        a.append(charSequence);
        b.append(charSequence);
        c.append(charSequence);
        d.append(charSequence);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
    }

    @Test
    public void testAppendCharSequenceStartEnd() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        CharSequence charSequence = "Hello World";
        int start = 3;
        int end = 7;

        a.append(charSequence, start, end);
        b.append(charSequence, start, end);
        c.append(charSequence, start, end);
        d.append(charSequence, start, end);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
    }

    @Test
    public void testAppendDouble() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        double value = 12345.6789;

        a.append(value);
        b.append(value);
        c.append(value);
        d.append(value);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
    }

    @Test
    public void testAppendFloat() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        float value = (float) 123.45;

        a.append(value);
        b.append(value);
        c.append(value);
        d.append(value);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
    }

    @Test
    public void testAppendInt() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        int value = 123;

        a.append(value);
        b.append(value);
        c.append(value);
        d.append(value);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
    }

    @Test
    public void testAppendLong() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        long value = 123;

        a.append(value);
        b.append(value);
        c.append(value);
        d.append(value);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
    }

    @Test
    public void testAppendObject() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        List<?> object = Arrays.asList("Hello World", 1, true, 'a', 2.5);

        a.append(object);
        b.append(object);
        c.append(object);
        d.append(object);

        assertEquals("CharacterBuffer STD <> CharacterBuffer 16K", a.toString(), b.toString());
        assertEquals("CharacterBuffer STD <> StringBuilder", a.toString(), c.toString());
        assertEquals("CharacterBuffer STD <> StringBuffer", a.toString(), d.toString());
        assertEquals("CharacterBuffer 16K <> StringBuilder", b.toString(), c.toString());
        assertEquals("CharacterBuffer 16K <> StringBuffer", b.toString(), d.toString());
    }

    @Test
    public void testCharAt() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        int index = 10;
        assertEquals("A.charAt(index) <> B.charAt(index)", a.charAt(index), b.charAt(index));
        assertEquals("A.charAt(index) <> C.charAt(index)", a.charAt(index), c.charAt(index));
        assertEquals("A.charAt(index) <> D.charAt(index)", a.charAt(index), d.charAt(index));
        assertEquals("B.charAt(index) <> C.charAt(index)", b.charAt(index), c.charAt(index));
        assertEquals("B.charAt(index) <> D.charAt(index)", b.charAt(index), d.charAt(index));
    }

    @Test
    public void testLength() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        assertEquals("A.length() <> B.length()", a.length(), b.length());
        assertEquals("A.length() <> C.length()", a.length(), c.length());
        assertEquals("A.length() <> D.length()", a.length(), d.length());
        assertEquals("B.length() <> C.length()", b.length(), c.length());
        assertEquals("B.length() <> D.length()", b.length(), d.length());
    }

    @Test
    public void testSubSequence() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        int start = 30;
        int end = 90;
        assertEquals("A.subSequence(start, end) <> B.subSequence(start, end)", a.subSequence(start, end).toString(), b.subSequence(start, end).toString());
        assertEquals("A.subSequence(start, end) <> C.subSequence(start, end)", a.subSequence(start, end).toString(), c.subSequence(start, end).toString());
        assertEquals("A.subSequence(start, end) <> D.subSequence(start, end)", a.subSequence(start, end).toString(), d.subSequence(start, end).toString());
        assertEquals("B.subSequence(start, end) <> C.subSequence(start, end)", b.subSequence(start, end).toString(), c.subSequence(start, end).toString());
        assertEquals("B.subSequence(start, end) <> D.subSequence(start, end)", b.subSequence(start, end).toString(), d.subSequence(start, end).toString());
    }

    @Test
    public void testToString() throws IOException {
        CharacterBuffer a = CharacterBuffer.standard();
        CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        StringBuilder c = new StringBuilder();
        StringBuffer d = new StringBuffer();

        fill(a, CHARACTER_COUNT);
        fill(b, CHARACTER_COUNT);
        fill(c, CHARACTER_COUNT);
        fill(d, CHARACTER_COUNT);

        assertEquals("A.toString() <> B.toString()", a.toString(), b.toString());
        assertEquals("A.toString() <> C.toString()", a.toString(), c.toString());
        assertEquals("A.toString() <> D.toString()", a.toString(), d.toString());
        assertEquals("B.toString() <> C.toString()", b.toString(), c.toString());
        assertEquals("B.toString() <> D.toString()", b.toString(), d.toString());
    }

    private <T extends Appendable & CharSequence> void fill(T buffer, int chars) throws IOException {
        for (int i = 0; i < chars; i++) {
            buffer.append('X');
        }
    }
}
