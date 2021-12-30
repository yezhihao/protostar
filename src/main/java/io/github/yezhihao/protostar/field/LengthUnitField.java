package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

/**
 * 指定长度单位域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class LengthUnitField<T> extends BasicField<T> {

    private final Schema<T> schema;
    private final int lengthUnit;
    private final IntTool intTool;

    public LengthUnitField(Schema<T> schema, int lengthUnit) {
        this.schema = schema;
        this.lengthUnit = lengthUnit;
        this.intTool = IntTool.getInstance(lengthUnit);
    }

    @Override
    public T readFrom(ByteBuf input) {
        int length = intTool.read(input);
        return schema.readFrom(input, length);
    }

    @Override
    public void writeTo(ByteBuf output, T value) {
        int begin = output.writerIndex();
        intTool.write(output, 0);
        if (value != null) {
            schema.writeTo(output, value);
            int length = output.writerIndex() - begin - lengthUnit;
            intTool.set(output, begin, length);
        }
    }

    @Override
    public T readFrom(ByteBuf input, Explain explain) {
        int length = intTool.read(input);
        explain.lengthField(input.readerIndex() - lengthUnit, desc + "长度", length, lengthUnit);
        T value = schema.readFrom(input, length, explain);
        explain.setLastDesc(desc);
        return value;
    }

    @Override
    public void writeTo(ByteBuf output, T value, Explain explain) {
        int begin = output.writerIndex();
        Info info = explain.lengthField(begin, desc + "长度", 0, lengthUnit);
        intTool.write(output, 0);
        if (value != null) {
            schema.writeTo(output, value, explain);
            explain.setLastDesc(desc);
            int length = output.writerIndex() - begin - lengthUnit;
            intTool.set(output, begin, length);
            info.setLength(length, lengthUnit);
        }
    }
}