package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 消息结构
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public interface Schema<T> {

    T readFrom(ByteBuf input);

    default T readFrom(ByteBuf input, int length) {
        return readFrom(input);
//        throw new RuntimeException("不支持长度读取");
    }

    void writeTo(ByteBuf output, T value);

    default void writeTo(ByteBuf output, int length, T value) {
        writeTo(output, value);
//        throw new RuntimeException("不支持长度写入");
    }

    default void writeTo(ByteBuf output, T value, Explain explain) {
        int begin = output.writerIndex();

        writeTo(output, value);

        int end = output.writerIndex();
        String raw = ByteBufUtil.hexDump(output, begin, end - begin);
        explain.add(Info.field(begin, desc(), value, raw));
    }

    default T readFrom(ByteBuf input, Explain explain) {
        int begin = input.readerIndex();

        T value = readFrom(input);

        int end = input.readerIndex();
        String raw = ByteBufUtil.hexDump(input, begin, end - begin);
        explain.add(Info.field(begin, desc(), value, raw));
        return value;
    }

    default T readFrom(ByteBuf input, int length, Explain explain) {
        int begin = input.readerIndex();

        T value = readFrom(input, length);

        int end = input.readerIndex();
        String raw = ByteBufUtil.hexDump(input, begin, end - begin);
        explain.add(Info.field(begin, desc(), value, raw));
        return value;
    }

    default String desc() {
        return "";
    }

    /** 用于预估内存分配，不需要精确值 */
    default int length() {
        return 32;
    }
}