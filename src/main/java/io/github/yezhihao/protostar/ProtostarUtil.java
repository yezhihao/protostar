package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.schema.RuntimeSchema;

import java.util.Map;

/**
 * 消息ID关系映射
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public class ProtostarUtil {

    private static volatile boolean Initial = false;

    private static LoadStrategy LOAD_STRATEGY = new DefaultLoadStrategy();

    public static void initial(String basePackage) {
        if (!Initial) {
            synchronized (ProtostarUtil.class) {
                if (!Initial) {
                    Initial = true;
                    LOAD_STRATEGY = new DefaultLoadStrategy(basePackage);
                }
            }
        }
    }

    public static RuntimeSchema getRuntimeSchema(Object typeId, Integer version) {
        return LOAD_STRATEGY.getRuntimeSchema(typeId, version);
    }

    public static RuntimeSchema getRuntimeSchema(Class<?> typeClass, Integer version) {
        return LOAD_STRATEGY.getRuntimeSchema(typeClass, version);
    }

    public static <T> Map<Integer, RuntimeSchema<T>> getRuntimeSchema(Class<T> typeClass) {
        return LOAD_STRATEGY.getRuntimeSchema(typeClass);
    }
}