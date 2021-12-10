package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.field.BasicField;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

/**
 * 运行时根据Class生成的消息结构，用于序列化对象
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
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
        int length = 0;
        for (BasicField field : fields) {
            int t = field.length();
            if (t < 0) t = 32;
            length += t;
        }
        this.length = length;
        try {
            this.constructor = typeClass.getDeclaredConstructor((Class[]) null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T newInstance() {
        try {
            return constructor.newInstance((Object[]) null);
        } catch (Exception e) {
            throw new RuntimeException("newInstance failed " + typeClass.getName(), e);
        }
    }

    public T mergeFrom(ByteBuf input, T result) {
        BasicField field = null;
        try {
            for (int i = 0; i < fields.length; i++) {
                if (!input.isReadable())
                    break;
                field = fields[i];
                Object value = field.readFrom(input);
                field.set(result, value);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + typeClass.getName() + field, e);
        }
    }

    public T mergeFrom(ByteBuf input, T result, Explain explain) {
        BasicField field = null;
        try {
            for (int i = 0; i < fields.length; i++) {
                if (!input.isReadable())
                    break;
                field = fields[i];
                Object value = field.readFrom(input, explain);
                field.set(result, value);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + typeClass.getName() + field, e);
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
                Object value = field.readFrom(input);
                field.set(result, value);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + typeClass.getName() + field, e);
        }
    }


    public T readFrom(ByteBuf input, Explain explain) {
        BasicField field = null;
        try {
            T result = constructor.newInstance((Object[]) null);
            for (int i = 0; i < fields.length; i++) {
                if (!input.isReadable())
                    break;
                field = fields[i];

                Object value = field.readFrom(input, explain);
                field.set(result, value);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + typeClass.getName() + field, e);
        }
    }

    public T readFrom(ByteBuf input, int length, Explain explain) {
        int writerIndex = input.writerIndex();
        input.writerIndex(input.readerIndex() + length);
        T result = readFrom(input, explain);
        input.writerIndex(writerIndex);
        return result;
    }

    @Override
    public T readFrom(ByteBuf input, int length) {
        int writerIndex = input.writerIndex();
        input.writerIndex(input.readerIndex() + length);
        T result = readFrom(input);
        input.writerIndex(writerIndex);
        return result;
    }

    public void writeTo(ByteBuf output, T message) {
        BasicField field = null;
        try {
            for (int i = 0; i < fields.length; i++) {
                field = fields[i];
                Object value = field.get(message);
                field.writeTo(output, value);
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