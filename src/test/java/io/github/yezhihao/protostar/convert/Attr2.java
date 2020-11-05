package io.github.yezhihao.protostar.convert;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class Attr2 {

    private int type;
    private String content;

    public Attr2() {
    }

    public Attr2(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Attr2{");
        sb.append("type=").append(type);
        sb.append(", content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class Schema implements io.github.yezhihao.protostar.Schema<Attr2> {

        public static final Schema INSTANCE = new Schema();

        private Schema() {
        }

        @Override
        public Attr2 readFrom(ByteBuf input) {
            Attr2 message = new Attr2();
            message.type = input.readInt();
            message.content = input.readCharSequence(input.readableBytes(), StandardCharsets.UTF_8).toString();
            return message;
        }

        @Override
        public void writeTo(ByteBuf output, Attr2 message) {
            output.writeInt(message.type);
            output.writeCharSequence(message.content, StandardCharsets.UTF_8);
        }
    }
}
