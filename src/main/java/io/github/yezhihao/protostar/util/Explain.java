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
        this.add(Info.field(index, desc, value, ByteBufUtil.hexDump(input, index, input.readerIndex() - index)));
    }

    public void writeField(int index, String desc, Object value, ByteBuf output) {
        this.add(Info.field(index, desc, value, ByteBufUtil.hexDump(output, index, output.writerIndex() - index)));
    }

    public void lengthField(int index, String desc, int value, int lengthUnit) {
        this.add(Info.lengthField(index, desc, value, lengthUnit));
    }

    public void lengthFieldPrevious(int index, String desc, int value, int lengthUnit) {
        this.add(this.size() - 1, Info.lengthField(index, desc, value, lengthUnit));
    }

    public void setLastDesc(String desc) {
        this.get(this.size() - 1).desc = desc;
    }

    public void println() {
        for (Info info : this) {
            Object value = info.getValue();
            if (value != null)
                System.out.println(info.getIndex() + "\t" + "[" + info.getRaw() + "] > [" + StrUtils.toString(value) + "] " + info.getDesc());
        }
    }
}