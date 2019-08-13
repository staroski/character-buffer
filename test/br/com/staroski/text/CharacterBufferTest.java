package br.com.staroski.text;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CharacterBufferTest {

    /* Holds references to the objects that are tested */
    private final class Objects {

        public final CharacterBuffer a = CharacterBuffer.standard();
        public final CharacterBuffer b = CharacterBuffer.withPageSize(CUSTOM_PAGE_SIZE);
        public final StringBuilder c = new StringBuilder();
        public final StringBuffer d = new StringBuffer();

        public void append(String text) {
            a.append(text);
            b.append(text);
            c.append(text);
            d.append(text);
        }

        public void checkCharAt(int index) {
            String method = "charAt(int)";
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> CharacterBuffer 16K '" + method + "'", a.charAt(index), b.charAt(index));
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuilder       '" + method + "'", a.charAt(index), c.charAt(index));
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuffer        '" + method + "'", a.charAt(index), d.charAt(index));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'", b.charAt(index), c.charAt(index));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'", b.charAt(index), d.charAt(index));
        }

        public void checkLength() {
            String method = "length()";
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> CharacterBuffer 16K '" + method + "'", a.length(), b.length());
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuilder       '" + method + "'", a.length(), c.length());
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuffer        '" + method + "'", a.length(), d.length());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'", b.length(), c.length());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'", b.length(), d.length());
        }

        public void checkSubSequence(int start, int end) {
            String method = "subSequence(int, int)";
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> CharacterBuffer 16K '" + method + "'",
                                a.subSequence(start, end).toString(),
                                b.subSequence(start, end).toString());
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuilder       '" + method + "'",
                                a.subSequence(start, end).toString(),
                                c.subSequence(start, end).toString());
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuffer        '" + method + "'",
                                a.subSequence(start, end).toString(),
                                d.subSequence(start, end).toString());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'",
                                b.subSequence(start, end).toString(),
                                c.subSequence(start, end).toString());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'",
                                b.subSequence(start, end).toString(),
                                d.subSequence(start, end).toString());
        }

        public void checkSubstring(int start) {
            String method = "substring(int)";
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> CharacterBuffer 16K '" + method + "'", a.substring(start), b.substring(start));
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuilder       '" + method + "'", a.substring(start), c.substring(start));
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuffer        '" + method + "'", a.substring(start), d.substring(start));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'", b.substring(start), c.substring(start));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'", b.substring(start), d.substring(start));
        }

        public void checkSubstring(int start, int end) {
            String method = "substring(int, int)";
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> CharacterBuffer 16K '" + method + "'",
                                a.substring(start, end),
                                b.substring(start, end));
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuilder       '" + method + "'",
                                a.substring(start, end),
                                c.substring(start, end));
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuffer        '" + method + "'",
                                a.substring(start, end),
                                d.substring(start, end));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'",
                                b.substring(start, end),
                                c.substring(start, end));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'",
                                b.substring(start, end),
                                d.substring(start, end));
        }

        public void checkToString() {
            String method = "toString()";
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> CharacterBuffer 16K '" + method + "'", a.toString(), b.toString());
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuilder       '" + method + "'", a.toString(), c.toString());
            Assert.assertEquals("CharacterBuffer STD '" + method + "' <> StringBuffer        '" + method + "'", a.toString(), d.toString());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'", b.toString(), c.toString());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'", b.toString(), d.toString());
        }

        public void fill(int characterCount) throws IOException {
            fill(a, characterCount);
            fill(b, characterCount);
            fill(c, characterCount);
            fill(d, characterCount);
        }

        private <T extends Appendable & CharSequence> void fill(T buffer, int chars) throws IOException {
            for (int i = 0; i < chars; i++) {
                buffer.append('X');
            }
        }
    }

    private static final int CHARACTER_COUNT = 10485760; // 10 M
    private static final int CUSTOM_PAGE_SIZE = 16384;

    @Test
    public void testAppendBoolean() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        boolean value = true;
        objects.a.append(value);
        objects.b.append(value);
        objects.c.append(value);
        objects.d.append(value);

        objects.checkToString();
    }

    @Test
    public void testAppendChar() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        char character = 'A';
        objects.a.append(character);
        objects.b.append(character);
        objects.c.append(character);
        objects.d.append(character);

        objects.checkToString();
    }

    @Test
    public void testAppendCharacters() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        char[] characters = new char[] { 'a', 'b', 'c', 'd', 'e' };
        objects.a.append(characters);
        objects.b.append(characters);
        objects.c.append(characters);
        objects.d.append(characters);

        objects.checkToString();
    }

    @Test
    public void testAppendCharactersOffsetLength() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        char[] characters = new char[] { 'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd' };
        int offset = 3;
        int length = 5;
        objects.a.append(characters, offset, length);
        objects.b.append(characters, offset, length);
        objects.c.append(characters, offset, length);
        objects.d.append(characters, offset, length);

        objects.checkToString();
    }

    @Test
    public void testAppendCharSequence() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        CharSequence charSequence = "Hello World";
        objects.a.append(charSequence);
        objects.b.append(charSequence);
        objects.c.append(charSequence);
        objects.d.append(charSequence);

        objects.checkToString();
    }

    @Test
    public void testAppendCharSequenceStartEnd() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        CharSequence charSequence = "Hello World";
        int start = 3;
        int end = 7;
        objects.a.append(charSequence, start, end);
        objects.b.append(charSequence, start, end);
        objects.c.append(charSequence, start, end);
        objects.d.append(charSequence, start, end);

        objects.checkToString();
    }

    @Test
    public void testAppendDouble() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        double value = 12345.6789;
        objects.a.append(value);
        objects.b.append(value);
        objects.c.append(value);
        objects.d.append(value);

        objects.checkToString();
    }

    @Test
    public void testAppendFloat() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        float value = (float) 123.45;
        objects.a.append(value);
        objects.b.append(value);
        objects.c.append(value);
        objects.d.append(value);

        objects.checkToString();
    }

    @Test
    public void testAppendInt() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        int value = 123;
        objects.a.append(value);
        objects.b.append(value);
        objects.c.append(value);
        objects.d.append(value);

        objects.checkToString();
    }

    @Test
    public void testAppendLong() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        long value = 123;
        objects.a.append(value);
        objects.b.append(value);
        objects.c.append(value);
        objects.d.append(value);

        objects.checkToString();
    }

    @Test
    public void testAppendObject() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);

        List<?> object = Arrays.asList("Hello World", 1, true, 'a', 2.5);
        objects.a.append(object);
        objects.b.append(object);
        objects.c.append(object);
        objects.d.append(object);

        objects.checkToString();
    }

    @Test
    public void testCharAt() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);
        objects.checkCharAt(10);
    }

    @Test
    public void testLength() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);
        objects.checkLength();
    }

    @Test
    public void testSubSequence() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);
        objects.checkSubSequence(30, 90);
    }

    @Test
    public void testSubstringStart() throws IOException {
        Objects objects = new Objects();
        objects.append("Hello World");
        objects.checkSubstring(6);
    }

    @Test
    public void testSubstringStartEnd() throws IOException {
        Objects objects = new Objects();
        objects.append("Hello World");
        objects.checkSubstring(3, 8);
    }

    @Test
    public void testToString() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);
        objects.checkToString();
    }
}
