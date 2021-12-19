package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

/**
 * 指定长度域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class LengthField<T> extends BasicField<T> {

    private Schema<T> schema;
    private int length;

    public LengthField(Schema<T> schema, int length) {
        this.schema = schema;
        this.length = length;
    }

    public T readFrom(ByteBuf input) {
        if (!input.isReadable(length))
            return null;
        return schema.readFrom(input, length);
    }

    public void writeTo(ByteBuf output, T value) {
        if (value != null)
            schema.writeTo(output, length, value);
    }
}