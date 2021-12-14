package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 无界字典域，只能位于消息末尾
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class TotalMapField extends BasicField {

    public final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    protected final IntTool intTool;

    private final boolean treeMap=true;

    public TotalMapField(Field field, java.lang.reflect.Field f, Schema schema) {
        super(field, f, schema);
        this.intTool = IntTool.getInstance(field.totalUnit());
//        this.treeMap = TreeMap.class.isAssignableFrom(f.getType());
    }

    public Object readFrom(ByteBuf input) {
        int total = intTool.read(input);
        if (total <= 0)
            return null;

        Map map;
        if (treeMap) map = new TreeMap();
        else map = new HashMap((int) (total / 0.75) + 1);

        Map.Entry entry;
        try {
            for (int i = 0; i < total; i++) {
                entry = (Map.Entry) schema.readFrom(input);
                map.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            log.warn("解析出错:{}", ByteBufUtil.hexDump(input, input.readerIndex(), input.writerIndex()));
        }
        return map;
    }

    public void writeTo(ByteBuf output, Object t) {
        if (t != null) {
            Map<Object, Object> map = (Map) t;
            intTool.write(output, map.size());
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                schema.writeTo(output, entry);
            }
        } else {
            intTool.write(output, 0);
        }
    }

    @Override
    public Object readFrom(ByteBuf input, Explain explain) {
        int begin = input.readerIndex();
        int total = intTool.read(input);
        explain.add(Info.lengthField(begin, desc, total));

        if (total <= 0)
            return null;

        Map map = new HashMap((int) (total / 0.75) + 1);
        Map.Entry entry;
        try {
            for (int i = 0; i < total; i++) {
                entry = (Map.Entry) schema.readFrom(input, explain);
                map.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            log.warn("解析出错:{}", ByteBufUtil.hexDump(input, input.readerIndex(), input.writerIndex()));
        }
        return map;
    }
}