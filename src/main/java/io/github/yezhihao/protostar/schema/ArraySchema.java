package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.field.BasicField;
import io.netty.buffer.ByteBuf;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class ArraySchema {

    public static final Schema<char[]> CHARS = new CharArray();
    public static final Schema<byte[]> BYTES = new ByteArray();
    public static final Schema<short[]> SHORTS = new ShortArray();
    public static final Schema<int[]> INTS = new IntArray();
    public static final Schema<float[]> FLOATS = new FloatArray();
    public static final Schema<long[]> LONGS = new LongArray();
    public static final Schema<double[]> DOUBLES = new DoubleArray();

    protected static class ByteArray extends BasicField<byte[]> {
        @Override
        public byte[] readFrom(ByteBuf input) {
            byte[] array = new byte[input.readableBytes()];
            input.readBytes(array);
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, byte[] array) {
            if (array == null) return;
            output.writeBytes(array);
        }
    }

    protected static class CharArray extends BasicField<char[]> {
        @Override
        public char[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 1;
            char[] array = new char[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readChar();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, char[] array) {
            if (array == null) return;
            for (int i = 0; i < array.length; i++) {
                output.writeChar(array[i]);
            }
        }
    }

    protected static class ShortArray extends BasicField<short[]> {
        @Override
        public short[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 1;
            short[] array = new short[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readShort();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, short[] array) {
            if (array == null) return;
            for (int i = 0; i < array.length; i++) {
                output.writeShort(array[i]);
            }
        }
    }

    protected static class IntArray extends BasicField<int[]> {
        @Override
        public int[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 2;
            int[] array = new int[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readInt();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, int[] array) {
            if (array == null) return;
            for (int i = 0; i < array.length; i++) {
                output.writeInt(array[i]);
            }
        }
    }

    protected static class LongArray extends BasicField<long[]> {
        @Override
        public long[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 3;
            long[] array = new long[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readLong();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, long[] array) {
            if (array == null) return;
            for (int i = 0; i < array.length; i++) {
                output.writeLong(array[i]);
            }
        }
    }

    protected static class FloatArray extends BasicField<float[]> {
        @Override
        public float[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 2;
            float[] array = new float[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readFloat();
            return array;
        }


        @Override
        public void writeTo(ByteBuf output, float[] array) {
            if (array == null) return;
            for (int i = 0; i < array.length; i++) {
                output.writeFloat(array[i]);
            }
        }
    }

    protected static class DoubleArray extends BasicField<double[]> {
        @Override
        public double[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 3;
            double[] array = new double[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readDouble();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, double[] array) {
            if (array == null) return;
            for (int i = 0; i < array.length; i++) {
                output.writeDouble(array[i]);
            }
        }
    }
}