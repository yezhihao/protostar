package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.field.BasicField;
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

    public static final Schema<LocalTime> BYTE_TIME = new Time(DateTool.BYTE);
    public static final Schema<LocalDate> BYTE_DATE = new Date(DateTool.BYTE);
    public static final Schema<LocalDateTime> BYTE_DATETIME = new DateTime(DateTool.BYTE);

    public static final Schema<LocalTime> BCD_TIME = new Time(DateTool.BCD);
    public static final Schema<LocalDate> BCD_DATE = new Date(DateTool.BCD);
    public static final Schema<LocalDateTime> BCD_DATETIME = new DateTime(DateTool.BCD);

    protected static class DateTime extends BasicField<LocalDateTime> {
        protected final DateTool tool;

        protected DateTime(DateTool tool) {
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

    protected static class Date extends BasicField<LocalDate> {
        protected final DateTool tool;

        protected Date(DateTool tool) {
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

    protected static class Time extends BasicField<LocalTime> {
        protected final DateTool tool;

        protected Time(DateTool tool) {
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