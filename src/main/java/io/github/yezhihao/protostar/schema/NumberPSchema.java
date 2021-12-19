package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.field.BasicField;
import io.netty.buffer.ByteBuf;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public final class NumberPSchema {

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

    public static final Schema<Short> WORD_SHORT_LE = new WORD2ShortLE();
    public static final Schema<Integer> WORD_INT_LE = new WORD2IntLE();
    public static final Schema<Integer> DWORD_INT_LE = new DWORD2IntLE();
    public static final Schema<Long> DWORD_LONG_LE = new DWORD2LongLE();
    public static final Schema<Float> DWORD_FLOAT_LE = new DWORD2FloatLE();
    public static final Schema<Long> QWORD_LONG_LE = new QWORD2LongLE();
    public static final Schema<Double> QWORD_DOUBLE_LE = new QWORD2DoubleLE();

    protected static class BOOL extends BasicField<Boolean> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setBoolean(obj, input.readBoolean());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeBoolean(f.getBoolean(obj));
        }

        public Boolean readFrom(ByteBuf input) {
            return input.readBoolean();
        }

        public void writeTo(ByteBuf output, Boolean value) {
            output.writeBoolean(value);
        }
    }

    protected static class CHAR extends BasicField<Character> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setChar(obj, input.readChar());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeChar(f.getChar(obj));
        }

        public Character readFrom(ByteBuf input) {
            return input.readChar();
        }

        public void writeTo(ByteBuf output, Character value) {
            output.writeChar(value);
        }
    }

    protected static class BYTE2Byte extends BasicField<Byte> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setByte(obj, input.readByte());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeByte(f.getByte(obj));
        }

        public Byte readFrom(ByteBuf input) {
            return input.readByte();
        }

        public void writeTo(ByteBuf output, Byte value) {
            output.writeByte(value);
        }
    }

    protected static class BYTE2Short extends BasicField<Short> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setShort(obj, input.readUnsignedByte());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeByte(f.getShort(obj));
        }

        public Short readFrom(ByteBuf input) {
            return input.readUnsignedByte();
        }

        public void writeTo(ByteBuf output, Short value) {
            output.writeByte(value);
        }
    }

    protected static class BYTE2Int extends BasicField<Integer> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readUnsignedByte());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeByte(f.getInt(obj));
        }

        public Integer readFrom(ByteBuf input) {
            return (int) input.readUnsignedByte();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeByte(value);
        }
    }

    protected static class WORD2Short extends BasicField<Short> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setShort(obj, input.readShort());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeShort(f.getShort(obj));
        }

        public Short readFrom(ByteBuf input) {
            return input.readShort();
        }

        public void writeTo(ByteBuf output, Short value) {
            output.writeShort(value);
        }
    }

    protected static class WORD2Int extends BasicField<Integer> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readUnsignedShort());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeShort(f.getInt(obj));
        }

        public Integer readFrom(ByteBuf input) {
            return input.readUnsignedShort();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeShort(value);
        }
    }

    protected static class DWORD2Int extends BasicField<Integer> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readInt());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeInt(f.getInt(obj));
        }

        public Integer readFrom(ByteBuf input) {
            return input.readInt();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeInt(value);
        }
    }

    protected static class DWORD2Long extends BasicField<Long> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setLong(obj, input.readUnsignedInt());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeInt((int) f.getLong(obj));
        }

        public Long readFrom(ByteBuf input) {
            return input.readUnsignedInt();
        }

        public void writeTo(ByteBuf output, Long value) {
            output.writeInt(value.intValue());
        }
    }

    protected static class DWORD2Float extends BasicField<Float> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setFloat(obj, input.readFloat());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeFloat(f.getFloat(obj));
        }

        public Float readFrom(ByteBuf input) {
            return input.readFloat();
        }

        public void writeTo(ByteBuf output, Float value) {
            output.writeFloat(value);
        }
    }

    protected static class QWORD2Long extends BasicField<Long> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setLong(obj, input.readLong());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeLong(f.getLong(obj));
        }

        public Long readFrom(ByteBuf input) {
            return input.readLong();
        }

        public void writeTo(ByteBuf output, Long value) {
            output.writeLong(value);
        }
    }

    protected static class QWORD2Double extends BasicField<Double> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setDouble(obj, input.readDouble());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeDouble(f.getDouble(obj));
        }

        public Double readFrom(ByteBuf input) {
            return input.readDouble();
        }

        public void writeTo(ByteBuf output, Double value) {
            output.writeDouble(value);
        }
    }

    protected static class WORD2ShortLE extends BasicField<Short> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setShort(obj, input.readShortLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeShortLE(f.getShort(obj));
        }

        public Short readFrom(ByteBuf input) {
            return input.readShortLE();
        }

        public void writeTo(ByteBuf output, Short value) {
            output.writeShortLE(value);
        }
    }

    protected static class WORD2IntLE extends BasicField<Integer> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readUnsignedShortLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeShortLE(f.getInt(obj));
        }

        public Integer readFrom(ByteBuf input) {
            return input.readUnsignedShortLE();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeShortLE(value);
        }
    }

    protected static class DWORD2IntLE extends BasicField<Integer> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readIntLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeIntLE(f.getInt(obj));
        }

        public Integer readFrom(ByteBuf input) {
            return input.readIntLE();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeIntLE(value);
        }
    }

    protected static class DWORD2LongLE extends BasicField<Long> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setLong(obj, input.readUnsignedIntLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeIntLE((int) f.getLong(obj));
        }

        public Long readFrom(ByteBuf input) {
            return input.readUnsignedIntLE();
        }

        public void writeTo(ByteBuf output, Long value) {
            output.writeIntLE(value.intValue());
        }
    }

    protected static class DWORD2FloatLE extends BasicField<Float> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setFloat(obj, input.readFloatLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeFloatLE(f.getFloat(obj));
        }

        public Float readFrom(ByteBuf input) {
            return input.readFloatLE();
        }

        public void writeTo(ByteBuf output, Float value) {
            output.writeFloatLE(value);
        }
    }

    protected static class QWORD2LongLE extends BasicField<Long> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setLong(obj, input.readLongLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeLongLE(f.getLong(obj));
        }

        public Long readFrom(ByteBuf input) {
            return input.readLongLE();
        }

        public void writeTo(ByteBuf output, Long value) {
            output.writeLongLE(value);
        }
    }

    protected static class QWORD2DoubleLE extends BasicField<Double> {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setDouble(obj, input.readDoubleLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeDoubleLE(f.getDouble(obj));
        }

        public Double readFrom(ByteBuf input) {
            return input.readDoubleLE();
        }

        public void writeTo(ByteBuf output, Double value) {
            output.writeDoubleLE(value);
        }
    }
}