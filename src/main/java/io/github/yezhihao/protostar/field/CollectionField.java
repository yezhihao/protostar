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
        Collection list = new ArrayList<>();
        while (input.isReadable()) {
            T t = schema.readFrom(input);
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
        Collection list = new ArrayList<>();
        while (input.isReadable()) {
            T t = schema.readFrom(input, explain);
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