package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Fs;
import io.github.yezhihao.protostar.field.BasicField;
import io.github.yezhihao.protostar.schema.RuntimeSchema;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * RuntimeSchema加载策略
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public abstract class LoadStrategy {

    protected Map<Object, Map<Integer, RuntimeSchema<?>>> typeIdMapping = new HashMap<>(64);

    public abstract <T> Map<Integer, RuntimeSchema<T>> getRuntimeSchema(Class<T> typeClass);

    public abstract <T> RuntimeSchema<T> getRuntimeSchema(Class<T> typeClass, Integer version);

    public RuntimeSchema getRuntimeSchema(Object typeId, Integer version) {
        Map<Integer, RuntimeSchema<?>> schemaMap = typeIdMapping.get(typeId);
        if (schemaMap == null)
            return null;
        return schemaMap.get(version);
    }

    protected void loadRuntimeSchema(Map<String, Map<Integer, RuntimeSchema<?>>> root, Object typeId, Class<?> typeClass) {
        Map<Integer, RuntimeSchema<?>> schemas = typeIdMapping.get(typeId);
        if (schemas == null) {
            schemas = loadRuntimeSchema(root, typeClass);
            typeIdMapping.put(typeId, schemas);
        }
    }

    protected Map<Integer, RuntimeSchema<?>> loadRuntimeSchema(Map<String, Map<Integer, RuntimeSchema<?>>> root, Class<?> typeClass) {
        Map<Integer, RuntimeSchema<?>> schemas = root.get(typeClass.getName());
        //不支持循环引用
        if (schemas != null)
            return schemas;

        List<java.lang.reflect.Field> fs = findFields(typeClass);
        if (fs.isEmpty())
            return null;

        root.put(typeClass.getName(), schemas = new HashMap(4));

        Map<Integer, List<BasicField>> multiVersionFields = findMultiVersionFields(root, fs);
        for (Map.Entry<Integer, List<BasicField>> entry : multiVersionFields.entrySet()) {

            Integer version = entry.getKey();
            List<BasicField> fieldList = entry.getValue();

            BasicField[] fields = fieldList.toArray(new BasicField[fieldList.size()]);
            Arrays.sort(fields);

            RuntimeSchema schema = new RuntimeSchema(typeClass, version, fields);
            schemas.put(version, schema);
        }
        return schemas;
    }

    protected List<java.lang.reflect.Field> findFields(Class<?> typeClass) {
        java.lang.reflect.Field[] fs = typeClass.getDeclaredFields();

        List<java.lang.reflect.Field> result = new ArrayList<>(fs.length);

        for (java.lang.reflect.Field f : fs) {
            if (f.isAnnotationPresent(Fs.class) || f.isAnnotationPresent(Field.class)) {
                result.add(f);
            }
        }
        return result;
    }

    protected Map<Integer, List<BasicField>> findMultiVersionFields(Map<String, Map<Integer, RuntimeSchema<?>>> root, List<java.lang.reflect.Field> fs) {
        Map<Integer, List<BasicField>> multiVersionFields = new TreeMap<Integer, List<BasicField>>() {
            @Override
            public List<BasicField> get(Object key) {
                List result = super.get(key);
                if (result == null)
                    super.put((Integer) key, result = new ArrayList<>(fs.size()));
                return result;
            }
        };

        for (java.lang.reflect.Field f : fs) {

            Field field = f.getDeclaredAnnotation(Field.class);
            if (field != null) {
                fillField(root, multiVersionFields, f, field);
            } else {
                Field[] fields = f.getDeclaredAnnotation(Fs.class).value();
                for (int i = 0; i < fields.length; i++)
                    fillField(root, multiVersionFields, f, fields[i]);
            }
        }
        return multiVersionFields;
    }

    protected void fillField(Map<String, Map<Integer, RuntimeSchema<?>>> root, Map<Integer, List<BasicField>> multiVersionFields, java.lang.reflect.Field f, Field field) {
        Class<?> typeClass = f.getType();

        BasicField value;
        int[] versions = field.version();

        if (field.type() == DataType.OBJ || field.type() == DataType.LIST) {
            if (Collection.class.isAssignableFrom(typeClass))
                typeClass = (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
            loadRuntimeSchema(root, typeClass);
            for (int ver : versions) {
                Map<Integer, RuntimeSchema<?>> schemaMap = root.getOrDefault(typeClass.getName(), Collections.EMPTY_MAP);
                RuntimeSchema schema = schemaMap.get(ver);
                value = FieldFactory.create(field, f, schema);
                multiVersionFields.get(ver).add(value);
            }
        } else {
            value = FieldFactory.create(field, f);
            for (int ver : versions) {
                multiVersionFields.get(ver).add(value);
            }
        }
    }
}