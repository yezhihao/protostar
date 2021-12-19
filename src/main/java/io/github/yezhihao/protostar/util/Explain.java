package io.github.yezhihao.protostar.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * 编解码分析
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class Explain {

    public static final Empty Empty = new Empty();

    private List<Info> list;

    public Explain() {
        this.list = new LinkedList<>();
    }

    public void readField(int index, String desc, Object value, ByteBuf input) {
        list.add(Info.field(index, desc, value, ByteBufUtil.hexDump(input, index, input.readerIndex() - index)));
    }

    public void writeField(int index, String desc, Object value, ByteBuf output) {
        list.add(Info.field(index, desc, value, ByteBufUtil.hexDump(output, index, output.writerIndex() - index)));
    }

    public void lengthField(int index, String desc, int value) {
        list.add(Info.lengthField(index, desc, value));
    }

    public List<Info> getList() {
        return list;
    }

    public void setLastDesc(String desc) {
        list.get(list.size() - 1).desc = desc;
    }

    public void setList(List<Info> list) {
        this.list = list;
    }

    public void println() {
        for (Info info : list) {
            Object value = info.getValue();
            if (value != null)
                System.out.println(info.getIndex() + "\t" + "[" + info.getRaw() + "] " + info.getDesc() + ": " + StrUtils.toString(value));
        }
    }

    private static class Empty extends Explain {
        @Override
        public void readField(int index, String desc, Object value, ByteBuf input) {
        }

        @Override
        public void writeField(int index, String desc, Object value, ByteBuf output) {
        }

        @Override
        public void lengthField(int index, String desc, int value) {
        }

        @Override
        public void setLastDesc(String desc) {
        }
    }
}