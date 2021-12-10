package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.github.yezhihao.protostar.util.IntTool;
import io.github.yezhihao.protostar.util.StrUtils;
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
        Collection value = new ArrayList<>(total);
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
        int before = input.readerIndex();
        int total = intTool.read(input);
        String hex = StrUtils.leftPad(Integer.toHexString(total), lengthSize << 1, '0');
        explain.add(Info.lengthField(before, field, hex, total));

        if (total <= 0)
            return null;
        Collection value = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            Object t = ((RuntimeSchema) schema).readFrom(input, explain);
            value.add(t);
        }
        return value;
    }

    @Override
    public void writeTo(ByteBuf output, Object value, Explain explain) {
        int before = output.readerIndex();
        int total = value == null ? 0 : ((Collection) value).size();
        String hex = StrUtils.leftPad(Integer.toHexString(total), lengthSize << 1, '0');
        explain.add(Info.lengthField(before, field, hex, total));

        this.writeTo(output, value);
    }

    @Override
    public int compareTo(BasicField that) {
        int r = super.compareTo(that);
        if (r == 0)
            r = (that instanceof ArrayTotalField) ? 1 : -1;
        return r;
    }
}