package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.schema.ArraySchema;
import io.github.yezhihao.protostar.schema.NumberSchema;

import java.util.HashMap;
import java.util.Map;

public abstract class PrepareLoadStrategy extends IdStrategy {

    private final Map<Object, Schema> typeClassMapping = new HashMap<>();

    protected PrepareLoadStrategy() {
        this.addSchemas(this);
    }

    protected abstract void addSchemas(PrepareLoadStrategy schemaRegistry);

    @Override
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
                this.typeIdMapping.put(key, NumberSchema.Int8.INSTANCE);
                break;
            case WORD:
                this.typeIdMapping.put(key, NumberSchema.Int16.INSTANCE);
                break;
            case DWORD:
                this.typeIdMapping.put(key, NumberSchema.Long32.INSTANCE);
                break;
            case QWORD:
                this.typeIdMapping.put(key, NumberSchema.Long64.INSTANCE);
                break;
            case BYTES:
                this.typeIdMapping.put(key, ArraySchema.ByteArraySchema.INSTANCE);
                break;
            default:
                throw new RuntimeException("不支持的类型转换");
        }
        return this;
    }
}
