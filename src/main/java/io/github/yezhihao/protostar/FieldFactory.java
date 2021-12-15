package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.field.*;
import io.github.yezhihao.protostar.schema.SchemaRegistry;

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

        if (field.length() != 0) {
            if (schema instanceof Fixed)
                return new BasicField(field, f, schema);
            return new LengthField(field, f, schema);
        }

        if (field.lengthUnit() > 0) {
            if (Collection.class.isAssignableFrom(typeClass))
                return new LengthUnitCollectionField(field, f, schema);
            return new LengthUnitField(field, f, schema);
        }

        if (field.totalUnit() > 0) {
            if (Collection.class.isAssignableFrom(typeClass)) {
                return new TotalCollectionField(field, f, schema);
            }
            if (Map.class.isAssignableFrom(typeClass)) {
                return new TotalMapField(field, f, schema);
            }
            if (typeClass.isArray()) {
                typeClass = typeClass.getComponentType();
                if (typeClass.isPrimitive())
                    return new TotalArrayPrimitiveField(field, f, schema, SchemaRegistry.getLength(typeClass));
                return new TotalArrayObjectField(field, f, schema);
            }
        }

        if (Collection.class.isAssignableFrom(typeClass)) {
            return new CollectionField(field, f, schema);
        } else if (Map.class.isAssignableFrom(typeClass)) {
            return new MapField(field, f, schema);
        } else {
            return new BasicField(field, f, schema);
        }
    }
}