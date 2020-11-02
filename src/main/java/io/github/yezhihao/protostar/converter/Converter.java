package io.github.yezhihao.protostar.converter;

import io.netty.buffer.ByteBuf;

public interface Converter<T> {

    T convert(ByteBuf input);

    void convert(ByteBuf output, T value);
}