package io.github.yezhihao.protostar.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 编解码分析
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class Explain {

    private List<Info> list;

    public Explain() {
        this.list = new LinkedList<>();
    }

    public void add(Info info) {
        list.add(info);
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
}
