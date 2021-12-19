package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
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

    protected final Schema<T> schema;
    protected final IntTool intTool;

    public TotalCollectionField(Schema<T> schema, int totalUnit) {
        this.schema = schema;
        this.intTool = IntTool.getInstance(totalUnit);
    }

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
}