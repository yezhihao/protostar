package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;

/**
 * 指定长度域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class LengthField<T> extends BasicField<T> {

    private final Schema<T> schema;
    private final int length;

    public LengthField(Schema<T> schema, int length) {
        this.schema = schema;
        this.length = length;
    }

    @Override
    public T readFrom(ByteBuf input) {
        if (input.isReadable(length))
            return schema.readFrom(input, length);
        return null;
    }

    @Override
    public void writeTo(ByteBuf output, T value) {
        if (value != null)
            schema.writeTo(output, length, value);
    }

    @Override
    public T readFrom(ByteBuf input, Explain explain) {
        if (input.isReadable(length))
            return schema.readFrom(input, length, explain);
        return null;
    }

    @Override
    public void writeTo(ByteBuf output, T value, Explain explain) {
        if (value != null)
            schema.writeTo(output, length, value, explain);
    }
}