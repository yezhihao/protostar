package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.DataType;
import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

import static io.github.yezhihao.protostar.DataType.*;

public class NumberSchema {

    public static Schema getSchema(DataType dataType, Class<?> typeClass) {
        if (Byte.TYPE.isAssignableFrom(typeClass) || Byte.class.isAssignableFrom(typeClass)) {
            if (dataType == BYTE)
                return BYTE2Byte.INSTANCE;
        } else if (Short.TYPE.isAssignableFrom(typeClass) || Short.class.isAssignableFrom(typeClass)) {
            if (dataType == BYTE)
                return BYTE2Short.INSTANCE;
            if (dataType == WORD)
                return WORD2Short.INSTANCE;
        } else if (Integer.TYPE.isAssignableFrom(typeClass) || Integer.class.isAssignableFrom(typeClass)) {
            if (dataType == BYTE)
                return BYTE2Int.INSTANCE;
            if (dataType == WORD)
                return WORD2Int.INSTANCE;
            if (dataType == DWORD)
                return DWORD2Int.INSTANCE;
        } else if (Long.TYPE.isAssignableFrom(typeClass) || Long.class.isAssignableFrom(typeClass)) {
            if (dataType == DWORD)
                return DWORD2Long.INSTANCE;
            if (dataType == QWORD)
                return QWORD2Long.INSTANCE;
        } else if (String.class.isAssignableFrom(typeClass)) {
            if (dataType == QWORD)
                return QWORD2String.INSTANCE;
        }
        return null;
    }

    public static class BYTE2Byte implements Schema<Byte> {
        public static final Schema INSTANCE = new BYTE2Byte();

        private BYTE2Byte() {
        }

        @Override
        public Byte readFrom(ByteBuf input) {
            return input.readByte();
        }

        @Override
        public void writeTo(ByteBuf output, Byte value) {
            output.writeByte(value);
        }
    }

    public static class BYTE2Short implements Schema<Short> {
        public static final Schema INSTANCE = new BYTE2Short();

        private BYTE2Short() {
        }

        @Override
        public Short readFrom(ByteBuf input) {
            return input.readUnsignedByte();
        }

        @Override
        public void writeTo(ByteBuf output, Short value) {
            output.writeByte(value);
        }
    }

    public static class BYTE2Int implements Schema<Integer> {
        public static final Schema INSTANCE = new BYTE2Int();

        private BYTE2Int() {
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

    public static class WORD2Short implements Schema<Short> {
        public static final Schema INSTANCE = new WORD2Short();

        private WORD2Short() {
        }

        @Override
        public Short readFrom(ByteBuf input) {
            return input.readShort();
        }

        @Override
        public void writeTo(ByteBuf output, Short value) {
            output.writeShort(value);
        }
    }

    public static class WORD2Int implements Schema<Integer> {
        public static final Schema INSTANCE = new WORD2Int();

        private WORD2Int() {
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

    public static class DWORD2Int implements Schema<Integer> {
        public static final Schema INSTANCE = new DWORD2Int();

        private DWORD2Int() {
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

    public static class DWORD2Long implements Schema<Long> {
        public static final Schema INSTANCE = new DWORD2Long();

        private DWORD2Long() {
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

    public static class QWORD2Long implements Schema<Long> {
        public static final Schema INSTANCE = new QWORD2Long();

        private QWORD2Long() {
        }

        @Override
        public Long readFrom(ByteBuf input) {
            return input.readLong();
        }

        @Override
        public void writeTo(ByteBuf output, Long value) {
            output.writeLong(value);
        }
    }

    public static class QWORD2String implements Schema<String> {
        public static final Schema INSTANCE = new QWORD2String();

        private QWORD2String() {
        }

        @Override
        public String readFrom(ByteBuf input) {
            return Long.toString(input.readLong());
        }

        @Override
        public void writeTo(ByteBuf output, String value) {
            output.writeLong(Long.parseUnsignedLong(value, 10));
        }
    }
}