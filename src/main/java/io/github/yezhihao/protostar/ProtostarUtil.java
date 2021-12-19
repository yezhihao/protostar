package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Fs;
import io.github.yezhihao.protostar.annotation.MergeSuperclass;
import io.github.yezhihao.protostar.field.BasicField;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.schema.SchemaRegistry;
import io.github.yezhihao.protostar.util.ClassUtils;

import java.util.*;

/**
 * 多版本Schema加载器
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
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
        LinkedList<java.lang.reflect.Field> fs = new LinkedList<>();

        boolean addFirst = false;
        Class<?> temp = typeClass;

        while (temp != null) {
            if (addFirst)
                fs.addAll(0, Arrays.asList(temp.getDeclaredFields()));
            else
                fs.addAll(Arrays.asList(temp.getDeclaredFields()));
            MergeSuperclass marge = temp.getAnnotation(MergeSuperclass.class);
            if (marge == null)
                break;
            addFirst = marge.addFirst();
            temp = typeClass.getSuperclass();
        }

        List<java.lang.reflect.Field> result = new ArrayList<>(fs.size());
        for (java.lang.reflect.Field f : fs) {
            if (f.isAnnotationPresent(Fs.class) || f.isAnnotationPresent(Field.class)) {
                f.setAccessible(true);
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

        BasicField schema = SchemaRegistry.get(typeClass, field);
        if (schema != null) {
            for (int ver : field.version()) {
                multiVersionFields.get(ver).add(schema.build(f, field));
            }
        } else {
            Map<Integer, RuntimeSchema> schemaMap = Optional.ofNullable(getRuntimeSchema(root, ClassUtils.getGenericType(f))).orElse(Collections.EMPTY_MAP);
            for (int ver : field.version()) {
                schema = schemaMap.get(ver);
                BasicField value = SchemaRegistry.get(typeClass, field, schema);
                multiVersionFields.get(ver).add(value.build(f, field));
            }
        }
    }
}