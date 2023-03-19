package io.github.yezhihao.protostar.util;

import io.netty.buffer.ByteBuf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 日期编码工具类
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class DateTool {

    public static final DateTool BYTE = new DateTool();
    public static final DateTool BCD = new BCD();

    public static final int YEAR = LocalDate.now().getYear();
    public static final int YEAR_RANGE = YEAR - 30;
    public static final int HUNDRED_YEAR = YEAR_RANGE / 100 * 100;

    public static int getYear(int year) {
        year += HUNDRED_YEAR;
        if (year < YEAR_RANGE)
            year += 100;
        return year;
    }

    /** 时间转byte[] (yyMMddHHmmss) */
    public final byte[] from(LocalDateTime dateTime) {
        byte[] bytes = new byte[6];
        if (dateTime != null) {
            bytes[0] = toByte(dateTime.getYear() % 100);
            bytes[1] = toByte(dateTime.getMonthValue());
            bytes[2] = toByte(dateTime.getDayOfMonth());
            bytes[3] = toByte(dateTime.getHour());
            bytes[4] = toByte(dateTime.getMinute());
            bytes[5] = toByte(dateTime.getSecond());
        }
        return bytes;
    }

    /** byte[]转时间 (yyMMddHHmmss) */
    public final LocalDateTime toDateTime(byte[] bytes) {
        try {
            return LocalDateTime.of(
                    getYear(toInt(bytes[0])),
                    toInt(bytes[1]),
                    toInt(bytes[2]),
                    toInt(bytes[3]),
                    toInt(bytes[4]),
                    toInt(bytes[5]));
        } catch (Exception e) {
            return null;
        }
    }

    /** 日期转byte[] (yyMMdd) */
    public final byte[] from(LocalDate date) {
        return new byte[]{
                toByte((date.getYear() % 100)),
                toByte(date.getMonthValue()),
                toByte(date.getDayOfMonth())
        };
    }

    /** byte[]转日期 (yyMMdd) */
    public final LocalDate toDate(byte[] bytes) {
        return LocalDate.of(
                getYear(toInt(bytes[0])),
                toInt(bytes[1]),
                toInt(bytes[2])
        );
    }

    /** 时间转byte[] (HHmmss) */
    public final byte[] from(LocalTime time) {
        return new byte[]{
                toByte(time.getHour()),
                toByte(time.getMinute()),
                toByte(time.getSecond())
        };
    }

    /** byte[]转时间 (HHmmss) */
    public final LocalTime toTime(byte[] bytes) {
        return LocalTime.of(
                toInt(bytes[0]),
                toInt(bytes[1]),
                toInt(bytes[2])
        );
    }

    /** 写入2字节时间(HHmm) */
    public final void writeTime2(ByteBuf output, LocalTime time) {
        output.writeByte(toByte(time.getHour())).writeByte(toByte(time.getMinute()));
    }

    /** 读取2字节时间(HHmm) */
    public final LocalTime readTime2(ByteBuf input) {
        int hour = toInt(input.readByte());
        int minute = toInt(input.readByte());
        try {
            return LocalTime.of(hour, minute);
        } catch (Exception e) {
            return null;
        }
    }

    /** 写入3字节时间(HHmmss) */
    public final void writeTime3(ByteBuf output, LocalTime time) {
        output.writeByte(toByte(time.getHour())).writeByte(toByte(time.getMinute())).writeByte(toByte(time.getSecond()));
    }

    /** 读取3字节时间(HHmmss) */
    public final LocalTime readTime3(ByteBuf input) {
        int hour = toInt(input.readByte());
        int minute = toInt(input.readByte());
        int second = toInt(input.readByte());
        try {
            return LocalTime.of(hour, minute, second);
        } catch (Exception e) {
            return null;
        }
    }

    /** 写入3字节日期(yyMMdd) */
    public final void writeDate3(ByteBuf output, LocalDate time) {
        output.writeByte(toByte(time.getYear() % 100)).writeByte(toByte(time.getMonthValue())).writeByte(toByte(time.getDayOfMonth()));
    }

    /** 读取3字节日期(yyMMdd) */
    public final LocalDate readDate3(ByteBuf input) {
        int year = getYear(toInt(input.readByte()));
        int month = toInt(input.readByte());
        int dayOfMonth = toInt(input.readByte());
        try {
            return LocalDate.of(year, month, dayOfMonth);
        } catch (Exception e) {
            return null;
        }
    }

    /** 写入4字节日期(yyyyMMdd) */
    public final void writeDate4(ByteBuf output, LocalDate time) {
        output.writeByte(toByte(time.getYear() / 100)).writeByte(toByte(time.getYear() % 100)).writeByte(toByte(time.getMonthValue())).writeByte(toByte(time.getDayOfMonth()));
    }

    /** 读取4字节日期(yyyyMMdd) */
    public final LocalDate readDate4(ByteBuf input) {
        int year = (toInt(input.readByte()) * 100) + toInt(input.readByte());
        int month = toInt(input.readByte());
        int dayOfMonth = toInt(input.readByte());
        try {
            return LocalDate.of(year, month, dayOfMonth);
        } catch (Exception e) {
            return null;
        }
    }

    /** 写入6字节时间(yyMMddHHmmss) */
    public final void writeDateTime6(ByteBuf output, LocalDateTime dateTime) {
        writeDate3(output, dateTime.toLocalDate());
        writeTime3(output, dateTime.toLocalTime());
    }

    /** 读取6字节时间(yyMMdddHHmmss) */
    public final LocalDateTime readDateTime6(ByteBuf input) {
        LocalDate date = readDate3(input);
        LocalTime time = readTime3(input);
        if (date == null || time == null)
            return null;
        return LocalDateTime.of(date, time);
    }

    /** 写入7字节时间(yyyyMMddHHmmss) */
    public final void writeDateTime7(ByteBuf output, LocalDateTime dateTime) {
        writeDate4(output, dateTime.toLocalDate());
        writeTime3(output, dateTime.toLocalTime());
    }

    /** 读取7字节时间(yyyyMMdddHHmmss) */
    public final LocalDateTime readDateTime7(ByteBuf input) {
        LocalDate date = readDate4(input);
        LocalTime time = readTime3(input);
        if (date == null || time == null)
            return null;
        return LocalDateTime.of(date, time);
    }

    private DateTool() {
    }

    public byte toByte(int i) {
        return (byte) i;
    }

    public int toInt(byte b) {
        return b & 0xff;
    }

    private static class BCD extends DateTool {
        public byte toByte(int i) {
            return (byte) ((i / 10 << 4) | (i % 10 & 0xf));
        }

        public int toInt(byte b) {
            return (b >> 4 & 0xf) * 10 + (b & 0xf);
        }
    }
}