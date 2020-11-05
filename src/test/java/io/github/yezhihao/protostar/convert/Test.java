package io.github.yezhihao.protostar.convert;

import io.github.yezhihao.protostar.DataType;
import io.github.yezhihao.protostar.ProtostarUtil;
import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Convert;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Fs;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

public class Test {

    public static void main(String[] args) {
        Map<Integer, Schema<Foo>> multiVersionSchema = ProtostarUtil.getSchema(Foo.class);
        Schema<Foo> schema_v0 = multiVersionSchema.get(0);
        Schema<Foo> schema_v1 = multiVersionSchema.get(1);

        ByteBuf buffer = Unpooled.buffer(32);
        schema_v0.writeTo(buffer, foo());
        String hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        Foo foo = schema_v0.readFrom(buffer);
        System.out.println(foo);
        System.out.println("=========================version: 0");

        buffer = Unpooled.buffer(32);
        schema_v1.writeTo(buffer, foo());
        hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        foo = schema_v1.readFrom(buffer);
        System.out.println(foo);
        System.out.println("=========================version: 1");
    }

    public static Foo foo() {
        Foo foo = new Foo();
        foo.setName("张三");
        foo.setId(128);
        foo.setDateTime(LocalDateTime.of(2020, 7, 7, 19, 23, 59));
        Map<Integer, Object> attrs = new TreeMap<>();
        attrs.put(1, 123);
        attrs.put(2, "李四");
        attrs.put(3, new Attr1("test", 1));
        attrs.put(4, new Attr2(2, "test2"));
        foo.setAttributes(attrs);
        return foo;
    }

    public static class Foo {

        private String name;
        private int id;
        private LocalDateTime dateTime;
        private Map<Integer, Object> attributes;

        @Fs({@Field(index = 0, type = DataType.STRING, lengthSize = 1, desc = "名称", version = 0),
                @Field(index = 0, type = DataType.STRING, length = 10, desc = "名称", version = 1)})
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Fs({@Field(index = 1, type = DataType.WORD, desc = "ID", version = 0),
                @Field(index = 1, type = DataType.DWORD, desc = "ID", version = 1)})
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Field(index = 3, type = DataType.BCD8421, desc = "日期", version = {0, 1})
        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        @Convert(converter = AttributeConverter.class)
        @Field(index = 4, type = DataType.MAP, desc = "属性", version = {0, 1})
        public Map<Integer, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<Integer, Object> attributes) {
            this.attributes = attributes;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Foo{");
            sb.append("name='").append(name).append('\'');
            sb.append(", id=").append(id);
            sb.append(", dateTime=").append(dateTime);
            sb.append(", attributes=").append(attributes);
            sb.append('}');
            return sb.toString();
        }
    }
}
