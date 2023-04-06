package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * 表达式长度域
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class ExpressionLengthField<T> extends BasicField<T> {

    private static final ExpressionParser parser = new SpelExpressionParser();

    private final BasicField<T> schema;
    private final Expression expression;

    public ExpressionLengthField(BasicField<T> schema, String lengthExpression) {
        this.schema = schema;
        this.expression = parser.parseExpression(lengthExpression);
    }

    @Override
    public BasicField init(Field field, java.lang.reflect.Field f, int position) {
        BasicField init = super.init(field, f, position);
        schema.init(field, f, position);
        return init;
    }

    @Override
    public T readFrom(ByteBuf input) {
        return schema.readFrom(input);
    }

    @Override
    public void writeTo(ByteBuf output, T value) {
        schema.writeTo(output, value);
    }

    @Override
    public T readFrom(ByteBuf input, Explain explain) {
        return schema.readFrom(input, explain);
    }

    @Override
    public void writeTo(ByteBuf output, T value, Explain explain) {
        schema.writeTo(output, value, explain);
    }

    @Override
    public void readAndSet(ByteBuf input, Object obj) throws Exception {
        Integer length = expression.getValue(obj, Integer.class);
        if (length <= 0)
            return;
        T value = schema.readFrom(input, length);
        f.set(obj, value);
    }

    @Override
    public void getAndWrite(ByteBuf output, Object obj) throws Exception {
        Integer length = expression.getValue(obj, Integer.class);
        if (length <= 0)
            return;
        T value = (T) f.get(obj);
        schema.writeTo(output, length, value);
    }

    @Override
    public void readAndSet(ByteBuf input, Object obj, Explain explain) throws Exception {
        Integer length = expression.getValue(obj, Integer.class);
        if (length <= 0)
            return;
        T value = schema.readFrom(input, length, explain);
        f.set(obj, value);
    }

    @Override
    public void getAndWrite(ByteBuf output, Object obj, Explain explain) throws Exception {
        Integer length = expression.getValue(obj, Integer.class);
        if (length <= 0)
            return;
        T value = (T) f.get(obj);
        schema.writeTo(output, length, value, explain);
    }
}