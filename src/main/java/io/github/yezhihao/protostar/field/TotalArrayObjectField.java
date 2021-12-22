package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 指定前置数量的数组域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class TotalArrayObjectField<T> extends BasicField<T[]> {

    private final Schema<T> schema;
    private final int totalUnit;
    private final IntTool intTool;
    private final Class<T> arrayClass;

    public TotalArrayObjectField(Schema<T> schema, int totalUnit, Class<T> arrayClass) {
        this.schema = schema;
        this.totalUnit = totalUnit;
        this.intTool = IntTool.getInstance(totalUnit);
        this.arrayClass = arrayClass;
    }

    @Override
    public T[] readFrom(ByteBuf input) {
        int total = intTool.read(input);
        if (total <= 0)
            return null;
        T[] value = (T[]) Array.newInstance(arrayClass, total);
        for (int i = 0; i < total; i++) {
            T t = schema.readFrom(input);
            value[i] = t;
        }
        return value;
    }

    @Override
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

    @Override
    public T[] readFrom(ByteBuf input, Explain explain) {
        int total = intTool.read(input);
        explain.lengthField(input.readerIndex() - totalUnit, desc + "数量", total, totalUnit);
        if (total <= 0)
            return null;
        T[] value = (T[]) Array.newInstance(arrayClass, total);
        for (int i = 0; i < total; i++) {
            T t = schema.readFrom(input, explain);
            value[i] = t;
        }
        return value;
    }

    @Override
    public void writeTo(ByteBuf output, T[] value, Explain explain) {
        if (value == null) {
            explain.lengthField(output.writerIndex(), desc + "数量", 0, totalUnit);
            intTool.write(output, 0);
        } else {
            int total = value.length;
            explain.lengthField(output.writerIndex(), desc + "数量", total, totalUnit);
            intTool.write(output, total);
            for (int i = 0; i < total; i++) {
                T t = value[i];
                schema.writeTo(output, t, explain);
            }
        }
    }
}