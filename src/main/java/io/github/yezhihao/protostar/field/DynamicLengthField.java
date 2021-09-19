package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 动态长度的字段
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public class DynamicLengthField<T> extends BasicField<T> {

    protected final Schema schema;

    protected final int lengthSize;

    public DynamicLengthField(Field field, java.lang.reflect.Field f, Schema schema) {
        super(field, f);
        this.schema = schema;
        this.lengthSize = field.lengthSize();
    }

    public boolean readFrom(ByteBuf input, Object message) throws Exception {
        int length = ByteBufUtils.readInt(input, lengthSize);
        if (!input.isReadable(length))
            return false;
        Object value = schema.readFrom(input, length);
        f.set(message, value);
        return true;
    }

    public void writeTo(ByteBuf output, Object message) throws Exception {
        Object value = f.get(message);
        if (value != null) {
            int begin = output.writerIndex();
            ByteBufUtils.writeInt(output, lengthSize, 0);
            schema.writeTo(output, value);
            int length = output.writerIndex() - begin - lengthSize;
            ByteBufUtils.setInt(output, begin, lengthSize, length);
        }
    }

    @Override
    public int compareTo(BasicField<T> that) {
        int r = Integer.compare(this.index, that.index);
        if (r == 0)
            r = (that instanceof DynamicLengthField) ? 1 : -1;
        return r;
    }

    public static class Logger<T> extends DynamicLengthField<T> {

        public Logger(Field field, java.lang.reflect.Field f, Schema<T> schema) {
            super(field, f, schema);
        }

        public boolean readFrom(ByteBuf input, Object message) throws Exception {
            int before = input.readerIndex();

            int length = ByteBufUtils.readInt(input, lengthSize);
            if (!input.isReadable(length))
                return false;
            Object value = schema.readFrom(input, length);
            f.set(message, value);

            int after = input.readerIndex();
            String hex = ByteBufUtil.hexDump(input.slice(before, after - before));
            println(this.index, this.field.desc(), hex, value);
            return true;
        }

        public void writeTo(ByteBuf output, Object message) throws Exception {
            int before = output.writerIndex();

            Object value = f.get(message);
            if (value != null) {
                int begin = output.writerIndex();
                ByteBufUtils.writeInt(output, lengthSize, 0);
                schema.writeTo(output, value);
                int length = output.writerIndex() - begin - lengthSize;
                ByteBufUtils.setInt(output, begin, lengthSize, length);
            }

            int after = output.writerIndex();
            String hex = ByteBufUtil.hexDump(output.slice(before, after - before));
            println(this.index, this.field.desc(), hex, value);
        }
    }
}