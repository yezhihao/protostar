package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.schema.MapSchema;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;
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
public class TotalMapField<K, V> extends BasicField<Map<K, V>> {

    private static final Logger log = LoggerFactory.getLogger(TotalMapField.class.getSimpleName());

    private Schema<K> schema;
    private final Map<K, Schema<V>> valueSchema;
    private final int lengthUnit;
    private final IntTool valueIntTool;
    private final boolean treeMap;
    private final int totalUnit;
    private final IntTool totalIntTool;

    public TotalMapField(Schema schema, Class typeClass, int totalUnit) {
        MapSchema mapSchema = (MapSchema) schema;
        this.valueSchema = mapSchema.valueSchema;
        this.lengthUnit = mapSchema.lengthUnit;
        this.valueIntTool = mapSchema.intTool;
        this.schema = ((MapSchema) schema).keySchema;
        this.treeMap = !HashMap.class.isAssignableFrom(typeClass);
        this.totalUnit = totalUnit;
        this.totalIntTool = IntTool.getInstance(totalUnit);
    }

    @Override
    public Map<K, V> readFrom(ByteBuf input) {
        if (!input.isReadable())
            return null;
        int total = totalIntTool.read(input);
        if (total <= 0)
            return null;

        Map map;
        if (treeMap) map = new TreeMap();
        else map = new HashMap((int) (total / 0.75) + 1);

        K key = null;
        int length = 0;
        try {
            for (int i = 0; i < total; i++) {
                key = schema.readFrom(input);

                length = valueIntTool.read(input);
                if (length <= 0)
                    continue;

                int writerIndex = input.writerIndex();
                int readerIndex = input.readerIndex() + length;
                if (writerIndex > readerIndex) {
                    input.writerIndex(readerIndex);
                    Object value = readValue(key, input);
                    map.put(key, value);
                    input.setIndex(readerIndex, writerIndex);
                } else {
                    Object value = readValue(key, input);
                    map.put(key, value);
                    break;
                }
            }
        } catch (Exception e) {
            log.warn("解析出错:KEY[{}], LENGTH[{}], {}", key, length, e.getMessage());
        }
        return map;
    }

    public Object readValue(Object key, ByteBuf input) {
        Schema schema = valueSchema.get(key);
        if (schema != null) {
            return schema.readFrom(input);
        }
        byte[] bytes = new byte[input.readableBytes()];
        input.readBytes(bytes);
        return bytes;
    }

    @Override
    public void writeTo(ByteBuf output, Map<K, V> map) {
        if (map == null)
            return;
        totalIntTool.write(output, map.size());

        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            schema.writeTo(output, key);

            V value = entry.getValue();
            Schema<V> schema = valueSchema.get(key);
            if (schema != null) {
                int begin = output.writerIndex();
                valueIntTool.write(output, 0);
                schema.writeTo(output, value);
                int length = output.writerIndex() - begin - lengthUnit;
                valueIntTool.set(output, begin, length);
            } else {
                log.warn("未注册的信息:ID[{}], VALUE[{}]", key, value);
            }
        }
    }

    @Override
    public Map<K, V> readFrom(ByteBuf input, Explain explain) {
        if (!input.isReadable())
            return null;
        int total = totalIntTool.read(input);
        explain.lengthField(input.readerIndex() - totalUnit, desc + "数量", total, totalUnit);
        if (total <= 0)
            return null;

        Map map;
        if (treeMap) map = new TreeMap();
        else map = new HashMap((int) (total / 0.75) + 1);

        K key = null;
        int length = 0;
        try {
            for (int i = 0; i < total; i++) {
                key = schema.readFrom(input, explain);
                explain.setLastDesc("key");

                length = valueIntTool.read(input);
                explain.lengthField(input.readerIndex() - lengthUnit, desc + "数量", length, lengthUnit);
                if (length <= 0)
                    continue;

                int writerIndex = input.writerIndex();
                int readerIndex = input.readerIndex() + length;
                if (writerIndex > readerIndex) {
                    input.writerIndex(readerIndex);
                    Object value = readValue(key, input, explain);
                    map.put(key, value);
                    input.setIndex(readerIndex, writerIndex);
                } else {
                    Object value = readValue(key, input, explain);
                    map.put(key, value);
                    break;
                }
            }
        } catch (Exception e) {
            log.warn("解析出错:KEY[{}], LENGTH[{}], {}", key, length, e.getMessage());
        }
        return map;
    }

    public Object readValue(Object key, ByteBuf input, Explain explain) {
        Schema schema = valueSchema.get(key);
        if (schema != null) {
            Object value = schema.readFrom(input, explain);
            explain.setLastDesc("Value");
            return value;
        }
        byte[] bytes = new byte[input.readableBytes()];
        input.readBytes(bytes);
        return bytes;
    }

    @Override
    public void writeTo(ByteBuf output, Map<K, V> map, Explain explain) {
        if (map == null)
            return;
        totalIntTool.write(output, map.size());

        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            schema.writeTo(output, key, explain);
            explain.setLastDesc("Key");

            V value = entry.getValue();
            Schema<V> schema = valueSchema.get(key);
            if (schema != null) {
                int begin = output.writerIndex();
                valueIntTool.write(output, 0);
                schema.writeTo(output, value, explain);
                explain.setLastDesc("Value");
                int length = output.writerIndex() - begin - lengthUnit;
                valueIntTool.set(output, begin, length);
                explain.lengthFieldPrevious(begin, desc + "长度", length, lengthUnit);
            } else {
                log.warn("未注册的信息:ID[{}], VALUE[{}]", key, value);
            }
        }
    }
}