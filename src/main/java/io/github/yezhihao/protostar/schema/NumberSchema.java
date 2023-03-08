package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.field.BasicField;
import io.netty.buffer.ByteBuf;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public final class NumberSchema {

    public static final Schema<Boolean> BOOL = new BOOL();
    public static final Schema<Character> CHAR = new CHAR();
    public static final Schema<Number> BYTE_BYTE = new BYTE2Byte();
    public static final Schema<Number> BYTE_SHORT = new BYTE2Short();
    public static final Schema<Number> BYTE_INT = new BYTE2Int();

    public static final Schema<Number> WORD_SHORT = new WORD2Short();
    public static final Schema<Number> WORD_INT = new WORD2Int();
    public static final Schema<Number> MEDIUM_INT = new MEDIUM2Int();
    public static final Schema<Number> DWORD_INT = new DWORD2Int();
    public static final Schema<Number> DWORD_LONG = new DWORD2Long();
    public static final Schema<Number> DWORD_FLOAT = new DWORD2Float();
    public static final Schema<Number> QWORD_LONG = new QWORD2Long();
    public static final Schema<Number> QWORD_DOUBLE = new QWORD2Double();

    public static final Schema<Number> WORD_SHORT_LE = new WORD2ShortLE();
    public static final Schema<Number> WORD_INT_LE = new WORD2IntLE();
    public static final Schema<Number> MEDIUM_INT_LE = new MEDIUM2IntLE();
    public static final Schema<Number> DWORD_INT_LE = new DWORD2IntLE();
    public static final Schema<Number> DWORD_LONG_LE = new DWORD2LongLE();
    public static final Schema<Number> DWORD_FLOAT_LE = new DWORD2FloatLE();
    public static final Schema<Number> QWORD_LONG_LE = new QWORD2LongLE();
    public static final Schema<Number> QWORD_DOUBLE_LE = new QWORD2DoubleLE();

    protected static class BOOL extends BasicField<Boolean> {
        public Boolean readFrom(ByteBuf input) {
            return input.readBoolean();
        }

        public void writeTo(ByteBuf output, Boolean value) {
            if (value != null) output.writeBoolean(value);
        }
    }

    protected static class CHAR extends BasicField<Character> {
        public Character readFrom(ByteBuf input) {
            return input.readChar();
        }

        public void writeTo(ByteBuf output, Character value) {
            if (value != null) output.writeChar(value);
        }
    }

    protected static class BYTE2Byte extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readByte();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeByte(value.byteValue());
        }
    }

    protected static class BYTE2Short extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readUnsignedByte();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeByte(value.byteValue());
        }
    }

    protected static class BYTE2Int extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return (int) input.readUnsignedByte();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeByte(value.byteValue());
        }
    }

    protected static class WORD2Short extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readShort();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeShort(value.shortValue());
        }
    }

    protected static class WORD2Int extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readUnsignedShort();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeShort(value.shortValue());
        }
    }

    protected static class MEDIUM2Int extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readMedium();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeMedium(value.intValue());
        }
    }

    protected static class DWORD2Int extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readInt();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeInt(value.intValue());
        }
    }

    protected static class DWORD2Long extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readUnsignedInt();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeInt(value.intValue());
        }
    }

    protected static class DWORD2Float extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readFloat();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeFloat(value.floatValue());
        }
    }

    protected static class QWORD2Long extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readLong();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeLong(value.longValue());
        }
    }

    protected static class QWORD2Double extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readDouble();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeDouble(value.doubleValue());
        }
    }

    protected static class WORD2ShortLE extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readShortLE();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeShortLE(value.shortValue());
        }
    }

    protected static class WORD2IntLE extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readUnsignedShortLE();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeShortLE(value.shortValue());
        }
    }

    protected static class MEDIUM2IntLE extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readMediumLE();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeMediumLE(value.intValue());
        }
    }

    protected static class DWORD2IntLE extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readIntLE();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeIntLE(value.intValue());
        }
    }

    protected static class DWORD2LongLE extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readUnsignedIntLE();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeIntLE(value.intValue());
        }
    }

    protected static class DWORD2FloatLE extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readFloatLE();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeFloatLE(value.floatValue());
        }
    }

    protected static class QWORD2LongLE extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readLongLE();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeLongLE(value.longValue());
        }
    }

    protected static class QWORD2DoubleLE extends BasicField<Number> {
        public Number readFrom(ByteBuf input) {
            return input.readDoubleLE();
        }

        public void writeTo(ByteBuf output, Number value) {
            if (value != null) output.writeDoubleLE(value.doubleValue());
        }
    }
}