package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
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

    public T readFrom(ByteBuf input) {
        int length = intTool.read(input);
        return schema.readFrom(input, length);
    }

    public void writeTo(ByteBuf output, T value) {
        int begin = output.writerIndex();
        intTool.write(output, 0);
        if (value != null) {
            schema.writeTo(output, value);
            int length = output.writerIndex() - begin - lengthUnit;
            intTool.set(output, begin, length);
        }
    }
}