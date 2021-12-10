package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.field.*;
import io.github.yezhihao.protostar.schema.*;
import io.github.yezhihao.protostar.util.Cache;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;

/**
 * FieldFactory
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public abstract class FieldFactory {

    private static final Cache<String, Schema> cache = new Cache<>();

    private static Schema getInstance(Class<? extends Schema> clazz) {
        return cache.get(clazz.getName(), () -> {
            try {
                return clazz.getDeclaredConstructor((Class[]) null).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

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
                    fieldSchema = DateTimeSchema.BCD;
                else
                    fieldSchema = StringSchema.BCD;
                break;
            case BYTES:
                if (String.class.isAssignableFrom(typeClass))
                    fieldSchema = StringSchema.Chars.getInstance(field.pad(), field.charset());
                else if (LocalDateTime.class.isAssignableFrom(typeClass))
                    fieldSchema = DateTimeSchema.BYTES;
                else if (ByteBuffer.class.isAssignableFrom(typeClass))
                    fieldSchema = ByteBufferSchema.INSTANCE;
                else
                    fieldSchema = ArraySchema.BYTE_ARRAY;
                break;
            case HEX:
                fieldSchema = StringSchema.HEX;
                break;
            case STRING:
                fieldSchema = StringSchema.Chars.getInstance(field.pad(), field.charset());
                break;
            case OBJ:
                if (schema != null)
                    fieldSchema = schema;
                else
                    fieldSchema = getInstance(field.converter());
                break;
            case LIST:
                fieldSchema = schema;
                break;
            case MAP:
                fieldSchema = getInstance(field.converter());
                break;
        }

        if (fieldSchema == null)
            throw new IllegalArgumentException("不支持的类型转换 field:" + f.getName() + ",desc:" + field.desc() + "[" + dataType + " to " + typeClass.getSimpleName() + "]");

        if (field.length() > 0) {
            return new FixedLengthField(field, f, fieldSchema);
        } else if (field.lengthSize() > 0) {
            if (DataType.LIST == dataType)
                return new ArrayTotalField(field, f, fieldSchema);
            else
                return new DynamicLengthField(field, f, fieldSchema);
        } else {
            if (DataType.LIST == dataType)
                return new ArrayField(field, f, fieldSchema);
            else
                return new BasicField(field, f, fieldSchema);
        }
    }
}