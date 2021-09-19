package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.ByteBufUtils;
import io.github.yezhihao.protostar.util.StrUtils;
import io.netty.buffer.ByteBuf;

import java.util.Collection;

/**
 * 动态长度的字段
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public class DynamicTotalField<T> extends BasicField<T> {

    protected final Schema<Collection<?>> schema;

    protected final int totalSize;

    public DynamicTotalField(Field field, java.lang.reflect.Field f, Schema schema) {
        super(field, f);
        this.schema = schema;
        this.totalSize = field.lengthSize();
    }

    public boolean readFrom(ByteBuf input, Object message) throws Exception {
        Object value = schema.readFrom(input, totalSize);
        f.set(message, value);
        return true;
    }

    public void writeTo(ByteBuf output, Object message) throws Exception {
        Collection value = (Collection) f.get(message);
        if (value != null)
            schema.writeTo(output, totalSize, value);
    }

    @Override
    public int compareTo(BasicField<T> that) {
        int r = Integer.compare(this.index, that.index);
        if (r == 0)
            r = (that instanceof DynamicTotalField) ? 1 : -1;
        return r;
    }

    public static class Logger<T> extends DynamicTotalField<T> {

        public Logger(Field field, java.lang.reflect.Field f, Schema<T> schema) {
            super(field, f, schema);
        }

        public boolean readFrom(ByteBuf input, Object message) throws Exception {
            int total = ByteBufUtils.getInt(input, input.readerIndex(), totalSize);
            String hex = StrUtils.leftPad(Integer.toHexString(total), totalSize << 1, '0');
            println(this.index, this.field.desc() + "总数", hex, total);

            Collection value = schema.readFrom(input, totalSize);
            f.set(message, value);
            return true;
        }

        public void writeTo(ByteBuf output, Object message) throws Exception {
            Collection value = (Collection) f.get(message);
            if (value != null) {

                int total = value.size();
                String hex = StrUtils.leftPad(Integer.toHexString(total), totalSize << 1, '0');
                println(this.index, this.field.desc() + "总数", hex, total);

                schema.writeTo(output, totalSize, value);
            }
        }
    }
}