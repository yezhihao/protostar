package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.schema.SchemaRegistry;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 指定前置数量的数组域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class TotalArrayPrimitiveField extends BasicField {

    private final Schema schema;
    private final int totalUnit;
    private final int valueUnit;
    private final IntTool intTool;

    public TotalArrayPrimitiveField(Schema schema, int totalUnit, Class arrayClass) {
        this.schema = schema;
        this.totalUnit = totalUnit;
        this.valueUnit = SchemaRegistry.getLength(arrayClass);
        this.intTool = IntTool.getInstance(totalUnit);
    }

    @Override
    public Object readFrom(ByteBuf input) {
        int total = intTool.read(input);
        if (total <= 0)
            return null;
        int length = valueUnit * total;
        return schema.readFrom(input, length);
    }

    @Override
    public void writeTo(ByteBuf output, Object value) {
        if (value == null) {
            intTool.write(output, 0);
        } else {
            int total = Array.getLength(value);
            intTool.write(output, total);
            schema.writeTo(output, value);
        }
    }

    @Override
    public Object readFrom(ByteBuf input, Explain explain) {
        int total = intTool.read(input);
        explain.lengthField(input.readerIndex() - totalUnit, desc + "数量", total, totalUnit);
        if (total <= 0)
            return null;
        int length = valueUnit * total;
        return schema.readFrom(input, length, explain);
    }

    @Override
    public void writeTo(ByteBuf output, Object value, Explain explain) {
        if (value == null) {
            explain.lengthField(output.writerIndex(), desc + "数量", 0, totalUnit);
            intTool.write(output, 0);
        } else {
            int total = Array.getLength(value);
            explain.lengthField(output.writerIndex(), desc + "数量", total, totalUnit);
            intTool.write(output, total);
            schema.writeTo(output, value, explain);
        }
    }
}