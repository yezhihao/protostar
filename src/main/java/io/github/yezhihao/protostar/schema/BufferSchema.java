package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.field.BasicField;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public abstract class BufferSchema {
    public static class ByteBufSchema extends BasicField<ByteBuf> {
        @Override
        public ByteBuf readFrom(ByteBuf input) {
            return input.readSlice(input.readableBytes());
        }

        @Override
        public void writeTo(ByteBuf output, ByteBuf value) {
            output.writeBytes(value);
        }
    }

    public static class ByteBufferSchema extends BasicField<ByteBuffer> {
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
}