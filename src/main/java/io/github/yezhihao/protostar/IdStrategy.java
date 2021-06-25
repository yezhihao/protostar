package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.field.BasicField;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Schema加载策略
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public abstract class IdStrategy {

    protected Map<Object, Schema> typeIdMapping = new HashMap<>(64);

    public Object readFrom(Object typeId, ByteBuf input) {
        Schema schema = typeIdMapping.get(typeId);
        return schema.readFrom(input);
    }

    public void writeTo(Object typeId, ByteBuf output, Object element) {
        Schema schema = typeIdMapping.get(typeId);
        schema.writeTo(output, element);
    }

    public Schema getSchema(Object typeId) {
        Schema schema = typeIdMapping.get(typeId);
        return schema;
    }

    public abstract <T> Schema<T> getSchema(Class<T> typeClass);

    protected <T> Schema<T> loadSchema(Map<Object, Schema> root, Object typeId, Class<T> typeClass) {
        Schema<T> schema = typeIdMapping.get(typeId);
        if (schema == null) {
            schema = loadSchema(root, typeClass);
            typeIdMapping.put(typeId, schema);
        }
        return schema;
    }

    protected static <T> Schema<T> loadSchema(Map<Object, Schema> root, Class<T> typeClass) {
        Schema schema = root.get(typeClass.getName());
        //不支持循环引用
        if (schema != null)
            return (Schema<T>) schema;

        List<java.lang.reflect.Field> fs = findFields(typeClass);
        if (fs.isEmpty())
            return null;

        List<BasicField> fieldList = findFields(root, fs);
        BasicField[] fields = fieldList.toArray(new BasicField[fieldList.size()]);
        Arrays.sort(fields);

        schema = new RuntimeSchema(typeClass, 0, fields);
        root.put(typeClass.getName(), schema);
        return (Schema<T>) schema;
    }

    protected static List<java.lang.reflect.Field> findFields(Class typeClass) {
        java.lang.reflect.Field[] fields = typeClass.getDeclaredFields();
        List<java.lang.reflect.Field> result = new ArrayList<>(fields.length);

        for (java.lang.reflect.Field f : fields) {
            if (f.isAnnotationPresent(Field.class)) {
                result.add(f);
            }
        }
        return result;
    }

    protected static List<BasicField> findFields(Map<Object, Schema> root, List<java.lang.reflect.Field> fs) {
        List<BasicField> fields = new ArrayList<>(fs.size());

        for (java.lang.reflect.Field f : fs) {
            Field field = f.getDeclaredAnnotation(Field.class);
            if (field != null) {
                fillField(root, fields, f, field);
            }
        }
        return fields;
    }

    protected static void fillField(Map<Object, Schema> root, List<BasicField> fields, java.lang.reflect.Field f, Field field) {
        Class typeClass = f.getType();

        BasicField value;

        if (field.type() == DataType.OBJ || field.type() == DataType.LIST) {
            if (Collection.class.isAssignableFrom(typeClass))
                typeClass = (Class) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
            loadSchema(root, typeClass);
            Schema schema = root.get(typeClass.getName());
            value = FieldFactory.create(field, f, schema);
            fields.add(value);
        } else {
            value = FieldFactory.create(field, f);
            fields.add(value);
        }
    }
}