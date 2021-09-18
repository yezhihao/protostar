package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.ByteBufUtils;
import io.github.yezhihao.protostar.util.Cache;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public class CollectionSchema<T> implements Schema<List<T>> {

    private static final Cache<String, CollectionSchema> CACHE = new Cache<>();

    public static CollectionSchema getInstance(Schema schema, int lengthSize) {
        return CACHE.get(schema.hashCode() + "_" + lengthSize, key -> new CollectionSchema(schema, lengthSize));
    }

    private final Schema<T> schema;

    private final int lengthSize;

    private CollectionSchema(Schema<T> schema, int lengthSize) {
        this.schema = schema;
        this.lengthSize = lengthSize;
    }

    @Override
    public List<T> readFrom(ByteBuf input) {
        if (!input.isReadable())
            return null;
        List<T> list;
        if (lengthSize > 0) {
            int length = ByteBufUtils.readInt(input, lengthSize);
            list = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                T obj = schema.readFrom(input);
                if (obj == null) break;
                list.add(obj);
            }
        } else {
            list = new ArrayList<>(2);
            do {
                T obj = schema.readFrom(input);
                if (obj == null) break;
                list.add(obj);
            } while (input.isReadable());
        }
        return list;
    }


    @Override
    public List<T> readFrom(ByteBuf input, int length) {
        int writerIndex = input.writerIndex();
        input.writerIndex(input.readerIndex() + length);
        List<T> result = this.readFrom(input);
        input.writerIndex(writerIndex);
        return result;
    }

    @Override
    public void writeTo(ByteBuf output, List<T> list) {
        if (list == null || list.isEmpty())
            return;
        if (lengthSize > 0)
            ByteBufUtils.writeInt(output, lengthSize, list.size());
        for (T obj : list) {
            schema.writeTo(output, obj);
        }
    }

    @Override
    public void writeTo(ByteBuf output, int length, List<T> list) {
        if (list == null || list.isEmpty())
            return;

        for (T obj : list) {
            schema.writeTo(output, length, obj);
        }
    }
}