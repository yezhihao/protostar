package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Cache;
import io.github.yezhihao.protostar.util.CharsBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Arrays;

public class StringSchema {

    private static final Logger log = LoggerFactory.getLogger(StringSchema.class.getSimpleName());

    public static class Chars implements Schema<String> {
        private static final Cache<String, Chars> cache = new Cache<>();

        public static Chars getInstance(final byte pad, final String charset) {
            String key = new StringBuilder(10).append((char) pad).append('/').append(charset.toLowerCase()).toString();
            return cache.get(key, k -> new Chars(pad, charset));
        }

        private final byte pad;
        private final Charset charset;

        private Chars(byte pad, String charset) {
            this.pad = pad;
            this.charset = Charset.forName(charset);
        }

        @Override
        public String readFrom(ByteBuf input) {
            return readFrom(input, input.readableBytes());
        }

        @Override
        public String readFrom(ByteBuf input, int length) {
            int len = length > 0 ? length : input.readableBytes();
            byte[] bytes = new byte[len];
            input.readBytes(bytes);

            int st = 0;
            while ((st < len) && (bytes[st] == pad))
                st++;
            while ((st < len) && (bytes[len - 1] == pad))
                len--;
            return new String(bytes, st, len - st, charset);
        }

        @Override
        public void writeTo(ByteBuf output, String value) {
            byte[] bytes = value.getBytes(charset);
            output.writeBytes(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, int length, String value) {
            byte[] bytes = value.getBytes(charset);
            if (length > 0) {
                int srcPos = length - bytes.length;

                if (srcPos > 0) {
                    byte[] pads = new byte[srcPos];
                    if (pad != 0x00)
                        Arrays.fill(pads, pad);
                    output.writeBytes(pads);
                    output.writeBytes(bytes);
                } else if (srcPos < 0) {
                    output.writeBytes(bytes, -srcPos, length);
                    log.info("字符长度超出限制: 长度[{}],数据长度[{}],{}", length, bytes.length, value);
                } else {
                    output.writeBytes(bytes);
                }
            } else {
                output.writeBytes(bytes);
            }
        }
    }

    public static class BCD implements Schema<String> {
        public static final Schema INSTANCE = new BCD();

        private BCD() {
        }

        @Override
        public String readFrom(ByteBuf input) {
            return readFrom(input, input.readableBytes());
        }

        @Override
        public String readFrom(ByteBuf input, int length) {
            byte[] bytes = new byte[length];
            input.readBytes(bytes);

            CharsBuilder cb = new CharsBuilder(length << 1);
            StringUtil.toHexStringPadded(cb, bytes);
            return cb.leftStrip('0');
        }

        @Override
        public void writeTo(ByteBuf output, String value) {
            writeTo(output, value.length() >> 1, value);
        }

        @Override
        public void writeTo(ByteBuf output, int length, String value) {
            int charLength = length << 1;
            char[] chars = new char[charLength];
            int i = charLength - value.length();
            if (i >= 0) {
                value.getChars(0, charLength - i, chars, i);
                while (i > 0)
                    chars[--i] = '0';
            } else {
                value.getChars(-i, charLength - i, chars, 0);
                log.info("字符长度超出限制: 长度[{}],[{}]", charLength, value);
            }
            byte[] src = StringUtil.decodeHexDump(new CharsBuilder(chars));
            output.writeBytes(src);
        }
    }

    public static class HEX implements Schema<String> {
        public static final Schema INSTANCE = new HEX();

        private HEX() {
        }

        @Override
        public String readFrom(ByteBuf input) {
            return readFrom(input, input.readableBytes());
        }

        @Override
        public String readFrom(ByteBuf input, int length) {
            byte[] bytes = new byte[length];
            input.readBytes(bytes);

            CharsBuilder cb = new CharsBuilder(length << 1);
            StringUtil.toHexStringPadded(cb, bytes);
            return cb.toString();
        }

        @Override
        public void writeTo(ByteBuf output, String value) {
            writeTo(output, value.length() >> 1, value);
        }

        @Override
        public void writeTo(ByteBuf output, int length, String value) {
            int charLength = length << 1;
            char[] chars = new char[charLength];
            int i = charLength - value.length();
            if (i >= 0) {
                value.getChars(0, charLength - i, chars, i);
                while (i > 0)
                    chars[--i] = '0';
            } else {
                value.getChars(-i, charLength - i, chars, 0);
                log.info("字符长度超出限制: 长度[{}],[{}]", charLength, value);
            }
            byte[] src = StringUtil.decodeHexDump(new CharsBuilder(chars));
            output.writeBytes(src);
        }
    }
}