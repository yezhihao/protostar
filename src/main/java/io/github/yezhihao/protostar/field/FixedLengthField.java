package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 固定长度的字段
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class FixedLengthField<T> extends BasicField<T> {

    public FixedLengthField(Field field, java.lang.reflect.Field f, Schema<T> schema) {
        super(field, f, schema);
    }

    public T readFrom(ByteBuf input) {
        return schema.readFrom(input, length);
    }

    public void writeTo(ByteBuf output, T value) {
        if (value != null)
            schema.writeTo(output, length, value);
    }

    @Override
    public T readFrom(ByteBuf input, Explain explain) {
        int before = input.readerIndex();

        T value;
        if (schema instanceof RuntimeSchema)
            value = ((RuntimeSchema<T>) schema).readFrom(input, length, explain);
        else
            value = schema.readFrom(input, length);

        int after = input.readerIndex();
        String hex = ByteBufUtil.hexDump(input.slice(before, after - before));
        explain.add(Info.field(before, field, hex, value));
        return value;
    }

    @Override
    public void writeTo(ByteBuf output, T value, Explain explain) {
        int before = output.writerIndex();

        if (value != null) {
            schema.writeTo(output, length, value);

            int after = output.writerIndex();
            String hex = ByteBufUtil.hexDump(output.slice(before, after - before));
            explain.add(Info.field(before, field, hex, value));
        }
    }
}