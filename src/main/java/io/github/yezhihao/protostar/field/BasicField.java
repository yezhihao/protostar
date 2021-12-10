package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 基本类型字段
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class BasicField<T> implements Schema<T>, Comparable<BasicField> {

    protected final int lengthSize;
    protected final int length;
    protected final Field field;
    protected final Schema<T> schema;
    protected final java.lang.reflect.Field f;

    public BasicField(Field field, java.lang.reflect.Field f, Schema<T> schema) {
        this.schema = schema;
        int length = field.length();
        if (length < 0)
            length = field.type().length;
        this.length = length;
        this.lengthSize = field.lengthSize();
        this.field = field;
        this.f = f;
        try {
            f.setAccessible(true);
        } catch (Exception e) {
        }
    }

    public void set(Object message, Object value) throws IllegalAccessException {
        f.set(message, value);
    }

    public Object get(Object message) throws IllegalAccessException {
        return f.get(message);
    }

    @Override
    public T readFrom(ByteBuf input) {
        return schema.readFrom(input);
    }

    @Override
    public void writeTo(ByteBuf output, T value) {
        if (value != null)
            schema.writeTo(output, value);
    }

    public T readFrom(ByteBuf input, Explain explain) {
        int before = input.readerIndex();

        T value;
        if (schema instanceof RuntimeSchema)
            value = ((RuntimeSchema<T>) schema).readFrom(input, explain);
        else
            value = schema.readFrom(input);

        int after = input.readerIndex();
        String hex = ByteBufUtil.hexDump(input.slice(before, after - before));
        explain.add(Info.field(before, field, hex, value));
        return value;
    }

    public void writeTo(ByteBuf output, T value, Explain explain) {
        int before = output.writerIndex();

        if (value != null)
            schema.writeTo(output, value);

        int after = output.writerIndex();
        String hex = ByteBufUtil.hexDump(output.slice(before, after - before));
        explain.add(Info.field(before, field, hex, value));
    }

    public int length() {
        return length;
    }

    @Override
    public int compareTo(BasicField that) {
        return Integer.compare(this.field.index(), that.field.index());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(50);
        sb.append('{');
        sb.append("index=").append(field.index());
        sb.append(", length=").append(length);
        sb.append(", desc").append(field.desc());
        sb.append(", field=").append(f.getName());
        sb.append('}');
        return sb.toString();
    }
}