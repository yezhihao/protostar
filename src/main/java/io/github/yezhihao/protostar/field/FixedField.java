package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 原子域
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public class FixedField<T> extends BasicField<T> {

    protected final Schema<T> schema;

    public FixedField(Field field, java.lang.reflect.Field f, Schema<T> schema) {
        super(field, f);
        this.schema = schema;
    }

    public boolean readFrom(ByteBuf input, Object message) throws Exception {
        Object value = schema.readFrom(input);
        f.set(message, value);
        return true;
    }

    public void writeTo(ByteBuf output, Object message) throws Exception {
        Object value = f.get(message);
        if (value != null)
            schema.writeTo(output, (T) value);
    }

    public static class Logger<T> extends FixedField<T> {

        public Logger(Field field, java.lang.reflect.Field f, Schema<T> schema) {
            super(field, f, schema);
        }

        public boolean readFrom(ByteBuf input, Object message) throws Exception {
            int before = input.readerIndex();

            Object value = schema.readFrom(input);
            f.set(message, value);

            int after = input.readerIndex();
            String hex = ByteBufUtil.hexDump(input.slice(before, after - before));
            println(this.index, this.field.desc(), hex, value);
            return true;
        }

        public void writeTo(ByteBuf output, Object message) throws Exception {
            int before = output.writerIndex();

            Object value = f.get(message);
            if (value != null)
                schema.writeTo(output, (T) value);

            int after = output.writerIndex();
            String hex = ByteBufUtil.hexDump(output.slice(before, after - before));
            println(this.index, this.field.desc(), hex, value);
        }
    }
}