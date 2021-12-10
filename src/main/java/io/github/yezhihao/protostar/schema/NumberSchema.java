package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.DataType;
import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

import static io.github.yezhihao.protostar.DataType.*;

public final class NumberSchema {

    public static final Schema BYTE2Byte = new BYTE2Byte();
    public static final Schema BYTE2Short = new BYTE2Short();
    public static final Schema BYTE2Int = new BYTE2Int();
    public static final Schema WORD2Short = new WORD2Short();
    public static final Schema WORD2Int = new WORD2Int();
    public static final Schema DWORD2Int = new DWORD2Int();
    public static final Schema DWORD2Long = new DWORD2Long();
    public static final Schema QWORD2Long = new QWORD2Long();
    public static final Schema QWORD2String = new QWORD2String();

    public static Schema getSchema(DataType dataType, Class<?> typeClass) {
        if (Byte.TYPE.isAssignableFrom(typeClass) || Byte.class.isAssignableFrom(typeClass)) {
            if (dataType == BYTE)
                return BYTE2Byte;
        } else if (Short.TYPE.isAssignableFrom(typeClass) || Short.class.isAssignableFrom(typeClass)) {
            if (dataType == BYTE)
                return BYTE2Short;
            if (dataType == WORD)
                return WORD2Short;
        } else if (Integer.TYPE.isAssignableFrom(typeClass) || Integer.class.isAssignableFrom(typeClass)) {
            if (dataType == BYTE)
                return BYTE2Int;
            if (dataType == WORD)
                return WORD2Int;
            if (dataType == DWORD)
                return DWORD2Int;
        } else if (Long.TYPE.isAssignableFrom(typeClass) || Long.class.isAssignableFrom(typeClass)) {
            if (dataType == DWORD)
                return DWORD2Long;
            if (dataType == QWORD)
                return QWORD2Long;
        } else if (String.class.isAssignableFrom(typeClass)) {
            if (dataType == QWORD)
                return QWORD2String;
        }
        return null;
    }

    public static class BYTE2Byte implements Schema<Byte> {
        private BYTE2Byte() {
        }

        public Byte readFrom(ByteBuf input) {
            return input.readByte();
        }

        public void writeTo(ByteBuf output, Byte value) {
            output.writeByte(value);
        }
    }

    public static class BYTE2Short implements Schema<Short> {
        private BYTE2Short() {
        }

        public Short readFrom(ByteBuf input) {
            return input.readUnsignedByte();
        }

        public void writeTo(ByteBuf output, Short value) {
            output.writeByte(value);
        }
    }

    public static class BYTE2Int implements Schema<Integer> {
        private BYTE2Int() {
        }

        public Integer readFrom(ByteBuf input) {
            return (int) input.readUnsignedByte();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeByte(value);
        }
    }

    public static class WORD2Short implements Schema<Short> {
        private WORD2Short() {
        }

        public Short readFrom(ByteBuf input) {
            return input.readShort();
        }

        public void writeTo(ByteBuf output, Short value) {
            output.writeShort(value);
        }
    }

    public static class WORD2Int implements Schema<Integer> {
        private WORD2Int() {
        }

        public Integer readFrom(ByteBuf input) {
            return input.readUnsignedShort();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeShort(value);
        }
    }

    public static class DWORD2Int implements Schema<Integer> {
        private DWORD2Int() {
        }

        public Integer readFrom(ByteBuf input) {
            return input.readInt();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeInt(value);
        }
    }

    public static class DWORD2Long implements Schema<Long> {
        private DWORD2Long() {
        }

        public Long readFrom(ByteBuf input) {
            return input.readUnsignedInt();
        }

        public void writeTo(ByteBuf output, Long value) {
            output.writeInt(value.intValue());
        }
    }

    public static class QWORD2Long implements Schema<Long> {
        private QWORD2Long() {
        }

        public Long readFrom(ByteBuf input) {
            return input.readLong();
        }

        public void writeTo(ByteBuf output, Long value) {
            output.writeLong(value);
        }
    }

    public static class QWORD2String implements Schema<String> {
        private QWORD2String() {
        }

        public String readFrom(ByteBuf input) {
            return Long.toString(input.readLong());
        }

        public void writeTo(ByteBuf output, String value) {
            output.writeLong(Long.parseUnsignedLong(value, 10));
        }
    }
}