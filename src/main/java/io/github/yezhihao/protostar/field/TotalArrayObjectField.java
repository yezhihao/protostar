package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 指定前置数量的数组域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class TotalArrayObjectField extends BasicField {

    protected final IntTool intTool;

    public TotalArrayObjectField(Field field, java.lang.reflect.Field f, Schema schema) {
        super(field, f, schema);
        this.intTool = IntTool.getInstance(field.totalUnit());
    }

    public Object readFrom(ByteBuf input) {
        int total = intTool.read(input);
        if (total <= 0)
            return null;
        Object value = Array.newInstance(schema.getClass(), total);
        for (int i = 0; i < total; i++) {
            Object t = schema.readFrom(input);
            Array.set(value, i, t);
        }
        return value;
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
        explain.add(Info.lengthField(begin, desc, total));

        if (total <= 0)
            return null;
        Object value = Array.newInstance(schema.getClass(), total);
        for (int i = 0; i < total; i++) {
            Object t = schema.readFrom(input, explain);
            Array.set(value, i, t);
        }
        return value;
    }
}