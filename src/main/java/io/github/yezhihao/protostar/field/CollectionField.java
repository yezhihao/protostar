package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 无界集合域，只能位于消息末尾
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class CollectionField<T> extends BasicField<Collection<T>> {

    private final Schema<T> schema;

    public CollectionField(Schema<T> schema) {
        this.schema = schema;
    }

    @Override
    public Collection<T> readFrom(ByteBuf input) {
        Collection<T> list = new ArrayList<>();
        T t;
        while (input.isReadable() && (t = schema.readFrom(input)) != null) {
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list) {
        if (list != null) {
            for (T t : list) {
                schema.writeTo(output, t);
            }
        }
    }

    @Override
    public Collection<T> readFrom(ByteBuf input, Explain explain) {
        Collection<T> list = new ArrayList<>();
        T t;
        while (input.isReadable() && (t = schema.readFrom(input, explain)) != null) {
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list, Explain explain) {
        if (list != null) {
            for (T t : list) {
                schema.writeTo(output, t, explain);
            }
        }
    }
}