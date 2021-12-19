package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 指定前置数量的数组域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class TotalArrayObjectField<T> extends BasicField<T[]> {

    protected final IntTool intTool;
    private Schema<T> schema;

    public TotalArrayObjectField(Schema<T> schema, Field field) {
        this.schema = schema;
        this.intTool = IntTool.getInstance(field.totalUnit());
    }

    public T[] readFrom(ByteBuf input) {
        int total = intTool.read(input);
        if (total <= 0)
            return null;
        T[] value = (T[]) Array.newInstance(schema.getClass(), total);
        for (int i = 0; i < total; i++) {
            T t = schema.readFrom(input);
            value[i] = t;
        }
        return value;
    }

    public void writeTo(ByteBuf output, T[] value) {
        if (value == null) {
            intTool.write(output, 0);
        } else {
            int length = value.length;
            intTool.write(output, length);
            for (int i = 0; i < length; i++) {
                T t = value[i];
                schema.writeTo(output, t);
            }
        }
    }
}