package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

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

    @Override
    public T readFrom(ByteBuf input, Explain explain) {
        int begin = input.readerIndex();

        T value = schema.readFrom(input, length, explain);

        if (notRs) {
            int end = input.readerIndex();
            String raw = ByteBufUtil.hexDump(input, begin, end - begin);
            explain.add(Info.field(begin, field, value, raw));
        }
        return value;
    }

    @Override
    public void writeTo(ByteBuf output, T value, Explain explain) {
        int begin = output.writerIndex();

        if (value != null) {
            schema.writeTo(output, length, value);
            if (notRs) {
                int end = output.writerIndex();
                String raw = ByteBufUtil.hexDump(output, begin, end - begin);
                explain.add(Info.field(begin, field, value, raw));
            }
        }
    }
}