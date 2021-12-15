package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 具有每个元素长度单位的集合域，只能位于消息末尾
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class LengthUnitCollectionField extends BasicField {

    private final IntTool intTool;
    private final int lengthUnit;

    public LengthUnitCollectionField(Field field, java.lang.reflect.Field f, Schema schema) {
        super(field, f, schema);
        this.lengthUnit = field.lengthUnit();
        this.intTool = IntTool.getInstance(lengthUnit);
    }

    public Object readFrom(ByteBuf input) {
        Collection value = new ArrayList<>();
        while (input.isReadable()) {
            int length = intTool.read(input);
            Object t = schema.readFrom(input, length);
            value.add(t);
        }
        return value;
    }

    public void writeTo(ByteBuf output, Object value) {
        if (value != null) {
            Collection list = (Collection) value;
            for (Object t : list) {
                if (t != null) {
                    int begin = output.writerIndex();
                    intTool.write(output, 0);
                    schema.writeTo(output, t);
                    int length = output.writerIndex() - begin - lengthUnit;
                    intTool.set(output, begin, length);
                }
            }
        }
    }

    @Override
    public Object readFrom(ByteBuf input, Explain explain) {
        Collection value = new ArrayList<>();
        while (input.isReadable()) {
            int length = intTool.read(input);
            Object t = schema.readFrom(input, length, explain);
            value.add(t);
        }
        return value;
    }

    @Override
    public void writeTo(ByteBuf output, Object value, Explain explain) {
        if (value != null) {
            Collection list = (Collection) value;
            for (Object t : list) {
                if (t != null) {
                    int begin = output.writerIndex();
                    intTool.write(output, 0);
                    schema.writeTo(output, t, explain);
                    int length = output.writerIndex() - begin - lengthUnit;
                    intTool.set(output, begin, length);
                }
            }
        }
    }
}