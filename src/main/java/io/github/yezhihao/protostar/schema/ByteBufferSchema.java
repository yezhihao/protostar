package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.field.BasicField;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public class ByteBufferSchema extends BasicField<ByteBuffer> {

    @Override
    public ByteBuffer readFrom(ByteBuf input) {
        ByteBuffer message = input.nioBuffer();
        input.skipBytes(input.readableBytes());
        return message;
    }

    @Override
    public void writeTo(ByteBuf output, ByteBuffer value) {
        output.writeBytes(value);
    }
}