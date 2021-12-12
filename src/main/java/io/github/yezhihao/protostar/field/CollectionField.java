package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 数组域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class CollectionField extends BasicField {

    public CollectionField(Field field, java.lang.reflect.Field f, Schema schema) {
        super(field, f, schema);
    }

    public Object readFrom(ByteBuf input) {
        Collection value = new ArrayList<>();
        while (input.isReadable()) {
            Object t = schema.readFrom(input);
            value.add(t);
        }
        return value;
    }

    public void writeTo(ByteBuf output, Object value) {
        Collection list = (Collection) value;
        if (list != null && !list.isEmpty()) {
            for (Object t : list) {
                schema.writeTo(output, t);
            }
        }
    }

    @Override
    public Object readFrom(ByteBuf input, Explain explain) {
        Collection value = new ArrayList<>();
        while (input.isReadable()) {
            Object t = schema.readFrom(input, explain);
            value.add(t);
        }
        return value;
    }

    @Override
    public void writeTo(ByteBuf output, Object value, Explain explain) {
        Collection list = (Collection) value;
        if (list != null && !list.isEmpty()) {
            for (Object t : list) {
                schema.writeTo(output, t, explain);
            }
        }
    }
}