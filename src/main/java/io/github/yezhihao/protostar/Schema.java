package io.github.yezhihao.protostar;

import io.netty.buffer.ByteBuf;

/**
 * 消息结构
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public interface Schema<T> {

    T readFrom(ByteBuf input);

    void writeTo(ByteBuf output, T value);

    default T readFrom(ByteBuf input, int length) {
        return readFrom(input);
    }

    default void writeTo(ByteBuf output, int length, T value) {
        writeTo(output, value);
    }

    /** 用于预估内存分配，不需要精确值 */
    default int length() {
        return 128;
    }
}