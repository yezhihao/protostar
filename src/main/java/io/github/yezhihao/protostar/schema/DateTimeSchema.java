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

    public static final Schema<LocalTime> BYTE_TIME = new Time3(DateTool.BYTE);
    public static final Schema<LocalDate> BYTE_DATE = new Date3(DateTool.BYTE);
    public static final Schema<LocalDateTime> BYTE_DATETIME = new DateTime6(DateTool.BYTE);

    public static final Schema<LocalTime> BCD_TIME = new Time3(DateTool.BCD);
    public static final Schema<LocalDate> BCD_DATE = new Date3(DateTool.BCD);
    public static final Schema<LocalDateTime> BCD_DATETIME = new DateTime6(DateTool.BCD);

    protected static class DateTime6 extends BasicField<LocalDateTime> {
        protected final DateTool tool;

        protected DateTime6(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalDateTime readFrom(ByteBuf input) {
            return tool.readDateTime6(input);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDateTime value) {
            tool.writeDateTime6(output, value);
        }
    }

    protected static class DateTime7 extends BasicField<LocalDateTime> {
        protected final DateTool tool;

        protected DateTime7(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalDateTime readFrom(ByteBuf input) {
            return tool.readDateTime7(input);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDateTime value) {
            tool.writeDateTime7(output, value);
        }
    }

    protected static class Date3 extends BasicField<LocalDate> {
        protected final DateTool tool;

        protected Date3(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalDate readFrom(ByteBuf input) {
            return tool.readDate3(input);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDate value) {
            tool.writeDate3(output, value);
        }
    }

    protected static class Date4 extends BasicField<LocalDate> {
        protected final DateTool tool;

        protected Date4(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalDate readFrom(ByteBuf input) {
            return tool.readDate4(input);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDate value) {
            tool.writeDate4(output, value);
        }
    }

    protected static class Time3 extends BasicField<LocalTime> {
        protected final DateTool tool;

        protected Time3(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalTime readFrom(ByteBuf input) {
            return tool.readTime3(input);
        }

        @Override
        public void writeTo(ByteBuf output, LocalTime value) {
            tool.writeTime3(output, value);
        }
    }

    protected static class Time2 extends BasicField<LocalTime> {
        protected final DateTool tool;

        protected Time2(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalTime readFrom(ByteBuf input) {
            return tool.readTime2(input);
        }

        @Override
        public void writeTo(ByteBuf output, LocalTime value) {
            tool.writeTime2(output, value);
        }
    }
}