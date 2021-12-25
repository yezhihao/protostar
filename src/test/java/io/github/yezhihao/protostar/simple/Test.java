package io.github.yezhihao.protostar.simple;

import io.github.yezhihao.protostar.ProtostarUtil;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.ArrayMap;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.time.LocalDateTime;

public class Test {

    public static void main(String[] args) {
        ArrayMap<RuntimeSchema> bodySchemas = ProtostarUtil.getRuntimeSchema(Foo.class);
        RuntimeSchema<Foo> bodySchema = bodySchemas.get(0);

        ArrayMap<RuntimeSchema> headSchemas = ProtostarUtil.getRuntimeSchema(BaseDO.class);
        RuntimeSchema<BaseDO> headSchema = headSchemas.get(0);

        ByteBuf buffer = Unpooled.buffer(32);
        Foo foo = foo();
        headSchema.writeTo(buffer, foo);
        bodySchema.writeTo(buffer, foo);

        String hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);


        Foo foo1 = new Foo();

        headSchema.mergeFrom(buffer, foo1);
        bodySchema.mergeFrom(buffer, foo1);
        System.out.println(foo);
    }

    public static Foo foo() {
        Foo foo = new Foo();
        foo.setName("张三");
        foo.setId(128);
        foo.setDateTime(LocalDateTime.of(2020, 7, 7, 19, 23, 59));

        foo.setType(1);
        foo.setClientId("123qwe");
        return foo;
    }

    public static class BaseDO {

        @Field(index = 0, length = 2, desc = "ID")
        protected int type;
        @Field(index = 1, length = 20, desc = "名称")
        protected String clientId;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }
    }

    public static class Foo extends BaseDO {

        @Field(lengthUnit = 1, desc = "名称")
        private String name;
        @Field(length = 2, desc = "ID")
        private int id;
        @Field(charset = "BCD", desc = "日期")
        private LocalDateTime dateTime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Foo{");
            sb.append("type=").append(type);
            sb.append(", clientId='").append(clientId).append('\'');
            sb.append(", name='").append(name).append('\'');
            sb.append(", id=").append(id);
            sb.append(", dateTime=").append(dateTime);
            sb.append('}');
            return sb.toString();
        }
    }
}
