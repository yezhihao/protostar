package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

public final class NumberSchema {

    public static final Schema BOOL = new BOOL();
    public static final Schema CHAR = new CHAR();
    public static final Schema BYTE_BYTE = new BYTE2Byte();
    public static final Schema BYTE_SHORT = new BYTE2Short();
    public static final Schema BYTE_INT = new BYTE2Int();
    public static final Schema WORD_SHORT = new WORD2Short();
    public static final Schema WORD_INT = new WORD2Int();
    public static final Schema DWORD_INT = new DWORD2Int();
    public static final Schema DWORD_LONG = new DWORD2Long();
    public static final Schema DWORD_FLOAT = new DWORD2Float();
    public static final Schema QWORD_LONG = new QWORD2Long();
    public static final Schema QWORD_DOUBLE = new QWORD2Double();

    public static class BOOL implements Schema<Boolean> {

        private BOOL() {
        }

        public Boolean readFrom(ByteBuf input) {
            return input.readBoolean();
        }

        public void writeTo(ByteBuf output, Boolean value) {
            output.writeBoolean(value);
        }
    }

    public static class CHAR implements Schema<Character> {
        private CHAR() {
        }

        public Character readFrom(ByteBuf input) {
            return input.readChar();
        }

        public void writeTo(ByteBuf output, Character value) {
            output.writeChar(value);
        }
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

    public static class DWORD2Float implements Schema<Float> {
        private DWORD2Float() {
        }

        public Float readFrom(ByteBuf input) {
            return input.readFloat();
        }

        public void writeTo(ByteBuf output, Float value) {
            output.writeFloat(value);
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

    public static class QWORD2Double implements Schema<Double> {
        private QWORD2Double() {
        }

        public Double readFrom(ByteBuf input) {
            return input.readDouble();
        }

        public void writeTo(ByteBuf output, Double value) {
            output.writeDouble(value);
        }
    }
}