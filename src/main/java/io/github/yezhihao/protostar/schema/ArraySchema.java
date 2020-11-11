package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.DataType;
import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

public class ArraySchema {

    public static Schema getSchema(DataType dataType) {
        Schema schema;
        switch (dataType) {
            case BYTE:
                schema = ByteArraySchema.INSTANCE;
                break;
            case WORD:
                schema = ShortArraySchema.INSTANCE;
                break;
            case DWORD:
                schema = IntArraySchema.INSTANCE;
                break;
            case QWORD:
                schema = LongArraySchema.INSTANCE;
                break;
            default:
                throw new RuntimeException("不支持的类型转换");
        }
        return schema;
    }

    public static class ByteArraySchema implements Schema<byte[]> {
        public static final Schema INSTANCE = new ByteArraySchema();

        private ByteArraySchema() {
        }

        @Override
        public byte[] readFrom(ByteBuf input) {
            byte[] array = new byte[input.readableBytes()];
            input.readBytes(array);
            return array;
        }

        @Override
        public byte[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            byte[] array = new byte[length];
            input.readBytes(array);
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, byte[] array) {
            output.writeBytes(array);
        }

        @Override
        public void writeTo(ByteBuf output, int length, byte[] array) {
            output.writeBytes(array, 0, length);
        }
    }

    public static class ShortArraySchema implements Schema<short[]> {
        public static final Schema INSTANCE = new ShortArraySchema();

        private ShortArraySchema() {
        }

        @Override
        public short[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 1;
            short[] array = new short[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readShort();
            return array;
        }

        @Override
        public short[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            int total = length >> 1;
            short[] array = new short[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readShort();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, short[] array) {
            for (int i = 0; i < array.length; i++) {
                output.writeShort(array[i]);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, short[] array) {
            for (int i = 0, total = length >> 1; i < total; i++) {
                output.writeShort(array[i]);
            }
        }
    }

    public static class IntArraySchema implements Schema<int[]> {
        public static final Schema INSTANCE = new IntArraySchema();

        private IntArraySchema() {
        }

        @Override
        public int[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 2;
            int[] array = new int[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readInt();
            return array;
        }

        @Override
        public int[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            int total = length >> 2;
            int[] array = new int[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readInt();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, int[] array) {
            for (int i = 0; i < array.length; i++) {
                output.writeInt(array[i]);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, int[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeInt(array[i]);
            }
        }
    }

    public static class LongArraySchema implements Schema<long[]> {
        public static final Schema INSTANCE = new LongArraySchema();

        private LongArraySchema() {
        }

        @Override
        public long[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 3;
            long[] array = new long[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readLong();
            return array;
        }

        @Override
        public long[] readFrom(ByteBuf input, int length) {
            if (length < 0)
                length = input.readableBytes();
            int total = length >> 3;
            long[] array = new long[total];
            for (int i = 0; i < total; i++)
                array[i] = input.readLong();
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, long[] array) {
            for (int i = 0; i < array.length; i++) {
                output.writeLong(array[i]);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, long[] array) {
            for (int i = 0, total = length >> 3; i < total; i++) {
                output.writeLong(array[i]);
            }
        }
    }
}