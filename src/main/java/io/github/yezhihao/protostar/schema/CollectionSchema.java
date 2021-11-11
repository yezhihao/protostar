package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.ByteBufUtils;
import io.github.yezhihao.protostar.util.Cache;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionSchema<T> implements Schema<Collection<T>> {

    private static final Cache<Schema, CollectionSchema> CACHE = new Cache<>();

    public static CollectionSchema getInstance(Schema schema) {
        return CACHE.get(schema, CollectionSchema::new);
    }

    private final Schema<T> schema;

    private CollectionSchema(Schema<T> schema) {
        this.schema = schema;
    }

    @Override
    public Collection<T> readFrom(ByteBuf input) {
        if (!input.isReadable())
            return null;
        Collection<T> list = new ArrayList<>(2);
        do {
            T obj = schema.readFrom(input);
            if (obj == null) break;
            list.add(obj);
        } while (input.isReadable());
        return list;
    }

    @Override
    public Collection<T> readFrom(ByteBuf input, int totalSize) {
        int total = ByteBufUtils.readInt(input, totalSize);
        if (total <= 0)
            return null;
        Collection<T> list = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            T obj = schema.readFrom(input);
            list.add(obj);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list) {
        if (list == null || list.isEmpty())
            return;

        for (T obj : list) {
            schema.writeTo(output, obj);
        }
    }

    @Override
    public void writeTo(ByteBuf output, int totalSize, Collection<T> list) {
        if (list == null || list.isEmpty()) {
            ByteBufUtils.writeInt(output, totalSize, 0);
            return;
        }

        ByteBufUtils.writeInt(output, totalSize, list.size());
        for (T obj : list) {
            schema.writeTo(output, totalSize, obj);
        }
    }
}