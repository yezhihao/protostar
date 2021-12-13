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

        if (Map.class.isAssignableFrom(typeClass) && !(schema instanceof MapSchema)) {
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
            if (schema instanceof Fixed)
                return new BasicField(field, f, schema);
            return new LengthField(field, f, schema);
        }

        if (field.lengthUnit() > 0) {
            return new LengthUnitField(field, f, schema);
        }

        if (field.totalUnit() > 0) {
            if (Collection.class.isAssignableFrom(typeClass))
                return new TotalCollectionField(field, f, schema);
            else if (typeClass.isArray()) {
                typeClass = typeClass.getComponentType();
                if (typeClass.isPrimitive())
                    return new TotalArrayPrimitiveField(field, f, schema, SchemaRegistry.getLength(typeClass));
                return new TotalArrayObjectField(field, f, schema);
            }
        }

        if (Collection.class.isAssignableFrom(typeClass)) {
            return new CollectionField(field, f, schema);
        } else {
            return new BasicField(field, f, schema);
        }
    }
}