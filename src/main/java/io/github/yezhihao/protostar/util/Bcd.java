package io.github.yezhihao.protostar.util;

import io.netty.buffer.ByteBuf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * BCD编码工具类
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 * @see DateTimeUtils
 */
@Deprecated
public class Bcd {

    public static final int YEAR = LocalDate.now().getYear();
    public static final int YEAR_RANGE = YEAR - 30;
    public static final int HUNDRED_YEAR = YEAR_RANGE / 100 * 100;

    /** 时间转BCD (yyMMddHHmmss) */
    public static byte[] from(LocalDateTime dateTime) {
        byte[] bcd = new byte[6];
        bcd[0] = bcd(dateTime.getYear() % 100);
        bcd[1] = bcd(dateTime.getMonthValue());
        bcd[2] = bcd(dateTime.getDayOfMonth());
        bcd[3] = bcd(dateTime.getHour());
        bcd[4] = bcd(dateTime.getMinute());
        bcd[5] = bcd(dateTime.getSecond());
        return bcd;
    }

    /** BCD转时间 (yyMMddHHmmss) */
    public static LocalDateTime toDateTime(byte[] bcd) {
        int i = bcd.length - 1;
        int year = HUNDRED_YEAR + num(bcd[i - 5]);
        if (year < YEAR_RANGE)
            year += 100;
        try {
            return LocalDateTime.of(
                    year,
                    num(bcd[i - 4]),
                    num(bcd[i - 3]),
                    num(bcd[i - 2]),
                    num(bcd[i - 1]),
                    num(bcd[i]));
        } catch (Exception e) {
            return null;
        }
    }

    /** 日期转BCD (yyMMdd) */
    public static byte[] from(LocalDate date) {
        byte[] bcd = new byte[3];
        bcd[0] = bcd(date.getYear() % 100);
        bcd[1] = bcd(date.getMonthValue());
        bcd[2] = bcd(date.getDayOfMonth());
        return bcd;
    }

    /** BCD转日期 (yyMMdd) */
    public static LocalDate toDate(byte[] bcd) {
        int i = bcd.length - 1;
        int year = HUNDRED_YEAR + num(bcd[i - 2]);
        if (year < YEAR_RANGE)
            year += 100;
        return LocalDate.of(year, num(bcd[i - 1]), num(bcd[i]));
    }

    /** BCD转时间 (HHMM) */
    public static LocalTime readTime2(ByteBuf input) {
        return LocalTime.of(num(input.readByte()), num(input.readByte()));
    }

    /** BCD转时间 (HHMM) */
    public static void writeTime2(ByteBuf output, LocalTime time) {
        output.writeByte(bcd(time.getHour()));
        output.writeByte(bcd(time.getMinute()));
    }

    public static byte bcd(int num) {
        return (byte) ((num / 10 << 4) | (num % 10 & 0xf));
    }

    public static int num(byte bcd) {
        return (bcd >> 4 & 0xf) * 10 + (bcd & 0xf);
    }
}