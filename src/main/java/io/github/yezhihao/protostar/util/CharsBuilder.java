package io.github.yezhihao.protostar.util;

import java.util.Arrays;

public class CharsBuilder implements CharSequence, Appendable {

    private final char[] value;
    private int pos;

    public CharsBuilder(int length) {
        this.value = new char[length];
    }

    public CharsBuilder(char[] chars) {
        this.value = chars;
    }

    @Override
    public Appendable append(CharSequence s) {
        return append(s, 0, s.length());
    }

    @Override
    public Appendable append(CharSequence s, int start, int end) {
        int len = end - start;
        for (int i = start, j = pos; i < end; i++, j++)
            value[j] = s.charAt(i);
        pos += len;
        return this;
    }

    @Override
    public Appendable append(char c) {
        value[pos++] = c;
        return this;
    }

    @Override
    public char charAt(int index) {
        return value[index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start == end) {
            return new CharsBuilder(Math.min(16, value.length));
        }
        return new CharsBuilder(Arrays.copyOfRange(value, start, end));
    }

    @Override
    public int length() {
        return value.length;
    }

    @Override
    public String toString() {
        return new String(value);
    }

    public String leftStrip(char c) {
        int i = leftOf(value, c);
        return new String(value, i, value.length - i);
    }

    public String rightStrip(char c) {
        int i = rightOf(value, c);
        return new String(value, 0, i);
    }

    public static int leftOf(char[] chars, char pad) {
        int i = 0, len = chars.length;
        while (i < len && chars[i] == pad) i++;
        return i;
    }

    public static int rightOf(char[] chars, char pad) {
        int i = 0, len = chars.length;
        while ((i < len) && (chars[len - 1] <= pad)) len--;
        return len;
    }
}
