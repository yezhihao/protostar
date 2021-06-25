package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Message;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public class DefaultLoadStrategy extends LoadStrategy {

    private Map<String, Map<Integer, RuntimeSchema<?>>> typeClassMapping = new HashMap(140);

    public DefaultLoadStrategy() {
    }

    public DefaultLoadStrategy(String basePackage) {
        List<Class<?>> types = ClassUtils.getClassList(basePackage);
        for (Class<?> type : types) {
            Message message = type.getAnnotation(Message.class);
            if (message != null) {
                int[] values = message.value();
                for (int typeId : values)
                    loadRuntimeSchema(typeClassMapping, typeId, type);
            }
        }
    }

    @Override
    public <T> RuntimeSchema<T> getRuntimeSchema(Class<T> typeClass, Integer version) {
        Map<Integer, RuntimeSchema<?>> schemas = typeClassMapping.get(typeClass.getName());
        if (schemas == null) {
            schemas = loadRuntimeSchema(typeClassMapping, typeClass);
        }
        if (schemas == null) return null;
        return (RuntimeSchema<T>) schemas.get(version);
    }

    @Override
    public <T> Map<Integer, RuntimeSchema<T>> getRuntimeSchema(Class<T> typeClass) {
        Map<Integer, RuntimeSchema<?>> schemas = typeClassMapping.get(typeClass.getName());
        if (schemas == null) {
            schemas = loadRuntimeSchema(typeClassMapping, typeClass);
        }
        if (schemas == null) return null;

        HashMap<Integer, RuntimeSchema<T>> result = new HashMap<>(schemas.size());
        for (Map.Entry<Integer, RuntimeSchema<?>> entry : schemas.entrySet()) {
            result.put(entry.getKey(), (RuntimeSchema<T>) entry.getValue());
        }
        return result;
    }
}