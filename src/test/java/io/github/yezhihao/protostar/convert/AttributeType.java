package io.github.yezhihao.protostar.convert;

import io.github.yezhihao.protostar.IdStrategy;
import io.github.yezhihao.protostar.PrepareLoadStrategy;
import io.github.yezhihao.protostar.schema.NumberSchema;
import io.github.yezhihao.protostar.schema.StringSchema;

public class AttributeType extends PrepareLoadStrategy {

    public static final IdStrategy INSTANCE = new AttributeType();

    @Override
    protected void addSchemas(PrepareLoadStrategy schemaRegistry) {
        schemaRegistry
                .addSchema(1, NumberSchema.Int32.INSTANCE)
                .addSchema(2, StringSchema.Chars.getInstance((byte) 0, "GBK"))

                .addSchema(3, Attr1.class)
                .addSchema(4, Attr2.Schema.INSTANCE);
    }
}