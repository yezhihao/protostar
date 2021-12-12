package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.field.*;
import io.github.yezhihao.protostar.schema.MapSchema;
import io.github.yezhihao.protostar.schema.SchemaRegistry;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

/**
 * FieldFactory
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public abstract class FieldFactory {

    public static BasicField create(Field field, java.lang.reflect.Field f, Schema schema) {
        Class<?> typeClass = f.getType();
        if (schema == null)
            throw new IllegalArgumentException("不支持的类型转换 name:" + f.getName() + ",desc:" + field.desc() + "[" + field.length() + " to " + typeClass.getName() + "]");

        if (Map.class.isAssignableFrom(typeClass) && !MapSchema.class.isAssignableFrom(schema.getClass())) {
            Class keyClass = (Class) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
            Schema keySchema = SchemaRegistry.get(keyClass, null);
            if (keySchema != null) {
                Schema valueSchema = schema;
                schema = new MapSchema(keySchema, 1) {
                    @Override
                    public Schema getSchema(Object key) {
                        return valueSchema;
                    }
                };
            }
        }

        if (field.length() > 0) {
            return new FixedLengthField(field, f, schema);
        } else if (field.lengthSize() > 0) {
            if (Collection.class.isAssignableFrom(typeClass))
                return new ArrayTotalField(field, f, schema);
            else
                return new DynamicLengthField(field, f, schema);
        } else {
            if (Collection.class.isAssignableFrom(typeClass))
                return new ArrayField(field, f, schema);
            else
                return new BasicField(field, f, schema);
        }
    }
}