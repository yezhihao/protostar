package io.github.yezhihao.protostar.simple;

import io.github.yezhihao.protostar.DataType;
import io.github.yezhihao.protostar.FieldFactory;
import io.github.yezhihao.protostar.ProtostarUtil;
import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.time.LocalDateTime;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        FieldFactory.EXPLAIN = true;

        Map<Integer, Schema<Foo>> multiVersionSchema = ProtostarUtil.getSchema(Foo.class);
        Schema<Foo> schema = multiVersionSchema.get(0);

        ByteBuf buffer = Unpooled.buffer(32);
        schema.writeTo(buffer, foo());
        String hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        Foo foo = schema.readFrom(buffer);
        System.out.println(foo);
    }

    public static Foo foo() {
        Foo foo = new Foo();
        foo.setName("张三");
        foo.setId(128);
        foo.setDateTime(LocalDateTime.of(2020, 7, 7, 19, 23, 59));
        return foo;
    }

    public static class Foo {

        private String name;
        private int id;
        private LocalDateTime dateTime;

        @Field(index = 0, type = DataType.STRING, lengthSize = 1, desc = "名称")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Field(index = 1, type = DataType.WORD, desc = "ID")
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Field(index = 3, type = DataType.BCD8421, desc = "日期")
        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Foo{");
            sb.append("name='").append(name).append('\'');
            sb.append(", id=").append(id);
            sb.append(", dateTime=").append(dateTime);
            sb.append('}');
            return sb.toString();
        }
    }
}
