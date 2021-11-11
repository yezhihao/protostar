package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Cache;
import io.netty.buffer.ByteBuf;

public class ObjectSchema<T> implements Schema<T> {

    private static final Cache<Schema, ObjectSchema> cache = new Cache<>();

    public static ObjectSchema getInstance(Schema schema) {
        return cache.get(schema, ObjectSchema::new);
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
        if (length > 0) {
            int writerIndex = input.writerIndex();
            input.writerIndex(input.readerIndex() + length);
            T result = schema.readFrom(input);
            input.writerIndex(writerIndex);
            return result;
        }
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