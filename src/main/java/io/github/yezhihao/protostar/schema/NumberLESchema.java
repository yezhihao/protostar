package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Fixed;
import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public final class NumberLESchema {

    public static final Schema<Short> WORD_SHORT = new WORD2Short();
    public static final Schema<Integer> WORD_INT = new WORD2Int();
    public static final Schema<Integer> DWORD_INT = new DWORD2Int();
    public static final Schema<Long> DWORD_LONG = new DWORD2Long();
    public static final Schema<Float> DWORD_FLOAT = new DWORD2Float();
    public static final Schema<Long> QWORD_LONG = new QWORD2Long();
    public static final Schema<Double> QWORD_DOUBLE = new QWORD2Double();

    private static class WORD2Short implements Fixed.L2<Short> {
        public Short readFrom(ByteBuf input) {
            return input.readShortLE();
        }

        public void writeTo(ByteBuf output, Short value) {
            output.writeShortLE(value);
        }
    }

    private static class WORD2Int implements Fixed.L2<Integer> {
        public Integer readFrom(ByteBuf input) {
            return input.readUnsignedShortLE();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeShortLE(value);
        }
    }

    private static class DWORD2Int implements Fixed.L4<Integer> {
        public Integer readFrom(ByteBuf input) {
            return input.readIntLE();
        }

        public void writeTo(ByteBuf output, Integer value) {
            output.writeIntLE(value);
        }
    }

    private static class DWORD2Long implements Fixed.L4<Long> {
        public Long readFrom(ByteBuf input) {
            return input.readUnsignedIntLE();
        }

        public void writeTo(ByteBuf output, Long value) {
            output.writeIntLE(value.intValue());
        }
    }

    private static class DWORD2Float implements Fixed.L4<Float> {
        public Float readFrom(ByteBuf input) {
            return input.readFloatLE();
        }

        public void writeTo(ByteBuf output, Float value) {
            output.writeFloatLE(value);
        }
    }

    private static class QWORD2Long implements Fixed.L8<Long> {
        public Long readFrom(ByteBuf input) {
            return input.readLongLE();
        }

        public void writeTo(ByteBuf output, Long value) {
            output.writeLongLE(value);
        }
    }

    private static class QWORD2Double implements Fixed.L8<Double> {
        public Double readFrom(ByteBuf input) {
            return input.readDoubleLE();
        }

        public void writeTo(ByteBuf output, Double value) {
            output.writeDoubleLE(value);
        }
    }
}