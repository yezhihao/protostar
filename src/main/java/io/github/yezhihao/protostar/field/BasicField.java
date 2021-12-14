package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.netty.buffer.ByteBuf;

/**
 * 固定长度域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class BasicField<T> implements Schema<T>, Comparable<BasicField> {

    protected final int length;
    protected final Field field;
    protected final Schema<T> schema;
    protected final java.lang.reflect.Field f;
    protected final String desc;

    public BasicField(Field field, java.lang.reflect.Field f, Schema<T> schema) {
        this.schema = schema;
        this.length = field.length() != 0 ? field.length() : schema.length();
        this.field = field;
        this.f = f;
        try {
            f.setAccessible(true);
        } catch (Exception e) {
        }
        String t = field.desc();
        if (t.length() == 0)
            t = f.getName();
        this.desc = t;
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

    @Override
    public String desc() {
        return desc;
    }

    @Override
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
        sb.append('[');
        sb.append("index=").append(field.index());
        sb.append(", length=").append(length);
        sb.append(", field=").append(f.getName());
        sb.append(", desc").append(field.desc());
        sb.append(']');
        return sb.toString();
    }
}