package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class ObjectSchema<T> implements Schema<T> {

    private static volatile Map<Object, ObjectSchema> cache = new HashMap<>();

    public static Schema getInstance(Schema schema) {
        Object key = schema;
        ObjectSchema instance;
        if ((instance = cache.get(key)) == null) {
            synchronized (cache) {
                if ((instance = cache.get(key)) == null) {
                    instance = new ObjectSchema(schema);
                    cache.put(schema, instance);
                    log.debug("new ObjectSchema({})", schema);
                }
            }
        }
        return instance;
    }

    private final Schema<T> schema;

    private ObjectSchema(Schema<T> schema) {
        this.schema = schema;
    }

    @Override
    public T readFrom(ByteBuf input) {
        return schema.readFrom(input);
    }

    @Override
    public T readFrom(ByteBuf input, int length) {
        if (length > 0)
            input = input.readSlice(length);
        return schema.readFrom(input);
    }

    @Override
    public void writeTo(ByteBuf output, T message) {
        schema.writeTo(output, message);
    }

    @Override
    public void writeTo(ByteBuf output, int length, T obj) {
        schema.writeTo(output, obj);
    }
}