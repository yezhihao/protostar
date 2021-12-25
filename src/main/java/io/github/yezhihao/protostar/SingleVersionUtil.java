package io.github.yezhihao.protostar;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.field.BasicField;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.schema.SchemaRegistry;
import io.github.yezhihao.protostar.util.ClassUtils;

import java.util.*;

/**
 * 单版本Schema加载器
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public abstract class SingleVersionUtil {

    private static final Map<String, RuntimeSchema> CACHE = new WeakHashMap<>();

    public static <T> RuntimeSchema<T> getRuntimeSchema(Class<T> typeClass) {
        return getRuntimeSchema(CACHE, typeClass);
    }

    public static <T> RuntimeSchema<T> getRuntimeSchema(Map<String, RuntimeSchema> root, Class<T> typeClass) {
        RuntimeSchema<T> schema = root.get(typeClass.getName());
        //不支持循环引用
        if (schema != null) return schema;

        List<java.lang.reflect.Field> fs = findFields(typeClass);
        if (fs.isEmpty()) return null;

        List<BasicField> fieldList = findFields(root, fs);
        BasicField[] fields = fieldList.toArray(new BasicField[fieldList.size()]);
        Arrays.sort(fields);

        schema = new RuntimeSchema(typeClass, 0, fields);
        root.put(typeClass.getName(), schema);
        return schema;
    }

    private static List<java.lang.reflect.Field> findFields(Class typeClass) {
        java.lang.reflect.Field[] fields = typeClass.getDeclaredFields();
        List<java.lang.reflect.Field> result = new ArrayList<>(fields.length);

        for (java.lang.reflect.Field f : fields) {
            if (f.isAnnotationPresent(Field.class)) {
                result.add(f);
            }
        }
        return result;
    }

    private static List<BasicField> findFields(Map<String, RuntimeSchema> root, List<java.lang.reflect.Field> fs) {
        int size = fs.size();
        List<BasicField> fields = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            java.lang.reflect.Field f = fs.get(i);
            Field field = f.getDeclaredAnnotation(Field.class);
            if (field != null) {
                f.setAccessible(true);
                fillField(root, fields, field, f, i);
            }
        }
        return fields;
    }

    private static void fillField(Map<String, RuntimeSchema> root, List<BasicField> fields, Field field, java.lang.reflect.Field f, int position) {
        BasicField basicField = SchemaRegistry.get(field, f);
        if (basicField != null) {
            fields.add(basicField.init(field, f, position));
        } else {
            RuntimeSchema schema = getRuntimeSchema(root, ClassUtils.getGenericType(f));
            basicField = SchemaRegistry.get(field, f, schema);
            fields.add(basicField.init(field, f, position));
        }
    }
}