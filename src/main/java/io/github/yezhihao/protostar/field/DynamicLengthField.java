package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.util.*;
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
        if (!input.isReadable(length))
            return null;
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
        int before = input.readerIndex();

        int length = intTool.read(input);
        String hex = StrUtils.leftPad(Integer.toHexString(length), lengthSize << 1, '0');
        explain.add(Info.lengthField(before, field, hex, length));

        if (!input.isReadable(length))
            return null;
        T value = ((RuntimeSchema<T>) schema).readFrom(input, length, explain);

        int after = input.readerIndex();
        hex = ByteBufUtil.hexDump(input.slice(before + lengthSize, after - before - lengthSize));
        explain.add(Info.field(before + lengthSize, field, hex, value));
        return value;
    }

    @Override
    public void writeTo(ByteBuf output, T value, Explain explain) {
        int before = output.writerIndex();

        intTool.write(output, 0);
        if (value != null) {
            schema.writeTo(output, value);
            int length = output.writerIndex() - before - lengthSize;
            intTool.set(output, before, length);
        }

        int after = output.writerIndex();

        int length = ByteBufUtils.getInt(output, before, lengthSize);
        String hex = StrUtils.leftPad(Integer.toHexString(length), lengthSize << 1, '0');
        explain.add(Info.lengthField(before, field, hex, length));
        if (value != null) {
            hex = ByteBufUtil.hexDump(output.slice(before + lengthSize, after - before - lengthSize));
            explain.add(Info.field(before + lengthSize, field, hex, value));
        }
    }

    @Override
    public int compareTo(BasicField that) {
        int r = super.compareTo(that);
        if (r == 0)
            r = (that instanceof DynamicLengthField) ? 1 : -1;
        return r;
    }
}