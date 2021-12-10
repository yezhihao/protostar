package io.github.yezhihao.protostar.convert;

import io.github.yezhihao.protostar.PrepareLoadStrategy;
import io.github.yezhihao.protostar.schema.MapSchema;
import io.github.yezhihao.protostar.schema.NumberSchema;
import io.github.yezhihao.protostar.schema.StringSchema;

public class AttributeSchemaV2 extends MapSchema<Integer, Object> {

    public AttributeSchemaV2() {
        super(NumberSchema.BYTE2Int, 1);
    }

    @Override
    protected void addSchemas(PrepareLoadStrategy schemaRegistry) {
        schemaRegistry
                .addSchema(1, NumberSchema.DWORD2Int)
                .addSchema(2, StringSchema.Chars.getInstance((byte) 0, "UTF-8"))

                .addSchema(3, Attr1.class)
                .addSchema(4, Attr2.Schema.INSTANCE);
    }
}