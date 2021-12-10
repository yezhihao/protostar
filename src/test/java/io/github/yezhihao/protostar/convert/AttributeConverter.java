package io.github.yezhihao.protostar.convert;

import io.github.yezhihao.protostar.PrepareLoadStrategy;
import io.github.yezhihao.protostar.converter.MapConverter;
import io.github.yezhihao.protostar.schema.NumberSchema;
import io.github.yezhihao.protostar.schema.StringSchema;
import io.netty.buffer.ByteBuf;

public class AttributeConverter extends MapConverter<Integer, Object> {

    @Override
    protected void addSchemas(PrepareLoadStrategy schemaRegistry) {
        schemaRegistry
                .addSchema(1, NumberSchema.DWORD2Int.INSTANCE)
                .addSchema(2, StringSchema.Chars.getInstance((byte) 0, "GBK"))

                .addSchema(3, Attr1.class)
                .addSchema(4, Attr2.Schema.INSTANCE);
    }

    @Override
    protected Integer readKey(ByteBuf input) {
        return (int) input.readUnsignedByte();
    }

    @Override
    protected void writeKey(ByteBuf output, Integer key) {
        output.writeByte(key);
    }

    @Override
    protected int valueSize() {
        return 1;
    }
}