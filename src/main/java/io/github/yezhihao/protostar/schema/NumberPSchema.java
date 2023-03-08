package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public final class NumberPSchema {

    public static final Schema<Boolean> BOOL = new BOOL();
    public static final Schema<Character> CHAR = new CHAR();
    public static final Schema<Number> BYTE_BYTE = new BYTE2Byte();
    public static final Schema<Number> BYTE_SHORT = new BYTE2Short();
    public static final Schema<Number> BYTE_INT = new BYTE2Int();

    public static final Schema<Number> WORD_SHORT = new WORD2Short();
    public static final Schema<Number> WORD_INT = new WORD2Int();
    public static final Schema<Number> DWORD_INT = new DWORD2Int();
    public static final Schema<Number> MEDIUM_INT = new MEDIUM2Int();
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

    protected static class BOOL extends NumberSchema.BOOL {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setBoolean(obj, input.readBoolean());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeBoolean(f.getBoolean(obj));
        }
    }

    protected static class CHAR extends NumberSchema.CHAR {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setChar(obj, input.readChar());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeChar(f.getChar(obj));
        }
    }

    protected static class BYTE2Byte extends NumberSchema.BYTE2Byte {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setByte(obj, input.readByte());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeByte(f.getByte(obj));
        }
    }

    protected static class BYTE2Short extends NumberSchema.BYTE2Short {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setShort(obj, input.readUnsignedByte());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeByte(f.getShort(obj));
        }
    }

    protected static class BYTE2Int extends NumberSchema.BYTE2Int {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readUnsignedByte());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeByte(f.getInt(obj));
        }
    }

    protected static class WORD2Short extends NumberSchema.WORD2Short {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setShort(obj, input.readShort());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeShort(f.getShort(obj));
        }
    }

    protected static class WORD2Int extends NumberSchema.WORD2Int {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readUnsignedShort());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeShort(f.getInt(obj));
        }
    }

    protected static class MEDIUM2Int extends NumberSchema.MEDIUM2Int {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readMedium());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeMedium(f.getInt(obj));
        }
    }

    protected static class DWORD2Int extends NumberSchema.DWORD2Int {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readInt());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeInt(f.getInt(obj));
        }
    }

    protected static class DWORD2Long extends NumberSchema.DWORD2Long {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setLong(obj, input.readUnsignedInt());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeInt((int) f.getLong(obj));
        }
    }

    protected static class DWORD2Float extends NumberSchema.DWORD2Float {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setFloat(obj, input.readFloat());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeFloat(f.getFloat(obj));
        }
    }

    protected static class QWORD2Long extends NumberSchema.QWORD2Long {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setLong(obj, input.readLong());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeLong(f.getLong(obj));
        }
    }

    protected static class QWORD2Double extends NumberSchema.QWORD2Double {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setDouble(obj, input.readDouble());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeDouble(f.getDouble(obj));
        }
    }

    protected static class WORD2ShortLE extends NumberSchema.WORD2ShortLE {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setShort(obj, input.readShortLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeShortLE(f.getShort(obj));
        }
    }

    protected static class WORD2IntLE extends NumberSchema.WORD2IntLE {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readUnsignedShortLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeShortLE(f.getInt(obj));
        }
    }

    protected static class MEDIUM2IntLE extends NumberSchema.MEDIUM2IntLE {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readMediumLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeMediumLE(f.getInt(obj));
        }
    }

    protected static class DWORD2IntLE extends NumberSchema.DWORD2IntLE {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setInt(obj, input.readIntLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeIntLE(f.getInt(obj));
        }
    }

    protected static class DWORD2LongLE extends NumberSchema.DWORD2LongLE {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setLong(obj, input.readUnsignedIntLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeIntLE((int) f.getLong(obj));
        }
    }

    protected static class DWORD2FloatLE extends NumberSchema.DWORD2FloatLE {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setFloat(obj, input.readFloatLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeFloatLE(f.getFloat(obj));
        }
    }

    protected static class QWORD2LongLE extends NumberSchema.QWORD2LongLE {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setLong(obj, input.readLongLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeLongLE(f.getLong(obj));
        }
    }

    protected static class QWORD2DoubleLE extends NumberSchema.QWORD2DoubleLE {
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            f.setDouble(obj, input.readDoubleLE());
        }

        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            output.writeDoubleLE(f.getDouble(obj));
        }
    }
}