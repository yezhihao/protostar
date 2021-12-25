package io.github.yezhihao.protostar.convert;

import io.github.yezhihao.protostar.ProtostarUtil;
import io.github.yezhihao.protostar.util.ArrayMap;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Map;
import java.util.TreeMap;

public class PressureTest {

    //1100
    public static void main(String[] args) {
        ArrayMap<RuntimeSchema> multiVersionSchema = ProtostarUtil.getRuntimeSchema(T0200.class);
        RuntimeSchema<T0200> schema = multiVersionSchema.get(0);

        ByteBuf buf = Unpooled.buffer(128);
        schema.writeTo(buf, foo());


        while (true) {
            long s = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                T0200 msg = schema.readFrom(buf);
                buf.setIndex(0, 0);
                msg.setWarnBit(msg.getWarnBit() + 1);
                schema.writeTo(buf, foo());
            }
            System.out.println(System.currentTimeMillis() - s);
        }
    }

    public static T0200 foo() {
        T0200 bean = new T0200();
        bean.setMessageId(1);
        bean.setProperties(2);
        bean.setProtocolVersion(3);
        bean.setClientId("210987654321");
        bean.setSerialNo(0);
        bean.setPackageNo(1);
        bean.setPackageTotal(1);
        bean.setWarnBit(0);
        bean.setStatusBit(2048);
        bean.setLatitude(116307629);
        bean.setLongitude(40058359);
        bean.setAltitude(312);
        bean.setSpeed(3);
        bean.setDirection(99);
        bean.setDateTime("200707192359");
        Map<Integer, Object> attrs = new TreeMap<>();
        attrs.put(1, 123);
        attrs.put(2, "李四");
        attrs.put(3, new Attr1("test", 1));
        attrs.put(4, new Attr2(2, "test2"));
        bean.setAttributes(attrs);
        return bean;
    }

}
