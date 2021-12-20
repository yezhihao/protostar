package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 指定前置数量的集合域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class TotalCollectionField<T> extends BasicField<Collection<T>> {

    private final Schema<T> schema;
    private final int totalUnit;
    private final IntTool intTool;

    public TotalCollectionField(Schema<T> schema, int totalUnit) {
        this.schema = schema;
        this.totalUnit = totalUnit;
        this.intTool = IntTool.getInstance(totalUnit);
    }

    @Override
    public Collection<T> readFrom(ByteBuf input) {
        int total = intTool.read(input);
        if (total <= 0)
            return null;
        ArrayList<T> list = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            T t = schema.readFrom(input);
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list) {
        if (list != null) {
            intTool.write(output, list.size());
            for (T t : list) {
                schema.writeTo(output, t);
            }
        } else {
            intTool.write(output, 0);
        }
    }

    @Override
    public Collection<T> readFrom(ByteBuf input, Explain explain) {
        int total = intTool.read(input);
        explain.lengthField(input.readerIndex() - totalUnit, desc + "数量", total, totalUnit);
        if (total <= 0)
            return null;
        ArrayList<T> list = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            T t = schema.readFrom(input, explain);
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list, Explain explain) {
        if (list != null) {
            int total = list.size();
            explain.lengthField(output.writerIndex(), desc + "数量", total, totalUnit);
            intTool.write(output, total);
            for (T t : list) {
                schema.writeTo(output, t, explain);
            }
        } else {
            explain.lengthField(output.writerIndex(), desc + "数量", 0, totalUnit);
            intTool.write(output, 0);
        }
    }
}