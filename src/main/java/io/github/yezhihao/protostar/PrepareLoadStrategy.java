package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.schema.ArraySchema;
import io.github.yezhihao.protostar.schema.NumberSchema;

import java.util.HashMap;
import java.util.Map;

public abstract class PrepareLoadStrategy extends SingleVersionSchemaManager {

    private final Map<Object, Schema> typeClassMapping = new HashMap<>();

    protected PrepareLoadStrategy() {
        this.addSchemas(this);
    }

    protected void addSchemas(PrepareLoadStrategy schemaRegistry) {
    }

    public <T> Schema<T> getSchema(Class<T> typeClass) {
        return typeClassMapping.get(typeClass);
    }

    public PrepareLoadStrategy addSchema(Object key, Schema schema) {
        if (schema == null)
            throw new RuntimeException("key[" + key + "],schema is null");
        typeIdMapping.put(key, schema);
        return this;
    }

    public PrepareLoadStrategy addSchema(Object key, Class typeClass) {
        loadSchema(typeClassMapping, key, typeClass);
        return this;
    }

    public PrepareLoadStrategy addSchema(Object key, DataType dataType) {
        switch (dataType) {
            case BYTE:
                this.typeIdMapping.put(key, NumberSchema.BYTE_INT);
                break;
            case WORD:
                this.typeIdMapping.put(key, NumberSchema.WORD_INT);
                break;
            case DWORD:
                this.typeIdMapping.put(key, NumberSchema.DWORD_LONG);
                break;
            case QWORD:
                this.typeIdMapping.put(key, NumberSchema.QWORD_LONG);
                break;
            case BYTES:
                this.typeIdMapping.put(key, ArraySchema.BYTES);
                break;
            default:
                throw new IllegalArgumentException("不支持的类型转换" + dataType);
        }
        return this;
    }
}
