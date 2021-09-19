package io.github.yezhihao.protostar.converter;

import io.github.yezhihao.protostar.PrepareLoadStrategy;
import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public abstract class MapConverter<K, V> extends PrepareLoadStrategy implements Converter<Map<K, V>> {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    protected abstract K readKey(ByteBuf input);

    protected abstract void writeKey(ByteBuf output, K key);

    protected abstract int valueSize();

    @Override
    public Map<K, V> convert(ByteBuf input) {
        if (!input.isReadable())
            return null;
        Map<K, V> map = new TreeMap<>();
        do {
            K key = readKey(input);
            int length = ByteBufUtils.readInt(input, valueSize());

            if (input.isReadable(length)) {
                int writerIndex = input.writerIndex();
                input.writerIndex(input.readerIndex() + length);
                map.put(key, (V) readValue(key, input));
                input.writerIndex(writerIndex);
            } else {
                map.put(key, (V) readValue(key, input));
            }
        } while (input.isReadable());
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
    public void convert(ByteBuf output, Map<K, V> map) {

        if (map == null || map.isEmpty())
            return;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            writeKey(output, key);
            writeValue(key, output, value);
        }
    }

    public void writeValue(K key, ByteBuf output, Object value) {
        Schema schema = getSchema(key);
        if (schema != null) {
            int lengthSize = valueSize();
            int begin = output.writerIndex();
            ByteBufUtils.writeInt(output, lengthSize, 0);
            schema.writeTo(output, value);
            int length = output.writerIndex() - begin - lengthSize;
            ByteBufUtils.setInt(output, begin, lengthSize, length);
        } else {
            log.warn("未注册的信息:ID[{}], VALUE[{}]", key, value);
        }
    }
}