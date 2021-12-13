package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Fixed;
import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.DateTool;
import io.netty.buffer.ByteBuf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class DateTimeSchema {

    public static final Schema BYTE_TIME = new Time(DateTool.BYTE);
    public static final Schema BYTE_DATE = new Date(DateTool.BYTE);
    public static final Schema BYTE_DATETIME = new DateTime(DateTool.BYTE);

    public static final Schema BCD_TIME = new Time(DateTool.BCD);
    public static final Schema BCD_DATE = new Date(DateTool.BCD);
    public static final Schema BCD_DATETIME = new DateTime(DateTool.BCD);

    private static class DateTime implements Fixed.L6<LocalDateTime> {
        private final DateTool tool;

        private DateTime(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalDateTime readFrom(ByteBuf input) {
            byte[] bytes = new byte[6];
            input.readBytes(bytes);
            return tool.toDateTime(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDateTime value) {
            output.writeBytes(tool.from(value));
        }
    }

    private static class Date implements Fixed.L3<LocalDate> {
        private final DateTool tool;

        private Date(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalDate readFrom(ByteBuf input) {
            byte[] bytes = new byte[3];
            input.readBytes(bytes);
            return tool.toDate(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDate value) {
            output.writeBytes(tool.from(value));
        }
    }

    private static class Time implements Fixed.L3<LocalTime> {
        private final DateTool tool;

        private Time(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalTime readFrom(ByteBuf input) {
            byte[] bytes = new byte[3];
            input.readBytes(bytes);
            return tool.toTime(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalTime value) {
            output.writeBytes(tool.from(value));
        }
    }
}