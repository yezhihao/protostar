package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 动态长度的字段
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class DynamicLengthField<T> extends BasicField<T> {

    private final IntTool intTool;

    public DynamicLengthField(Field field, java.lang.reflect.Field f, Schema<T> schema) {
        super(field, f, schema);
        this.intTool = IntTool.getInstance(field.lengthSize());
    }

    public T readFrom(ByteBuf input) {
        int length = intTool.read(input);
        return schema.readFrom(input, length);
    }

    public void writeTo(ByteBuf output, T value) {
        int begin = output.writerIndex();
        intTool.write(output, 0);
        if (value != null) {
            schema.writeTo(output, value);
            int length = output.writerIndex() - begin - lengthSize;
            intTool.set(output, begin, length);
        }
    }

    @Override
    public T readFrom(ByteBuf input, Explain explain) {
        int begin = input.readerIndex();

        int length = intTool.read(input);
        explain.add(Info.lengthField(begin, field, length));

        T value = schema.readFrom(input, length, explain);

        int end = input.readerIndex();
        String raw = ByteBufUtil.hexDump(input, begin + lengthSize, end - begin - lengthSize);
        explain.add(Info.field(begin + lengthSize, field, value, raw));
        return value;
    }

    @Override
    public void writeTo(ByteBuf output, T value, Explain explain) {
        int begin = output.writerIndex();

        intTool.write(output, 0);
        if (value != null) {
            schema.writeTo(output, value);
            int length = output.writerIndex() - begin - lengthSize;
            intTool.set(output, begin, length);
        }

        int length = intTool.get(output, begin);
        explain.add(Info.lengthField(begin, field, length));

        if (value != null) {
            int end = output.writerIndex();
            String raw = ByteBufUtil.hexDump(output, begin + lengthSize, end - begin - lengthSize);
            explain.add(Info.field(begin + lengthSize, field, value, raw));
        }
    }
}