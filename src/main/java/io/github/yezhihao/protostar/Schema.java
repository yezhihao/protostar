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

    void writeTo(ByteBuf output, T value);

    default T readFrom(ByteBuf input, int length) {
        int readerLength = input.readerIndex() + length;
        int writerIndex = input.writerIndex();
        input.writerIndex(readerLength);
        T value = readFrom(input);
        input.setIndex(readerLength, writerIndex);
        return value;
    }

    default void writeTo(ByteBuf output, int length, T value) {
        int writerLength = output.writerIndex() + length;
        writeTo(output, value);
        output.writerIndex(writerLength);
    }

    default T readFrom(ByteBuf input, Explain explain) {
        int begin = input.readerIndex();
        T value = readFrom(input);
        explain.readField(begin, desc(), value, input);
        return value;
    }

    default void writeTo(ByteBuf output, T value, Explain explain) {
        int begin = output.writerIndex();
        writeTo(output, value);
        explain.writeField(begin, desc(), value, output);
    }

    default T readFrom(ByteBuf input, int length, Explain explain) {
        int readerLength = input.readerIndex() + length;
        int writerIndex = input.writerIndex();
        input.writerIndex(readerLength);
        T value = readFrom(input, explain);
        input.setIndex(readerLength, writerIndex);
        return value;
    }

    default void writeTo(ByteBuf output, int length, T value, Explain explain) {
        int writerLength = output.writerIndex() + length;
        writeTo(output, value, explain);
        output.writerIndex(writerLength);
    }

    /** 用于预估内存分配，不需要精确值 */
    default int length() {
        return 32;
    }

    default String desc() {
        return "";
    }
}