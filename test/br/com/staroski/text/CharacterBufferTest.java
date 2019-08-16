package br.com.staroski.text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ricardo Artur Staroski
 */
public class CharacterBufferTest {

    /* Holds references to the objects that are tested */
    private final class Objects {

        public final CharacterBuffer a = CharacterBuffer.with(16).kilo().bytes();
        public final CharacterBuffer b = CharacterBuffer.with(32).kilo().bytes();
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
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> CharacterBuffer 32K '" + method + "'", a.charAt(index), b.charAt(index));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'", a.charAt(index), c.charAt(index));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'", a.charAt(index), d.charAt(index));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuilder       '" + method + "'", b.charAt(index), c.charAt(index));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuffer        '" + method + "'", b.charAt(index), d.charAt(index));
        }

        public void checkIndexOf(CharSequence text) {
            String method = "indexOf(CharSequence)";
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> CharacterBuffer 32K '" + method + "'", a.indexOf(text), b.indexOf(text));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'", a.indexOf(text), c.indexOf(text.toString()));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'", a.indexOf(text), d.indexOf(text.toString()));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuilder       '" + method + "'", b.indexOf(text), c.indexOf(text.toString()));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuffer        '" + method + "'", b.indexOf(text), d.indexOf(text.toString()));
        }

        public void checkIndexOf(String text, int from) {
            String method = "indexOf(CharSequence, int)";
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> CharacterBuffer 32K '" + method + "'",
                                a.indexOf(text, from),
                                b.indexOf(text, from));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'",
                                a.indexOf(text, from),
                                c.indexOf(text.toString(), from));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'",
                                a.indexOf(text, from),
                                d.indexOf(text.toString(), from));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuilder       '" + method + "'",
                                b.indexOf(text, from),
                                c.indexOf(text.toString(), from));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuffer        '" + method + "'",
                                b.indexOf(text, from),
                                d.indexOf(text.toString(), from));
        }

        public void checkLastIndexOf(CharSequence text) {
            String method = "lastIndexOf(CharSequence)";
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> CharacterBuffer 32K '" + method + "'",
                                a.lastIndexOf(text),
                                b.lastIndexOf(text));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'",
                                a.lastIndexOf(text),
                                c.lastIndexOf(text.toString()));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'",
                                a.lastIndexOf(text),
                                d.lastIndexOf(text.toString()));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuilder       '" + method + "'",
                                b.lastIndexOf(text),
                                c.lastIndexOf(text.toString()));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuffer        '" + method + "'",
                                b.lastIndexOf(text),
                                d.lastIndexOf(text.toString()));
        }

        public void checkLastIndexOf(CharSequence text, int from) {
            String method = "lastIndexOf(CharSequence, int)";
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> CharacterBuffer 32K '" + method + "'",
                                a.lastIndexOf(text, from),
                                b.lastIndexOf(text, from));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'",
                                a.lastIndexOf(text, from),
                                c.lastIndexOf(text.toString(), from));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'",
                                a.lastIndexOf(text, from),
                                d.lastIndexOf(text.toString(), from));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuilder       '" + method + "'",
                                b.lastIndexOf(text, from),
                                c.lastIndexOf(text.toString(), from));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuffer        '" + method + "'",
                                b.lastIndexOf(text, from),
                                d.lastIndexOf(text.toString(), from));
        }

        public void checkLength() {
            String method = "length()";
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> CharacterBuffer 32K '" + method + "'", a.length(), b.length());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'", a.length(), c.length());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'", a.length(), d.length());
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuilder       '" + method + "'", b.length(), c.length());
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuffer        '" + method + "'", b.length(), d.length());
        }

        public void checkSubSequence(int start, int end) {
            String method = "subSequence(int, int)";
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> CharacterBuffer 32K '" + method + "'",
                                a.subSequence(start, end).toString(),
                                b.subSequence(start, end).toString());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'",
                                a.subSequence(start, end).toString(),
                                c.subSequence(start, end).toString());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'",
                                a.subSequence(start, end).toString(),
                                d.subSequence(start, end).toString());
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuilder       '" + method + "'",
                                b.subSequence(start, end).toString(),
                                c.subSequence(start, end).toString());
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuffer        '" + method + "'",
                                b.subSequence(start, end).toString(),
                                d.subSequence(start, end).toString());
        }

        public void checkSubstring(int start) {
            String method = "substring(int)";
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> CharacterBuffer 32K '" + method + "'", a.substring(start), b.substring(start));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'", a.substring(start), c.substring(start));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'", a.substring(start), d.substring(start));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuilder       '" + method + "'", b.substring(start), c.substring(start));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuffer        '" + method + "'", b.substring(start), d.substring(start));
        }

        public void checkSubstring(int start, int end) {
            String method = "substring(int, int)";
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> CharacterBuffer 32K '" + method + "'",
                                a.substring(start, end),
                                b.substring(start, end));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'",
                                a.substring(start, end),
                                c.substring(start, end));
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'",
                                a.substring(start, end),
                                d.substring(start, end));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuilder       '" + method + "'",
                                b.substring(start, end),
                                c.substring(start, end));
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuffer        '" + method + "'",
                                b.substring(start, end),
                                d.substring(start, end));
        }

        public void checkToString() {
            String method = "toString()";
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> CharacterBuffer 32K '" + method + "'", a.toString(), b.toString());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuilder       '" + method + "'", a.toString(), c.toString());
            Assert.assertEquals("CharacterBuffer 16K '" + method + "' <> StringBuffer        '" + method + "'", a.toString(), d.toString());
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuilder       '" + method + "'", b.toString(), c.toString());
            Assert.assertEquals("CharacterBuffer 32K '" + method + "' <> StringBuffer        '" + method + "'", b.toString(), d.toString());
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
    public void testDeleteCharAtIndex() throws IOException {
        Objects objects = new Objects();
        objects.append("Hello World");

        int index = 4;
        objects.a.deleteCharAt(index);
        objects.b.deleteCharAt(index);
        objects.c.deleteCharAt(index);
        objects.d.deleteCharAt(index);

        objects.checkToString();
    }

    @Test
    public void testDeleteStartEnd() throws IOException {
        Objects objects = new Objects();
        objects.append("Hello World");

        int start = 2;
        int end = 7;
        objects.a.delete(start, end);
        objects.b.delete(start, end);
        objects.c.delete(start, end);
        objects.d.delete(start, end);

        objects.checkToString();
    }

    @Test
    public void testIndexOf() throws IOException {
        Objects objects = new Objects();
        objects.append("Hello World Wow");
        objects.checkIndexOf("Wo");
    }

    @Test
    public void testIndexOfFrom() throws IOException {
        Objects objects = new Objects();
        objects.append("Hello World Wow");
        objects.checkIndexOf("Wo", 5);
        objects.checkIndexOf("Wo", 9);
        objects.checkIndexOf("Wo", 12);
    }

    @Test
    public void testLastIndexOf() throws IOException {
        Objects objects = new Objects();
        objects.append("Hello World Wow");
        objects.checkLastIndexOf("Wo");
        objects.checkLastIndexOf("");
    }

    @Test
    public void testLastIndexOfFrom() throws IOException {
        Objects objects = new Objects();
        objects.append("Hello World Wow");
        objects.checkLastIndexOf("Wo", 5);
        objects.checkLastIndexOf("Wo", 9);
        objects.checkLastIndexOf("Wo", 12);
        objects.checkLastIndexOf("", 15);
    }

    @Test
    public void testLength() throws IOException {
        Objects objects = new Objects();
        objects.fill(CHARACTER_COUNT);
        objects.checkLength();
    }

    @Test
    public void testSerialization() throws Exception {
        CharacterBuffer expected = CharacterBuffer.with(16).kilo().bytes().append("Testing Serialization");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(expected);
        oos.flush();
        oos.close();

        byte[] bytes = bos.toByteArray();

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        CharacterBuffer actual = (CharacterBuffer) ois.readObject();
        ois.close();

        Assert.assertEquals(expected, actual);
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
