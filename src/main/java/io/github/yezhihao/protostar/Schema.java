package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;

/**
 * 消息结构
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public interface Schema<T> {

    T readFrom(ByteBuf input);

    default T readFrom(ByteBuf input, int length) {
        return readFrom(input);
    }

    default T readFrom(ByteBuf input, Explain explain) {
        return readFrom(input);
    }

    default T readFrom(ByteBuf input, int length, Explain explain) {
        return readFrom(input, length);
    }

    void writeTo(ByteBuf output, T value);

    default void writeTo(ByteBuf output, int length, T value) {
        writeTo(output, value);
    }

    default void writeTo(ByteBuf output, T value, Explain explain) {
        writeTo(output, value);
    }

    /** 用于预估内存分配，不需要精确值 */
    default int length() {
        return 32;
    }
}