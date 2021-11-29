package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Fs;
import io.github.yezhihao.protostar.annotation.MergeSuperclass;
import io.github.yezhihao.protostar.field.BasicField;
import io.github.yezhihao.protostar.schema.RuntimeSchema;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * 多版本Schema加载器
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
public class ProtostarUtil {

    private static final Map<String, Map<Integer, RuntimeSchema>> CACHE = new WeakHashMap<>();

    public static Map<Integer, RuntimeSchema> getRuntimeSchema(Class typeClass) {
        return getRuntimeSchema(CACHE, typeClass);
    }

    public static RuntimeSchema getRuntimeSchema(Class typeClass, int version) {
        Map<Integer, RuntimeSchema> schemaMap = getRuntimeSchema(CACHE, typeClass);
        if (schemaMap == null) return null;
        return schemaMap.get(version);
    }

    public static Map<Integer, RuntimeSchema> getRuntimeSchema(Map<String, Map<Integer, RuntimeSchema>> root, final Class typeClass) {
        Map<Integer, RuntimeSchema> schemaMap = root.get(typeClass.getName());
        //不支持循环引用
        if (schemaMap != null) return schemaMap;

        List<java.lang.reflect.Field> fs = findFields(typeClass);
        if (fs.isEmpty()) return null;

        root.put(typeClass.getName(), schemaMap = new HashMap<>(4));

        Map<Integer, List<BasicField>> multiVersionFields = findMultiVersionFields(root, fs);
        for (Map.Entry<Integer, List<BasicField>> entry : multiVersionFields.entrySet()) {

            Integer version = entry.getKey();
            List<BasicField> fieldList = entry.getValue();

            BasicField[] fields = fieldList.toArray(new BasicField[fieldList.size()]);
            Arrays.sort(fields);

            RuntimeSchema schema = new RuntimeSchema(typeClass, version, fields);
            schemaMap.put(version, schema);
        }
        schemaMap = Collections.unmodifiableMap(schemaMap);
        root.put(typeClass.getName(), schemaMap);
        return schemaMap;
    }

    private static List<java.lang.reflect.Field> findFields(Class typeClass) {
        List<java.lang.reflect.Field> fs = new LinkedList<>();

        Class temp = typeClass;
        while (temp != null) {
            fs.addAll(Arrays.asList(temp.getDeclaredFields()));
            if (!temp.isAnnotationPresent(MergeSuperclass.class)) break;
            temp = typeClass.getSuperclass();
        }

        List<java.lang.reflect.Field> result = new ArrayList<>(fs.size());
        for (java.lang.reflect.Field f : fs) {
            if (f.isAnnotationPresent(Fs.class) || f.isAnnotationPresent(Field.class)) {
                result.add(f);
            }
        }
        return result;
    }

    private static Map<Integer, List<BasicField>> findMultiVersionFields(Map<String, Map<Integer, RuntimeSchema>> root, List<java.lang.reflect.Field> fs) {
        Map<Integer, List<BasicField>> multiVersionFields = new TreeMap<Integer, List<BasicField>>() {
            @Override
            public List<BasicField> get(Object key) {
                List result = super.get(key);
                if (result == null) super.put((Integer) key, result = new ArrayList<>(fs.size()));
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

    private static void fillField(Map<String, Map<Integer, RuntimeSchema>> root, Map<Integer, List<BasicField>> multiVersionFields, java.lang.reflect.Field f, Field field) {
        Class typeClass = f.getType();

        BasicField value;
        int[] versions = field.version();

        if (field.type() == DataType.OBJ || field.type() == DataType.LIST) {
            if (Collection.class.isAssignableFrom(typeClass))
                typeClass = (Class) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
            getRuntimeSchema(root, typeClass);
            for (int ver : versions) {
                Map<Integer, RuntimeSchema> schemaMap = root.getOrDefault(typeClass.getName(), Collections.EMPTY_MAP);
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