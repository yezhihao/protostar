package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Fixed;
import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public final class NumberSchema {

    public static final Schema<Boolean> BOOL = new BOOL();
    public static final Schema<Character> CHAR = new CHAR();
    public static final Schema<Byte> BYTE_BYTE = new BYTE2Byte();
    public static final Schema<Short> BYTE_SHORT = new BYTE2Short();
    public static final Schema<Integer> BYTE_INT = new BYTE2Int();
    public static final Schema<Short> WORD_SHORT = new WORD2Short();
    public static final Schema<Integer> WORD_INT = new WORD2Int();
    public static final Schema<Integer> DWORD_INT = new DWORD2Int();
    public static final Schema<Long> DWORD_LONG = new DWORD2Long();
    public static final Schema<Float> DWORD_FLOAT = new DWORD2Float();
    public static final Schema<Long> QWORD_LONG = new QWORD2Long();
    public static final Schema<Double> QWORD_DOUBLE = new QWORD2Double();

    private static class BOOL implements Fixed.L1<Boolean> {
        public Boolean readFrom(ByteBuf input) {
            return input.readBoolean();
        }

        public void writeTo(ByteBuf output, Boolean value) {
            output.writeBoolean(value);
        }
    }

    private static class CHAR implements Fixed.L2<Character> {
        public Character readFrom(ByteBuf input) {
            return input.readChar();
        }

        public void writeTo(ByteBuf output, Character value) {
            output.writeChar(value);
        }
    }

    private static class BYTE2Byte implements Fixed.L1<Byte> {
        public Byte readFrom(ByteBuf input) {
            return input.readByte();
        }

        public void writeTo(ByteBuf output, Byte value) {
            output.writeByte(value);
        }
    }

    private static class BYTE2Short implements Fixed.L1<Short> {
        public Short readFrom(ByteBuf input) {
            return input.readUnsignedByte();
        }

        public void writeTo(ByteBuf output, Short value) {
            output.writeByte(value);
        }
    }

    private static class BYTE2Int implements Fixed.L1<Integer> {
        public Integer readFrom(ByteBuf input) {
            return (int) input.readUnsignedByte();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeByte(value);
        }
    }

    private static class WORD2Short implements Fixed.L2<Short> {
        public Short readFrom(ByteBuf input) {
            return input.readShort();
        }

        public void writeTo(ByteBuf output, Short value) {
            output.writeShort(value);
        }
    }

    private static class WORD2Int implements Fixed.L2<Integer> {
        public Integer readFrom(ByteBuf input) {
            return input.readUnsignedShort();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeShort(value);
        }
    }

    private static class DWORD2Int implements Fixed.L4<Integer> {
        public Integer readFrom(ByteBuf input) {
            return input.readInt();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeInt(value);
        }
    }

    private static class DWORD2Long implements Fixed.L4<Long> {
        public Long readFrom(ByteBuf input) {
            return input.readUnsignedInt();
        }

        public void writeTo(ByteBuf output, Long value) {
            output.writeInt(value.intValue());
        }
    }

    private static class DWORD2Float implements Fixed.L4<Float> {
        public Float readFrom(ByteBuf input) {
            return input.readFloat();
        }

        public void writeTo(ByteBuf output, Float value) {
            output.writeFloat(value);
        }
    }

    private static class QWORD2Long implements Fixed.L8<Long> {
        public Long readFrom(ByteBuf input) {
            return input.readLong();
        }

        public void writeTo(ByteBuf output, Long value) {
            output.writeLong(value);
        }
    }

    private static class QWORD2Double implements Fixed.L8<Double> {
        public Double readFrom(ByteBuf input) {
            return input.readDouble();
        }

        public void writeTo(ByteBuf output, Double value) {
            output.writeDouble(value);
        }
    }
}