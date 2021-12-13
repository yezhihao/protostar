package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.github.yezhihao.protostar.util.IntTool;
import io.github.yezhihao.protostar.util.StrUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.lang.reflect.Array;

/**
 * 指定前置数量的数组域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class TotalArrayPrimitiveField extends BasicField {

    protected final IntTool intTool;
    protected final int valueUnit;

    public TotalArrayPrimitiveField(Field field, java.lang.reflect.Field f, Schema schema, int valueUnit) {
        super(field, f, schema);
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

    @Override
    public Object readFrom(ByteBuf input, Explain explain) {
        int begin = input.readerIndex();
        int total = intTool.read(input);
        explain.add(Info.lengthField(begin, field, total));

        if (total <= 0)
            return null;
        int length = valueUnit * total;
        Object array = schema.readFrom(input, length);

        int end = input.readerIndex();
        String raw = ByteBufUtil.hexDump(input, begin + field.totalUnit(), end - begin - field.totalUnit());
        explain.add(Info.field(begin + field.totalUnit(), field, StrUtils.toString(array), raw));
        return array;
    }

    @Override
    public void writeTo(ByteBuf output, Object value, Explain explain) {
        int begin = output.readerIndex();
        if (value == null) {
            explain.add(Info.lengthField(begin, field, 0));
            intTool.write(output, 0);
        } else {
            int total = Array.getLength(value);
            explain.add(Info.lengthField(begin, field, total));
            intTool.write(output, total);
            schema.writeTo(output, value);
        }
    }
}