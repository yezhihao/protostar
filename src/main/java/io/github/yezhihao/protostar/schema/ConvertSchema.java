package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.converter.Converter;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义结构转换
 */
public class ConvertSchema<T> implements Schema<T> {

    private static volatile Map<Object, ConvertSchema> cache = new HashMap<>();

    public static Schema getInstance(Class<? extends Converter> clazz) {
        String key = clazz.getName();
        ConvertSchema instance;
        if ((instance = cache.get(key)) == null) {
            synchronized (cache) {
                if ((instance = cache.get(key)) == null) {
                    try {
                        Converter converter = clazz.newInstance();
                        instance = new ConvertSchema(converter);
                        cache.put(key, instance);
                        log.debug("new ConvertSchema({})", clazz);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return instance;
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
        if (length > 0)
            input = input.readSlice(length);
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