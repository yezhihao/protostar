package io.github.yezhihao.protostar.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class StrUtils {

    public static String toString(Object value) {
        if (value == null)
            return "null";
        if (value instanceof ByteBuf)
            return ByteBufUtil.hexDump((ByteBuf) value);
        if (!value.getClass().isArray())
            return value.toString();

        StringBuilder root = new StringBuilder(32);
        toString(value, root);
        return root.toString();
    }

    public static StringBuilder toString(Object value, StringBuilder builder) {
        if (value == null)
            return builder;

        builder.append('[');
        int start = builder.length();

        if (value instanceof long[]) {
            long[] array = (long[]) value;
            for (long t : array)
                builder.append(t).append(',');

        } else if (value instanceof int[]) {
            int[] array = (int[]) value;
            for (int t : array)
                builder.append(t).append(',');

        } else if (value instanceof short[]) {
            short[] array = (short[]) value;
            for (short t : array)
                builder.append(t).append(',');

        } else if (value instanceof byte[]) {
            byte[] array = (byte[]) value;
            for (byte t : array)
                builder.append(t).append(',');

        } else if (value instanceof char[]) {
            char[] array = (char[]) value;
            for (char t : array)
                builder.append(t).append(',');

        } else if (value instanceof double[]) {
            double[] array = (double[]) value;
            for (double t : array)
                builder.append(t).append(',');

        } else if (value instanceof float[]) {
            float[] array = (float[]) value;
            for (float t : array)
                builder.append(t).append(',');

        } else if (value instanceof boolean[]) {
            boolean[] array = (boolean[]) value;
            for (boolean t : array)
                builder.append(t).append(',');

        } else if (value instanceof String[]) {
            String[] array = (String[]) value;
            for (String t : array)
                builder.append(t).append(',');

        } else if (isArray(value)) {
            Object[] array = (Object[]) value;
            for (Object t : array)
                toString(t, builder).append(',');

        } else if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            for (Object t : array)
                builder.append(t).append(',');
        }

        int end = builder.length();
        if (end <= start) builder.append(']');
        else builder.setCharAt(end - 1, ']');
        return builder;
    }

    private static boolean isArray(Object value) {
        Class<?> componentType = value.getClass().getComponentType();
        if (componentType == null)
            return false;
        return componentType.isArray();
    }

    public static String leftPad(String str, int size, char ch) {
        int length = str.length();
        int pads = size - length;
        if (pads > 0) {
            char[] result = new char[size];
            str.getChars(0, length, result, pads);
            while (pads > 0)
                result[--pads] = ch;
            return new String(result);
        }
        return str;
    }
}