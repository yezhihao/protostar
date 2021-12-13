package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.PrepareLoadStrategy;
import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public abstract class MapSchema<K, V> extends PrepareLoadStrategy implements Schema<Map<K, V>> {

    protected final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    protected final Schema<K> keySchema;
    protected final int lengthSize;
    protected final IntTool intTool;

    public MapSchema(Schema<K> keySchema, int lengthSize) {
        this.keySchema = keySchema;
        this.lengthSize = lengthSize;
        this.intTool = IntTool.getInstance(lengthSize);
    }

    @Override
    public Map<K, V> readFrom(ByteBuf input) {
        if (!input.isReadable())
            return null;
        Map<K, V> map = new TreeMap<>();
        K key = null;
        int length = 0;
        try {
            do {
                key = keySchema.readFrom(input);
                length = intTool.read(input);
                if (length <= 0)
                    continue;

                if (input.isReadable(length)) {
                    int writerIndex = input.writerIndex();
                    input.writerIndex(input.readerIndex() + length);
                    map.put(key, (V) readValue(key, input));
                    input.writerIndex(writerIndex);
                } else {
                    map.put(key, (V) readValue(key, input));
                }
            } while (input.isReadable());
        } catch (Exception e) {
            log.warn("解析出错:KEY[{}], LENGTH[{}]", key, length);
        }
        return map;
    }

    public Object readValue(K key, ByteBuf input) {
        if (!input.isReadable())
            return null;
        Schema schema = getSchema(key);
        if (schema != null)
            return schema.readFrom(input);
        byte[] bytes = new byte[input.readableBytes()];
        input.readBytes(bytes);
        return bytes;
    }

    @Override
    public Map<K, V> readFrom(ByteBuf input, Explain explain) {
        if (!input.isReadable())
            return null;
        Map<K, V> map = new TreeMap<>();
        K key = null;
        int length = 0;
        try {
            do {
                key = keySchema.readFrom(input, explain);
                length = intTool.read(input);
                if (length <= 0)
                    continue;

                if (input.isReadable(length)) {
                    int writerIndex = input.writerIndex();
                    input.writerIndex(input.readerIndex() + length);
                    map.put(key, (V) readValue(key, input, explain));
                    input.writerIndex(writerIndex);
                } else {
                    map.put(key, (V) readValue(key, input, explain));
                }
            } while (input.isReadable());
        } catch (Exception e) {
            log.warn("解析出错:KEY[{}], LENGTH[{}]", key, length);
        }
        return map;
    }

    public Object readValue(K key, ByteBuf input, Explain explain) {
        if (!input.isReadable())
            return null;
        Schema schema = getSchema(key);
        if (schema != null)
            return schema.readFrom(input, explain);
        byte[] bytes = new byte[input.readableBytes()];
        input.readBytes(bytes);
        return bytes;
    }

    @Override
    public void writeTo(ByteBuf output, Map<K, V> map) {

        if (map == null || map.isEmpty())
            return;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            keySchema.writeTo(output, key);
            writeValue(key, output, value);
        }
    }

    public void writeValue(K key, ByteBuf output, Object value) {
        Schema schema = getSchema(key);
        if (schema != null) {
            int begin = output.writerIndex();
            intTool.write(output, 0);
            schema.writeTo(output, value);
            int length = output.writerIndex() - begin - lengthSize;
            intTool.set(output, begin, length);
        } else {
            log.warn("未注册的信息:ID[{}], VALUE[{}]", key, value);
        }
    }
}