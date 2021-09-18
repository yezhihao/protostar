package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.StrUtils;
import io.netty.buffer.ByteBuf;

/**
 * 固定长度的字段
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public abstract class BasicField<T> implements Comparable<BasicField<T>> {

    protected final int index;
    protected final int length;
    protected final Field field;
    protected final java.lang.reflect.Field f;

    public BasicField(Field field, java.lang.reflect.Field f) {
        this.index = field.index();
        int length = field.length();
        if (length < 0)
            length = field.type().length;
        this.length = length;
        this.field = field;
        this.f = f;
        try {
            f.setAccessible(true);
        } catch (Exception e) {
        }
    }

    public abstract boolean readFrom(ByteBuf input, Object message) throws Exception;

    public abstract void writeTo(ByteBuf output, Object message) throws Exception;

    public void println(int index, String desc, String hex, Object value) {
        if (value != null)
            System.out.println(index + "\t" + "[" + hex + "] " + desc + ": " + StrUtils.toString(value));
    }

    public int index() {
        return index;
    }

    public int length() {
        return length;
    }

    @Override
    public int compareTo(BasicField<T> that) {
        return Integer.compare(this.index, that.index);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(50);
        sb.append('{');
        sb.append("index=").append(index);
        sb.append(", length=").append(length);
        sb.append(", desc").append(field.desc());
        sb.append(", field=").append(f.getName());
        sb.append('}');
        return sb.toString();
    }
}