package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.github.yezhihao.protostar.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 具有每个元素长度单位的集合域，只能位于消息末尾
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class LengthUnitCollectionField<T> extends BasicField<Collection<T>> {

    private final Schema<T> schema;
    private final int lengthUnit;
    private final IntTool intTool;

    public LengthUnitCollectionField(Schema<T> schema, int lengthUnit) {
        this.schema = schema;
        this.lengthUnit = lengthUnit;
        this.intTool = IntTool.getInstance(lengthUnit);
    }

    @Override
    public Collection<T> readFrom(ByteBuf input) {
        Collection list = new ArrayList<>();
        while (input.isReadable()) {
            int length = intTool.read(input);
            T t = schema.readFrom(input, length);
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list) {
        if (list != null) {
            for (T t : list) {
                if (t != null) {
                    int begin = output.writerIndex();
                    intTool.write(output, 0);
                    schema.writeTo(output, t);
                    int length = output.writerIndex() - begin - lengthUnit;
                    intTool.set(output, begin, length);
                }
            }
        }
    }

    @Override
    public Collection<T> readFrom(ByteBuf input, Explain explain) {
        Collection list = new ArrayList<>();
        while (input.isReadable()) {
            int length = intTool.read(input);
            explain.lengthField(input.readerIndex() - lengthUnit, desc + "长度", length, lengthUnit);
            T t = schema.readFrom(input, length, explain);
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list, Explain explain) {
        if (list != null) {
            for (T t : list) {
                if (t != null) {
                    int begin = output.writerIndex();
                    Info info = explain.lengthField(begin, desc + "长度", 0, lengthUnit);
                    intTool.write(output, 0);
                    schema.writeTo(output, t, explain);
                    int length = output.writerIndex() - begin - lengthUnit;
                    intTool.set(output, begin, length);
                    info.setLength(length, lengthUnit);
                }
            }
        }
    }
}