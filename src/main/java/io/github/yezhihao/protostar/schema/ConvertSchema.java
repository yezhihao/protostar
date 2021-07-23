package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.converter.Converter;
import io.github.yezhihao.protostar.util.Cache;
import io.netty.buffer.ByteBuf;

/**
 * 自定义结构转换
 */
public class ConvertSchema<T> implements Schema<T> {

    private static final Cache<String, ConvertSchema> cache = new Cache<>();

    public static ConvertSchema getInstance(Class<? extends Converter> clazz) {
        return cache.get(clazz.getName(), key -> {
            try {
                return new ConvertSchema(clazz.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private final Converter<T> converter;

    private ConvertSchema(Converter<T> converter) {
        this.converter = converter;
    }

    @Override
    public T readFrom(ByteBuf input) {
        return converter.convert(input);
    }

    @Override
    public T readFrom(ByteBuf input, int length) {
        if (length > 0) {
            int writerIndex = input.writerIndex();
            input.writerIndex(input.readerIndex() + length);
            T result = converter.convert(input);
            input.writerIndex(writerIndex);
            return result;
        }
        return converter.convert(input);
    }

    @Override
    public void writeTo(ByteBuf output, T message) {
        converter.convert(output, message);
    }

    @Override
    public void writeTo(ByteBuf output, int length, T obj) {
        converter.convert(output, obj);
    }
}