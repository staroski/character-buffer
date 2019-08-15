package br.com.staroski.text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Ricardo Artur Staroski
 */
public class CharacterBufferTestPerformance {

    private static class Report {
        int chars;
        int speed;
        long time;
    }

    public static void main(String[] args) {
        try {
            CharacterBufferTestPerformance program = new CharacterBufferTestPerformance();
            program.execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void execute() throws Exception {
        testSerializationBytesLength();
        testAppendCharPerformance();
        testAppendCharSequencePerformance();
    }

    public void testAppendCharPerformance() throws IOException {
        System.out.println("performance of append(char) {");

        try {
            Report report = null;

            report = fillWithCharWhileHasMemory(new StringBuffer());
            System.out.printf("  StringBuffer:          size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharWhileHasMemory(new StringBuilder());
            System.out.printf("  StringBuilder:         size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharWhileHasMemory(CharacterBuffer.with(16).kilo().bytes()); // 16 K
            System.out.printf("  CharacterBuffer  16K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharWhileHasMemory(CharacterBuffer.with(32).kilo().bytes()); // 32 K
            System.out.printf("  CharacterBuffer  32K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharWhileHasMemory(CharacterBuffer.with(64).kilo().bytes()); // 64 K
            System.out.printf("  CharacterBuffer  64K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharWhileHasMemory(CharacterBuffer.with(128).kilo().bytes()); // 128 K
            System.out.printf("  CharacterBuffer 128K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println("}");
    }

    public void testAppendCharSequencePerformance() throws IOException {
        System.out.println("performance of append(CharSequence) {");

        try {
            Report report = null;

            report = fillWithCharSequenceWhileHasMemory(new StringBuffer());
            System.out.printf("  StringBuffer:          size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharSequenceWhileHasMemory(new StringBuilder());
            System.out.printf("  StringBuilder:         size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharSequenceWhileHasMemory(CharacterBuffer.with(16).kilo().bytes()); // 16 K
            System.out.printf("  CharacterBuffer  16K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharSequenceWhileHasMemory(CharacterBuffer.with(32).kilo().bytes()); // 32 K
            System.out.printf("  CharacterBuffer  32K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharSequenceWhileHasMemory(CharacterBuffer.with(64).kilo().bytes()); // 64 K
            System.out.printf("  CharacterBuffer  64K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharSequenceWhileHasMemory(CharacterBuffer.with(128).kilo().bytes()); // 128 K
            System.out.printf("  CharacterBuffer 128K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println("}");
    }

    public void testSerializationBytesLength() throws Exception {
        String text = "testing the serialization mechanism";
        byte[] characterBuffer = serialize(CharacterBuffer.with(16).kilo().bytes());
        byte[] stringBuilder = serialize(new StringBuilder(text));
        byte[] stringBuffer = serialize(new StringBuffer(text));

        System.err.println("Serialization size of CharacterBuffer: " + characterBuffer.length);
        System.err.println("Serialization size of StringBuilder:   " + stringBuilder.length);
        System.err.println("Serialization size of StringBuffer:    " + stringBuffer.length);
    }

    private <T extends Appendable & CharSequence> Report fillWithCharSequenceWhileHasMemory(T buffer) throws Exception {
        Report report = new Report();
        long start = System.currentTimeMillis();
        try {
            while (true) {
                buffer.append("abcdefghijklmnopqrstuvwxyz0123456789");
            }
        } catch (Throwable t) {
            report.time = System.currentTimeMillis() - start;
            report.chars = buffer.length();
            report.speed = (int) (report.chars / Math.max(1, report.time));
            System.gc();
            Thread.sleep(1000);
            return report;
        }
    }

    private <T extends Appendable & CharSequence> Report fillWithCharWhileHasMemory(T buffer) throws Exception {
        Report report = new Report();
        long start = System.currentTimeMillis();
        try {
            while (true) {
                buffer.append('X');
            }
        } catch (Throwable t) {
            report.time = System.currentTimeMillis() - start;
            report.chars = buffer.length();
            report.speed = (int) (report.chars / Math.max(1, report.time));
            System.gc();
            Thread.sleep(1000);
            return report;
        }
    }

    private <T extends Serializable> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.flush();
        oos.close();
        return bos.toByteArray();
    }
}