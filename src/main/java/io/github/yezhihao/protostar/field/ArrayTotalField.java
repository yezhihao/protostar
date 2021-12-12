package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 前置数量的数组域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class ArrayTotalField extends BasicField {

    protected final IntTool intTool;

    public ArrayTotalField(Field field, java.lang.reflect.Field f, Schema schema) {
        super(field, f, schema);
        this.intTool = IntTool.getInstance(field.lengthSize());
    }

    public Object readFrom(ByteBuf input) {
        int total = intTool.read(input);
        if (total <= 0)
            return null;
        Object array = Array.newInstance(schema.getClass(), total);
        for (int i = 0; i < total; i++) {
            Object t = schema.readFrom(input);
            Array.set(array, i, t);
        }
        return array;
    }

    public void writeTo(ByteBuf output, Object value) {
        if (value == null) {
            intTool.write(output, 0);
        } else {
            int length = Array.getLength(value);
            intTool.write(output, length);
            for (int i = 0; i < length; i++) {
                Object t = Array.get(value, i);
                schema.writeTo(output, t);
            }
        }
    }

    @Override
    public Object readFrom(ByteBuf input, Explain explain) {
        int begin = input.readerIndex();
        int total = intTool.read(input);
        explain.add(Info.lengthField(begin, field, total));

        if (total <= 0)
            return null;
        Object array = Array.newInstance(schema.getClass(), total);
        for (int i = 0; i < total; i++) {
            Object t = schema.readFrom(input, explain);
            Array.set(array, i, t);
        }
        return array;
    }

    @Override
    public void writeTo(ByteBuf output, Object value, Explain explain) {
        int begin = output.readerIndex();
        if (value == null) {
            explain.add(Info.lengthField(begin, field, 0));
            intTool.write(output, 0);
        } else {
            int length = Array.getLength(value);
            explain.add(Info.lengthField(begin, field, length));
            intTool.write(output, length);
            for (int i = 0; i < length; i++) {
                Object t = Array.get(value, i);
                schema.writeTo(output, t, explain);
            }
        }
    }
}