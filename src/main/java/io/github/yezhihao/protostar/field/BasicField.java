package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.schema.SchemaRegistry;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;

/**
 * 消息结构
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public abstract class BasicField<T> implements Schema<T>, Comparable<BasicField> {

    protected java.lang.reflect.Field f;
    protected Field field;
    protected int index;
    protected int length;
    protected String desc;

    public void readAndSet(ByteBuf input, Object obj) throws Exception {
        T value = readFrom(input);
        f.set(obj, value);
    }

    public void getAndWrite(ByteBuf output, Object obj) throws Exception {
        T value = (T) f.get(obj);
        writeTo(output, value);
    }

    public void readAndSet(ByteBuf input, Object obj, Explain explain) throws Exception {
        T value = readFrom(input, explain);
        f.set(obj, value);
    }

    public void getAndWrite(ByteBuf output, Object obj, Explain explain) throws Exception {
        T value = (T) f.get(obj);
        writeTo(output, value, explain);
    }

    public BasicField init(Field field, java.lang.reflect.Field f, int position) {
        if (this.f == null && this.field == null) {
            this.f = f;
            this.field = field;
            length = field.length() > 0 ? field.length() : SchemaRegistry.getLength(f.getType());
            length = length > 0 ? length : 16;
            desc = field.desc();
            if (desc.isEmpty())
                desc = f.getName();
            index = field.index();
            if (index == 0)
                index = position;
        }
        return this;
    }

    public String fieldName() {
        return f.getName();
    }

    public String desc() {
        return desc;
    }

    /** 用于预估内存分配，不需要精确值 */
    public int length() {
        return length;
    }

    @Override
    public int compareTo(BasicField that) {
        return Integer.compare(this.index, that.index);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof BasicField)) return false;

        BasicField that = (BasicField) other;
        return f.equals(that.f);
    }

    @Override
    public int hashCode() {
        return f.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(12);
        sb.append(desc).append(' ').append(field);
        return sb.toString();
    }
}