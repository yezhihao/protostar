package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Message;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.util.ArrayMap;
import io.github.yezhihao.protostar.util.ClassUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 多版本Schema管理器
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class SchemaManager {

    private final Map<Integer, ArrayMap<RuntimeSchema>> typeIdMapping;

    private final Map<String, ArrayMap<RuntimeSchema>> typeClassMapping;

    public SchemaManager() {
        this(128);
    }

    public SchemaManager(int initialCapacity) {
        this.typeIdMapping = new HashMap<>(initialCapacity);
        this.typeClassMapping = new HashMap<>(initialCapacity);
    }

    public SchemaManager(String... basePackages) {
        this(Arrays.stream(basePackages).map(ClassUtils::getClassList).flatMap(Collection::stream).toArray(Class[]::new));
    }

    public SchemaManager(Class... types) {
        this(types.length * 2);
        loadRuntimeSchema(types);
    }

    public SchemaManager loadRuntimeSchema(Class... types) {
        for (Class<?> type : types) {
            Message message = type.getAnnotation(Message.class);
            if (message != null) {
                int[] values = message.value();
                for (Integer typeId : values)
                    loadRuntimeSchema(typeId, type);
            }
        }
        return this;
    }

    public SchemaManager loadRuntimeSchema(Integer typeId, Class typeClass) {
        ArrayMap<RuntimeSchema> schemaMap = ProtostarUtil.getRuntimeSchema(typeClassMapping, typeClass);
        if (schemaMap != null) typeIdMapping.put(typeId, schemaMap);
        return this;
    }

    public <T> RuntimeSchema<T> getRuntimeSchema(Class<T> typeClass, int version) {
        ArrayMap<RuntimeSchema> schemaMap = ProtostarUtil.getRuntimeSchema(typeClassMapping, typeClass);
        if (schemaMap == null) return null;
        return schemaMap.getOrDefault(version);
    }

    public ArrayMap<RuntimeSchema> getRuntimeSchema(Class typeClass) {
        return ProtostarUtil.getRuntimeSchema(typeClassMapping, typeClass);
    }

    public RuntimeSchema getRuntimeSchema(Integer typeId, int version) {
        ArrayMap<RuntimeSchema> schemaMap = typeIdMapping.get(typeId);
        if (schemaMap == null) return null;
        return schemaMap.getOrDefault(version);
    }

    public ArrayMap<RuntimeSchema> getRuntimeSchema(Integer typeId) {
        return typeIdMapping.get(typeId);
    }
}