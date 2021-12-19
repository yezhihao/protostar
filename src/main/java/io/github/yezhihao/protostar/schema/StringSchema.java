package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.field.BasicField;
import io.github.yezhihao.protostar.field.LengthUnitField;
import io.github.yezhihao.protostar.util.CharsBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public interface StringSchema {

    Logger log = LoggerFactory.getLogger(StringSchema.class.getSimpleName());

    Schema<String> HEX = new HEX(-1);
    Schema<String> BCD = new BCD(-1);
    Schema<String> GBK = new STR(Charset.forName("GBK"), -1);
    Schema<String> UTF8 = new STR(Charset.forName("UTF-8"), -1);
    Schema<String> ASCII = new STR(Charset.forName("US-ASCII"), -1);

    BasicField<String> getInstance(String charset, int length, int lengthUnit);

    StringSchema SCHEMA = new StringSchema() {
        @Override
        public BasicField<String> getInstance(String charset, int length, int lengthUnit) {
            final String cs = charset.toUpperCase();
            BasicField<String> schema;
            if ("BCD".equals(cs))
                schema = new BCD(length);
            else if ("HEX".equals(cs))
                schema = new HEX(length);
            else
                schema = new STR(Charset.forName(charset), length);

            if (lengthUnit > 0)
                schema = new LengthUnitField(schema, lengthUnit);

            return schema;
        }
    };


    class STR extends BasicField<String> {
        private final byte pad = 0;
        private final Charset charset;
        private final int length;

        private STR(Charset charset, int length) {
            this.charset = charset;
            this.length = length;
        }

        @Override
        public String readFrom(ByteBuf input) {
            return readFrom(input, length);
        }

        @Override
        public String readFrom(ByteBuf input, int length) {
            int len = length >= 0 ? length : input.readableBytes();
            if (!input.isReadable(len))
                return null;
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
            writeTo(output, length, value);
        }

        @Override
        public void writeTo(ByteBuf output, int length, String value) {
            if (value == null)
                return;
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

    class BCD extends BasicField<String> {
        private final int length;

        public BCD(int length) {
            this.length = length;
        }

        @Override
        public String readFrom(ByteBuf input) {
            return readFrom(input, length);
        }

        @Override
        public String readFrom(ByteBuf input, int length) {
            int len = length >= 0 ? length : input.readableBytes();
            byte[] bytes = new byte[len];
            input.readBytes(bytes);

            CharsBuilder cb = new CharsBuilder(length << 1);
            StringUtil.toHexStringPadded(cb, bytes);
            return cb.leftStrip('0');
        }

        @Override
        public void writeTo(ByteBuf output, String value) {
            writeTo(output, length, value);
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

    class HEX extends BasicField<String> {
        private final int length;

        public HEX(int length) {
            this.length = length;
        }

        @Override
        public String readFrom(ByteBuf input) {
            return readFrom(input, length);
        }

        @Override
        public String readFrom(ByteBuf input, int length) {
            int len = length >= 0 ? length : input.readableBytes();
            byte[] bytes = new byte[len];
            input.readBytes(bytes);

            CharsBuilder cb = new CharsBuilder(length << 1);
            StringUtil.toHexStringPadded(cb, bytes);
            return cb.toString();
        }

        @Override
        public void writeTo(ByteBuf output, String value) {
            writeTo(output, length, value);
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