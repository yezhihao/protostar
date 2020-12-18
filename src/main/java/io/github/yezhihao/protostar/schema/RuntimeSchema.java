package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.field.BasicField;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

/**
 * 运行时根据Class生成的消息结构，用于序列化对象
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public class RuntimeSchema<T> implements Schema<T> {

    protected final int version;
    protected final int length;
    protected final Class<T> typeClass;
    protected final BasicField[] fields;
    protected final Constructor<T> constructor;

    public RuntimeSchema(Class<T> typeClass, int version, BasicField[] fields) {
        this.typeClass = typeClass;
        this.version = version;
        this.fields = fields;
        BasicField lastField = fields[fields.length - 1];
        int lastIndex = lastField.index();
        int lastLength = lastField.length() < 0 ? 256 : lastField.length();
        this.length = lastIndex + lastLength;
        try {
            this.constructor = typeClass.getDeclaredConstructor((Class[]) null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T readFrom(ByteBuf input) {
        BasicField field = null;
        try {
            T result = constructor.newInstance((Object[]) null);
            for (int i = 0; i < fields.length; i++) {
                if (!input.isReadable())
                    break;
                field = fields[i];
                field.readFrom(input, result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + typeClass.getName() + field, e);
        }
    }

    public void writeTo(ByteBuf output, T message) {
        BasicField field = null;
        try {
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];
                field.writeTo(output, message);
            }
        } catch (Exception e) {
            throw new RuntimeException("Write failed " + typeClass.getName() + field, e);
        }
    }

    public int length() {
        return length;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(48);
        sb.append('{');
        sb.append("typeClass=").append(typeClass.getSimpleName());
        sb.append(", version=").append(version);
        sb.append(", length=").append(length);
        sb.append('}');
        return sb.toString();
    }
}