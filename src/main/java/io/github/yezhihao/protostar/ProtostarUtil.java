package io.github.yezhihao.protostar;

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

    public static Schema getSchema(Object typeId, Integer version) {
        return LOAD_STRATEGY.getSchema(typeId, version);
    }

    public static Schema getSchema(Class<?> typeClass, Integer version) {
        return LOAD_STRATEGY.getSchema(typeClass, version);
    }

    public static <T> Map<Integer, Schema<T>> getSchema(Class<T> typeClass) {
        return LOAD_STRATEGY.getSchema(typeClass);
    }
}