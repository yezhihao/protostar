package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Cache;
import io.github.yezhihao.protostar.util.CharsBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class StringSchema {

    private static final Logger log = LoggerFactory.getLogger(StringSchema.class.getSimpleName());
    private static final Cache<String, Schema> cache = new Cache<>();

    public static final Schema<String> ASCII = StringSchema.getInstance("US-ASCII");
    public static final Schema<String> UTF8 = StringSchema.getInstance("UTF-8");
    public static final Schema<String> GBK = StringSchema.getInstance("GBK");
    public static final Schema<String> BCD = cache.put("BCD", new BCD());
    public static final Schema<String> HEX = cache.put("HEX", new HEX());

    public static Schema<String> getInstance(final String charset) {
        return cache.get(charset.toUpperCase(), () -> new STR(charset));
    }

    private static class STR implements Schema<String> {
        private final byte pad = 0;
        private final Charset charset;

        private STR(String charset) {
            this.charset = Charset.forName(charset);
        }

        @Override
        public String readFrom(ByteBuf input) {
            int length = input.readUnsignedByte();
            return readFrom(input, length);
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
            int length = bytes.length > 255 ? 255 : bytes.length;
            output.writeBytes(bytes, 0, length);
        }

        @Override
        public void writeTo(ByteBuf output, int length, String value) {
            ByteBuffer buffer = charset.encode(value);
            if (length > 0) {
                int srcPos = length - buffer.limit();

                if (srcPos > 0) {
                    byte[] pads = new byte[srcPos];
                    output.writeBytes(pads);
                    output.writeBytes(buffer);
                } else if (srcPos < 0) {
                    buffer.position(-srcPos);
                    output.writeBytes(buffer);
                    log.info("字符长度超出限制: 长度[{}],数据长度[{}],{}", length, buffer.limit(), value);
                } else {
                    output.writeBytes(buffer);
                }
            } else {
                output.writeBytes(buffer);
            }
        }
    }

    private static class BCD implements Schema<String> {
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

    private static class HEX implements Schema<String> {
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