package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.netty.buffer.ByteBuf;

/**
 * 指定长度域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class LengthField<T> extends BasicField<T> {

    public LengthField(Field field, java.lang.reflect.Field f, Schema<T> schema) {
        super(field, f, schema);
    }

    public T readFrom(ByteBuf input) {
        return schema.readFrom(input, length);
    }

    public void writeTo(ByteBuf output, T value) {
        if (value != null)
            schema.writeTo(output, length, value);
    }
}