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
 * 指定前置数量的集合域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class TotalCollectionField extends BasicField {

    protected final IntTool intTool;

    public TotalCollectionField(Field field, java.lang.reflect.Field f, Schema schema) {
        super(field, f, schema);
        this.intTool = IntTool.getInstance(field.totalUnit());
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
        if (value != null) {
            Collection list = (Collection) value;
            intTool.write(output, list.size());
            for (Object t : list) {
                schema.writeTo(output, t);
            }
        } else {
            intTool.write(output, 0);
        }
    }

    @Override
    public Object readFrom(ByteBuf input, Explain explain) {
        int begin = input.readerIndex();
        int total = intTool.read(input);
        explain.add(Info.lengthField(begin, desc, total));

        if (total <= 0)
            return null;
        ArrayList value = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            Object t = schema.readFrom(input, explain);
            value.add(t);
        }
        return value;
    }
}