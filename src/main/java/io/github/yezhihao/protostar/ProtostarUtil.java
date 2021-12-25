package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Fs;
import io.github.yezhihao.protostar.annotation.MergeSuperclass;
import io.github.yezhihao.protostar.field.BasicField;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.schema.SchemaRegistry;
import io.github.yezhihao.protostar.util.ArrayMap;
import io.github.yezhihao.protostar.util.ClassUtils;

import java.util.*;

/**
 * 多版本Schema加载器
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class ProtostarUtil {

    private static final Map<String, ArrayMap<RuntimeSchema>> CACHE = new WeakHashMap<>();

    public static ArrayMap<RuntimeSchema> getRuntimeSchema(Class typeClass) {
        return getRuntimeSchema(CACHE, typeClass);
    }

    public static RuntimeSchema getRuntimeSchema(Class typeClass, int version) {
        ArrayMap<RuntimeSchema> schemaMap = getRuntimeSchema(CACHE, typeClass);
        if (schemaMap == null) return null;
        return schemaMap.getOrDefault(version);
    }

    public static ArrayMap<RuntimeSchema> getRuntimeSchema(Map<String, ArrayMap<RuntimeSchema>> root, final Class typeClass) {
        ArrayMap<RuntimeSchema> schemaMap = root.get(typeClass.getName());
        //不支持循环引用
        if (schemaMap != null) return schemaMap;

        List<java.lang.reflect.Field> fs = findFields(typeClass);
        if (fs.isEmpty()) return null;

        root.put(typeClass.getName(), schemaMap = new ArrayMap<>());

        Map<Integer, Set<BasicField>> multiVersionFields = findMultiVersionFields(root, fs);
        Set<BasicField> defFields = multiVersionFields.get(Integer.MAX_VALUE);
        for (Map.Entry<Integer, Set<BasicField>> entry : multiVersionFields.entrySet()) {

            Integer version = entry.getKey();
            Set<BasicField> fieldList = entry.getValue();
            if (defFields != null && !version.equals(Integer.MAX_VALUE)) {
                for (BasicField defField : defFields) {
                    if (!fieldList.contains(defField))
                        fieldList.add(defField);
                }
            }

            BasicField[] fields = fieldList.toArray(new BasicField[fieldList.size()]);
            Arrays.sort(fields);

            RuntimeSchema schema = new RuntimeSchema(typeClass, version, fields);
            schemaMap.put(version, schema);
        }
        root.put(typeClass.getName(), schemaMap.fillDefaultValue());
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

    private static Map<Integer, Set<BasicField>> findMultiVersionFields(Map<String, ArrayMap<RuntimeSchema>> root, List<java.lang.reflect.Field> fs) {
        final int size = fs.size();
        Map<Integer, Set<BasicField>> multiVersionFields = new TreeMap<Integer, Set<BasicField>>() {
            @Override
            public Set<BasicField> get(Object key) {
                Set result = super.get(key);
                if (result == null) super.put((Integer) key, result = new HashSet(size));
                return result;
            }
        };

        for (int i = 0; i < size; i++) {
            java.lang.reflect.Field f = fs.get(i);

            Field fa = f.getDeclaredAnnotation(Field.class);
            if (fa != null) {
                fillField(root, multiVersionFields, fa, f, i);
            } else {
                Field[] fas = f.getDeclaredAnnotation(Fs.class).value();
                for (int j = 0; j < fas.length; j++)
                    fillField(root, multiVersionFields, fas[j], f, i);
            }
        }
        return multiVersionFields;
    }

    private static void fillField(Map<String, ArrayMap<RuntimeSchema>> root, Map<Integer, Set<BasicField>> multiVersionFields, Field field, java.lang.reflect.Field f, int position) {
        BasicField basicField = SchemaRegistry.get(field, f);
        int[] versions = getVersions(field, ALL);
        if (basicField != null) {
            for (int ver : versions) {
                multiVersionFields.get(ver).add(basicField.init(field, f, position));
            }
        } else {
            ArrayMap<RuntimeSchema> schemaMap = getRuntimeSchema(root, ClassUtils.getGenericType(f));
            if (versions == ALL)
                versions = schemaMap.keys();
            for (int ver : versions) {
                Schema schema = schemaMap.getOrDefault(ver);
                basicField = SchemaRegistry.get(field, f, schema);
                multiVersionFields.get(ver).add(basicField.init(field, f, position));
            }
        }
    }

    private static final int[] ALL = {Integer.MAX_VALUE};

    private static int[] getVersions(Field field, int[] def) {
        int[] result = field.version();
        if (result.length == 0)
            result = def;
        return result;
    }
}