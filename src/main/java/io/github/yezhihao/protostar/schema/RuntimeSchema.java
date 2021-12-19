package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.field.BasicField;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

/**
 * 运行时根据Class生成的消息结构，用于序列化对象
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class RuntimeSchema<T> extends BasicField<T> {

    protected int version;
    protected int length;
    protected Class<T> typeClass;
    protected BasicField[] schemas;
    protected Constructor<T> constructor;

    public RuntimeSchema(Class<T> typeClass, int version, BasicField[] schemas) {
        this.typeClass = typeClass;
        this.version = version;
        this.schemas = schemas;
        int length = 0;
        for (BasicField schema : schemas) {
            if (field != null)
                length += schema.length() > 0 ? schema.length() : 32;
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
        int i = 0;
        try {
            for (; i < schemas.length; i++)
                schemas[i].readAndSet(input, result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + schemas[i].field.desc(), e);
        }
    }

    public T mergeFrom(ByteBuf input, T result, Explain explain) {
        int i = 0;
        try {
            if (explain == null)
                for (; i < schemas.length; i++)
                    schemas[i].readAndSet(input, result);
            else
                for (; i < schemas.length; i++)
                    schemas[i].readAndSet(input, result, explain);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + schemas[i].field.desc(), e);
        }
    }

    public T readFrom(ByteBuf input) {
        int i = 0;
        try {
            T result = constructor.newInstance((Object[]) null);
            for (; i < schemas.length; i++)
                schemas[i].readAndSet(input, result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + schemas[i].f.getName(), e);
        }
    }

    public T readFrom(ByteBuf input, Explain explain) {
        int i = 0;
        try {
            T result = constructor.newInstance((Object[]) null);
            if (explain == null)
                for (; i < schemas.length; i++)
                    schemas[i].readAndSet(input, result);
            else
                for (; i < schemas.length; i++)
                    schemas[i].readAndSet(input, result, explain);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + schemas[i].f.getName(), e);
        }
    }

    @Override
    public void writeTo(ByteBuf output, T message) {
        int i = 0;
        try {
            for (; i < schemas.length; i++)
                schemas[i].getAndWrite(output, message);
        } catch (Exception e) {
            throw new RuntimeException("Write failed " + i + " " + typeClass.getName() + " " + schemas[i].f.getName(), e);
        }
    }

    @Override
    public void writeTo(ByteBuf output, T message, Explain explain) {
        int i = 0;
        try {
            if (explain == null)
                for (; i < schemas.length; i++)
                    schemas[i].getAndWrite(output, message);
            else
                for (; i < schemas.length; i++)
                    schemas[i].getAndWrite(output, message, explain);
        } catch (Exception e) {
            throw new RuntimeException("Write failed " + i + " " + typeClass.getName() + " " + schemas[i].f.getName(), e);
        }
    }

    public int length() {
        return length;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(48);
        sb.append("{typeClass=").append(typeClass.getSimpleName());
        sb.append(", version=").append(version);
        sb.append(", length=").append(length);
        sb.append('}');
        return sb.toString();
    }
}