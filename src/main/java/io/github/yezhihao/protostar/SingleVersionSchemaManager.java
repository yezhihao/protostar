package io.github.yezhihao.protostar;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * 单版本Schema管理器
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class SingleVersionSchemaManager {

    protected Map<Object, Schema> typeIdMapping = new HashMap<>(64);

    public Object readFrom(Object typeId, ByteBuf input) {
        Schema schema = typeIdMapping.get(typeId);
        return schema.readFrom(input);
    }

    public void writeTo(Object typeId, ByteBuf output, Object element) {
        Schema schema = typeIdMapping.get(typeId);
        schema.writeTo(output, element);
    }

    public Schema getSchema(Object typeId) {
        Schema schema = typeIdMapping.get(typeId);
        return schema;
    }

    protected <T> Schema<T> loadSchema(Map<Object, Schema> root, Object typeId, Class<T> typeClass) {
        Schema<T> schema = typeIdMapping.get(typeId);
        if (schema == null) {
            schema = SingleVersionUtil.getRuntimeSchema(root, typeClass);
            typeIdMapping.put(typeId, schema);
        }
        return schema;
    }

}