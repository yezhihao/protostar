package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;

/**
 * 消息结构
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public interface Schema<T> {

    default T readFrom(ByteBuf input) {
        return readFrom(input, null);
    }

    default T readFrom(ByteBuf input, int length) {
        throw new RuntimeException("不支持长度读取");
    }

    void writeTo(ByteBuf output, T value);

    default void writeTo(ByteBuf output, int length, T value) {
        throw new RuntimeException("不支持长度写入");
    }

    default String desc() {
        return "";
    }

    /** 用于预估内存分配，不需要精确值 */
    default int length() {
        return 32;
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
}