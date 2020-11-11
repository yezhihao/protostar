package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.DataType;
import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

public class NumberSchema {

    public static Schema getSchema(DataType dataType, Class<?> typeClass) {
        Schema schema;
        switch (dataType) {
            case BYTE:
                schema = Int8.INSTANCE;
                break;
            case WORD:
                schema = Int16.INSTANCE;
                break;
            case DWORD:
                if (Integer.TYPE.isAssignableFrom(typeClass) || Integer.class.isAssignableFrom(typeClass))
                    schema = NumberSchema.Int32.INSTANCE;
                else
                    schema = NumberSchema.Long32.INSTANCE;
                break;
            case QWORD:
                schema = Long64.INSTANCE;
                break;
            default:
                throw new RuntimeException("不支持的类型转换");
        }
        return schema;
    }

    public static class Int8 implements Schema<Integer> {
        public static final Schema INSTANCE = new Int8();

        private Int8() {
        }

        @Override
        public Integer readFrom(ByteBuf input) {
            return (int) input.readUnsignedByte();
        }

        @Override
        public void writeTo(ByteBuf output, Integer value) {
            output.writeByte(value);
        }
    }

    public static class Int16 implements Schema<Integer> {
        public static final Schema INSTANCE = new Int16();

        private Int16() {
        }

        @Override
        public Integer readFrom(ByteBuf input) {
            return input.readUnsignedShort();
        }

        @Override
        public void writeTo(ByteBuf output, Integer value) {
            output.writeShort(value);
        }
    }

    public static class Int32 implements Schema<Integer> {
        public static final Schema INSTANCE = new Int32();

        private Int32() {
        }

        @Override
        public Integer readFrom(ByteBuf input) {
            return input.readInt();
        }

        @Override
        public void writeTo(ByteBuf output, Integer value) {
            output.writeInt(value);
        }
    }

    public static class Long32 implements Schema<Long> {
        public static final Schema INSTANCE = new Long32();

        private Long32() {
        }

        @Override
        public Long readFrom(ByteBuf input) {
            return input.readUnsignedInt();
        }

        @Override
        public void writeTo(ByteBuf output, Long value) {
            output.writeInt(value.intValue());
        }
    }

    public static class Long64 implements Schema<Long> {
        public static final Schema INSTANCE = new Long64();

        private Long64() {
        }

        @Override
        public Long readFrom(ByteBuf input) {
            return input.readLong();
        }

        @Override
        public void writeTo(ByteBuf output, Long value) {
            output.writeLong(value.intValue());
        }
    }
}