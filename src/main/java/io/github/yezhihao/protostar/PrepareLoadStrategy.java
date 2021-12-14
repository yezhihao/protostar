package io.github.yezhihao.protostar;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PrepareLoadStrategy<K> {

    private final Map<K, Schema> schemas = new TreeMap<>();

    public PrepareLoadStrategy<K> addSchema(K key, Schema schema) {
        schemas.put(key, schema);
        return this;
    }

    public PrepareLoadStrategy<K> addSchema(K key, Class typeClass) {
        Schema<Object> schema = SingleVersionUtil.getRuntimeSchema(typeClass);
        schemas.put(key, schema);
        return this;
    }

    public Map<K, Schema> build() {
        Map<K, Schema> a = new HashMap<>(schemas.size());
        a.putAll(schemas);
        return a;
    }
}