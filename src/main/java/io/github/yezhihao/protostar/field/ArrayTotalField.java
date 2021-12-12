package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 总数数组域
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
        ArrayList value = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            Object t = schema.readFrom(input);
            value.add(t);
        }
        return value;
    }

    public void writeTo(ByteBuf output, Object value) {
        Collection list = (Collection) value;
        if (list == null || list.isEmpty()) {
            intTool.write(output, 0);
        } else {
            intTool.write(output, list.size());
            for (Object t : list) {
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
        ArrayList value = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            Object t = schema.readFrom(input, explain);
            value.add(t);
        }
        return value;
    }

    @Override
    public void writeTo(ByteBuf output, Object value, Explain explain) {
        int begin = output.readerIndex();
        Collection list = (Collection) value;
        int total = list == null ? 0 : list.size();
        explain.add(Info.lengthField(begin, field, total));

        if (list != null) {
            for (Object t : list) {
                schema.writeTo(output, t, explain);
            }
        }
    }
}