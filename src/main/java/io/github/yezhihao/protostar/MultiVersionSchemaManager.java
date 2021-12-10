package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Message;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多版本Schema管理器
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class MultiVersionSchemaManager {

    private final Map<Integer, Map<Integer, RuntimeSchema>> typeIdMapping;

    private final Map<String, Map<Integer, RuntimeSchema>> typeClassMapping;

    public MultiVersionSchemaManager() {
        this(128);
    }

    public MultiVersionSchemaManager(int initialCapacity) {
        this.typeIdMapping = new HashMap<>(initialCapacity);
        this.typeClassMapping = new HashMap<>(initialCapacity);
    }

    public MultiVersionSchemaManager(String... basePackages) {
        this(256, basePackages);
    }

    public MultiVersionSchemaManager(int initialCapacity, String... basePackages) {
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
        Map<Integer, RuntimeSchema> schemaMap = ProtostarUtil.getRuntimeSchema(typeClassMapping, typeClass);
        if (schemaMap != null) typeIdMapping.put(typeId, schemaMap);
    }

    public <T> RuntimeSchema<T> getRuntimeSchema(Class<T> typeClass, Integer version) {
        Map<Integer, RuntimeSchema> schemaMap = ProtostarUtil.getRuntimeSchema(typeClassMapping, typeClass);
        if (schemaMap == null) return null;
        return schemaMap.get(version);
    }

    public Map<Integer, RuntimeSchema> getRuntimeSchema(Class typeClass) {
        return ProtostarUtil.getRuntimeSchema(typeClassMapping, typeClass);
    }

    public RuntimeSchema getRuntimeSchema(Integer typeId, Integer version) {
        Map<Integer, RuntimeSchema> schemaMap = typeIdMapping.get(typeId);
        if (schemaMap == null) return null;
        return schemaMap.get(version);
    }

    public Map<Integer, RuntimeSchema> getRuntimeSchema(Integer typeId) {
        return typeIdMapping.get(typeId);
    }
}