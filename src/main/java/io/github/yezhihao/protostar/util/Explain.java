package io.github.yezhihao.protostar.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.util.LinkedList;

/**
 * 编解码分析
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class Explain extends LinkedList<Info> {

    public void readField(int index, String desc, Object value, ByteBuf input) {
        if (value != null)
            this.add(Info.field(index, desc, value, ByteBufUtil.hexDump(input, index, input.readerIndex() - index)));
    }

    public void writeField(int index, String desc, Object value, ByteBuf output) {
        if (value != null)
            this.add(Info.field(index, desc, value, ByteBufUtil.hexDump(output, index, output.writerIndex() - index)));
    }

    public Info lengthField(int index, String desc, int length, int lengthUnit) {
        Info info = Info.lengthField(index, desc, length, lengthUnit);
        this.add(info);
        return info;
    }

    public void setLastDesc(String desc) {
        this.get(this.size() - 1).desc = desc;
    }

    public void println() {
        for (Info info : this)
            System.out.println(info);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.size() << 5);
        for (Info info : this)
            sb.append(info).append('\n');
        return sb.toString();
    }
}