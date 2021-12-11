package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.github.yezhihao.protostar.util.DateTimeUtils.Bcd;
import static io.github.yezhihao.protostar.util.DateTimeUtils.Bytes;

public class DateTimeSchema {

    public static final Schema BCD2DateTime = new BCD2DateTime();
    public static final Schema BYTES2Time = new BYTES2Time();
    public static final Schema BYTES2Date = new BYTES2Date();
    public static final Schema BYTES2DateTime = new BYTES2DateTime();

    public static class BCD2DateTime implements Schema<LocalDateTime> {

        private BCD2DateTime() {
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

    public static class BYTES2DateTime implements Schema<LocalDateTime> {

        private BYTES2DateTime() {
        }

        @Override
        public LocalDateTime readFrom(ByteBuf input) {
            byte[] bytes = new byte[6];
            input.readBytes(bytes);
            return Bytes.toDateTime(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDateTime value) {
            output.writeBytes(Bytes.from(value));
        }
    }

    public static class BYTES2Date implements Schema<LocalDate> {
        private BYTES2Date() {
        }

        @Override
        public LocalDate readFrom(ByteBuf input) {
            byte[] bytes = new byte[3];
            input.readBytes(bytes);
            return Bytes.toDate(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDate value) {
            output.writeBytes(Bytes.from(value));
        }
    }

    public static class BYTES2Time implements Schema<LocalTime> {
        private BYTES2Time() {
        }

        @Override
        public LocalTime readFrom(ByteBuf input) {
            byte[] bytes = new byte[3];
            input.readBytes(bytes);
            return Bytes.toTime(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalTime value) {
            output.writeBytes(Bytes.from(value));
        }
    }
}