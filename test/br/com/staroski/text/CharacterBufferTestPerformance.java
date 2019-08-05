package br.com.staroski.text;
import java.io.IOException;

import br.com.staroski.text.CharacterBuffer;

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

            report = fillWithCharWhileHasMemory(CharacterBuffer.pageSize(8192)); // 8 K
            System.out.printf("  CharacterBuffer  8 K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharWhileHasMemory(CharacterBuffer.pageSize(16384)); // 16 K
            System.out.printf("  CharacterBuffer 16 K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharWhileHasMemory(CharacterBuffer.pageSize(32768)); // 32 K
            System.out.printf("  CharacterBuffer 32 K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharWhileHasMemory(CharacterBuffer.pageSize(65536)); // 64 K
            System.out.printf("  CharacterBuffer 64 K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);
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

            report = fillWithCharSequenceWhileHasMemory(CharacterBuffer.pageSize(8192)); // 8 K
            System.out.printf("  CharacterBuffer  8 K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharSequenceWhileHasMemory(CharacterBuffer.pageSize(16384)); // 16 K
            System.out.printf("  CharacterBuffer 16 K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharSequenceWhileHasMemory(CharacterBuffer.pageSize(32768)); // 32 K
            System.out.printf("  CharacterBuffer 32 K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);

            report = fillWithCharSequenceWhileHasMemory(CharacterBuffer.pageSize(65536)); // 64 K
            System.out.printf("  CharacterBuffer 64 K:  size: %,d chars    speed: %,d appends/ms    time: %,d ms%n", report.chars, report.speed, report.time);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println("}");
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
}