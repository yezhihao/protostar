package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.field.*;
import io.github.yezhihao.protostar.schema.*;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;

/**
 * FieldFactory
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public abstract class FieldFactory {

    public static boolean EXPLAIN = false;

    public static BasicField create(Field field, java.lang.reflect.Field f) {
        return create(field, f, null);
    }

    public static BasicField create(Field field, java.lang.reflect.Field f, Schema schema) {
        DataType dataType = field.type();
        Class<?> typeClass = f.getType();

        Schema fieldSchema = null;
        switch (dataType) {
            case BYTE:
            case WORD:
            case DWORD:
            case QWORD:
                if (typeClass.isArray())
                    fieldSchema = ArraySchema.getSchema(dataType);
                else
                    fieldSchema = NumberSchema.getSchema(dataType, typeClass);
                break;
            case BCD8421:
                if (LocalDateTime.class.isAssignableFrom(typeClass))
                    fieldSchema = DateTimeSchema.BCD.INSTANCE;
                else
                    fieldSchema = StringSchema.BCD.INSTANCE;
                break;
            case BYTES:
                if (String.class.isAssignableFrom(typeClass))
                    fieldSchema = StringSchema.Chars.getInstance(field.pad(), field.charset());
                else if (LocalDateTime.class.isAssignableFrom(typeClass))
                    fieldSchema = DateTimeSchema.BYTES.INSTANCE;
                else if (ByteBuffer.class.isAssignableFrom(typeClass))
                    fieldSchema = ByteBufferSchema.INSTANCE;
                else
                    fieldSchema = ArraySchema.ByteArraySchema.INSTANCE;
                break;
            case HEX:
                fieldSchema = StringSchema.HEX.INSTANCE;
                break;
            case STRING:
                fieldSchema = StringSchema.Chars.getInstance(field.pad(), field.charset());
                break;
            case OBJ:
                if (schema != null)
                    fieldSchema = ObjectSchema.getInstance(schema);
                else
                    fieldSchema = ConvertSchema.getInstance(field.converter());
                break;
            case LIST:
                fieldSchema = CollectionSchema.getInstance(schema);
                break;
            case MAP:
                fieldSchema = ConvertSchema.getInstance(field.converter());
                break;
        }

        if (fieldSchema == null)
            throw new IllegalArgumentException("不支持的类型转换 field:" + f.getName() + ",desc:" + field.desc() + "[" + dataType + " to " + typeClass.getSimpleName() + "]");

        BasicField result;
        if (EXPLAIN) {
            if (field.lengthSize() > 0) {
                if (fieldSchema instanceof CollectionSchema)
                    result = new DynamicTotalField.Logger(field, f, fieldSchema);
                else
                    result = new DynamicLengthField.Logger(field, f, fieldSchema);
            } else if (field.length() > 0) {
                result = new FixedLengthField.Logger(field, f, fieldSchema);
            } else {
                result = new FixedField.Logger(field, f, fieldSchema);
            }
        } else {
            if (field.lengthSize() > 0) {
                if (fieldSchema instanceof CollectionSchema)
                    result = new DynamicTotalField(field, f, fieldSchema);
                else
                    result = new DynamicLengthField(field, f, fieldSchema);
            } else if (field.length() > 0) {
                result = new FixedLengthField(field, f, fieldSchema);
            } else {
                result = new FixedField(field, f, fieldSchema);
            }
        }
        return result;
    }
}