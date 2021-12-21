package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.schema.MapSchema;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
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
public class MapField<K, V> extends BasicField<Map<K, V>> {

    private static final Logger log = LoggerFactory.getLogger(MapField.class.getSimpleName());

    private Schema<K> schema;
    private final Map<K, Schema<V>> valueSchema;
    private final int lengthUnit;
    private final IntTool valueIntTool;
    private final boolean treeMap;

    public MapField(Schema schema, Class typeClass) {
        MapSchema mapSchema = (MapSchema) schema;
        this.valueSchema = mapSchema.valueSchema;
        this.lengthUnit = mapSchema.lengthUnit;
        this.valueIntTool = mapSchema.intTool;
        this.schema = ((MapSchema) schema).keySchema;
        this.treeMap = !HashMap.class.isAssignableFrom(typeClass);
    }

    @Override
    public Map<K, V> readFrom(ByteBuf input) {
        if (!input.isReadable())
            return null;

        Map map;
        if (treeMap) map = new TreeMap();
        else map = new HashMap(8);

        K key = null;
        int length = 0;
        try {
            do {
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
            } while (input.isReadable());
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

        Map map;
        if (treeMap) map = new TreeMap();
        else map = new HashMap(8);

        K key = null;
        int length = 0;
        try {
            do {
                key = schema.readFrom(input, explain);
                explain.setLastDesc(desc + "ID");

                length = valueIntTool.read(input);
                explain.lengthField(input.readerIndex() - lengthUnit, desc + "长度", length, lengthUnit);
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
            } while (input.isReadable());
        } catch (Exception e) {
            log.warn("解析出错:KEY[{}], LENGTH[{}], {}", key, length, e.getMessage());
        }
        return map;
    }

    public Object readValue(Object key, ByteBuf input, Explain explain) {
        Schema schema = valueSchema.get(key);
        if (schema != null) {
            Object value = schema.readFrom(input, explain);
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

        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            schema.writeTo(output, key, explain);
            explain.setLastDesc(desc + "ID");

            V value = entry.getValue();
            Schema<V> schema = valueSchema.get(key);
            if (schema != null) {
                int begin = output.writerIndex();
                Info info = explain.lengthField(begin, desc + "长度", 0, lengthUnit);
                valueIntTool.write(output, 0);
                schema.writeTo(output, value, explain);
                int length = output.writerIndex() - begin - lengthUnit;
                valueIntTool.set(output, begin, length);
                info.setLength(length, lengthUnit);
            } else {
                log.warn("未注册的信息:ID[{}], VALUE[{}]", key, value);
            }
        }
    }
}