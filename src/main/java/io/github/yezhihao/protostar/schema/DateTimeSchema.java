package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

import java.time.LocalDateTime;

import static io.github.yezhihao.protostar.util.DateTimeUtils.Bcd;
import static io.github.yezhihao.protostar.util.DateTimeUtils.Bytes;

public class DateTimeSchema {

    public static class BCD implements Schema<LocalDateTime> {
        public static final Schema INSTANCE = new BCD();

        private BCD() {
        }

        @Override
        public LocalDateTime readFrom(ByteBuf input) {
            byte[] bytes = new byte[6];
            input.readBytes(bytes);
            return Bcd.toDateTime(bytes);
        }

        @Override
        public LocalDateTime readFrom(ByteBuf input, int length) {
            byte[] bytes = new byte[length];
            input.readBytes(bytes);
            return Bcd.toDateTime(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDateTime value) {
            output.writeBytes(Bcd.from(value));
        }

        @Override
        public void writeTo(ByteBuf output, int length, LocalDateTime value) {
            output.writeBytes(Bcd.from(value));
        }
    }

    public static class BYTES implements Schema<LocalDateTime> {
        public static final Schema INSTANCE = new BYTES();

        private BYTES() {
        }

        @Override
        public LocalDateTime readFrom(ByteBuf input) {
            byte[] bytes = new byte[6];
            input.readBytes(bytes);
            return Bytes.toDateTime(bytes);
        }

        @Override
        public LocalDateTime readFrom(ByteBuf input, int length) {
            byte[] bytes = new byte[length];
            input.readBytes(bytes);
            return Bytes.toDateTime(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDateTime value) {
            output.writeBytes(Bytes.from(value));
        }

        @Override
        public void writeTo(ByteBuf output, int length, LocalDateTime value) {
            output.writeBytes(Bytes.from(value));
        }
    }
}