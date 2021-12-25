package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Message;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.util.ArrayMap;
import io.github.yezhihao.protostar.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
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
        this(256, basePackages);
    }

    public SchemaManager(int initialCapacity, String... basePackages) {
        this(initialCapacity);
        for (String basePackage : basePackages) {
            List<Class> types = ClassUtils.getClassList(basePackage);
            for (Class<?> type : types) {
                Message message = type.getAnnotation(Message.class);
                if (message != null) {
                    int[] values = message.value();
                    for (Integer typeId : values)
                        loadRuntimeSchema(typeId, type);
                }
            }
        }
    }

    public void loadRuntimeSchema(Integer typeId, Class typeClass) {
        ArrayMap<RuntimeSchema> schemaMap = ProtostarUtil.getRuntimeSchema(typeClassMapping, typeClass);
        if (schemaMap != null) typeIdMapping.put(typeId, schemaMap);
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