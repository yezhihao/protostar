package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 指定前置数量的数组域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class TotalArrayPrimitiveField extends BasicField {

    private Schema schema;
    protected final IntTool intTool;
    protected final int valueUnit;

    public TotalArrayPrimitiveField(Schema schema, Field field, int valueUnit) {
        this.schema = schema;
        this.intTool = IntTool.getInstance(field.totalUnit());
        this.valueUnit = valueUnit;
    }

    public Object readFrom(ByteBuf input) {
        int total = intTool.read(input);
        if (total <= 0)
            return null;
        int length = valueUnit * total;
        return schema.readFrom(input, length);
    }

    public void writeTo(ByteBuf output, Object value) {
        if (value == null) {
            intTool.write(output, 0);
        } else {
            int total = Array.getLength(value);
            intTool.write(output, total);
            schema.writeTo(output, value);
        }
    }
}