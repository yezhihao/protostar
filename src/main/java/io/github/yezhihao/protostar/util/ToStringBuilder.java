package io.github.yezhihao.protostar.util;

import java.beans.Transient;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class ToStringBuilder {

    private static final Cache<String, Builder[]> CACHE = new Cache<>(new WeakHashMap<>());

    public static String toString(Object object) {
        return toString(null, object, true, (String[]) null);
    }

    public static String toString(Object object, boolean superclass, String... ignores) {
        return toString(null, object, superclass, ignores);
    }

    public static String toString(StringBuilder sb, Object object, boolean superclass, String... ignores) {
        Class<?> typeClass = object.getClass();
        Builder[] builders = getBuilders(typeClass, ignores);
        if (sb == null)
            sb = new StringBuilder(builders.length * 10);

        String name = typeClass.getName();
        sb.append(name, name.lastIndexOf('.') + 1, name.length());
        sb.append('{');
        try {
            if (superclass) {
                for (Builder builder : builders)
                    builder.append(sb, object);
            } else {
                for (Builder builder : builders)
                    if (!builder.superclass)
                        builder.append(sb, object);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        sb.setCharAt(sb.length() - 1, '}');
        return sb.toString();
    }

    private static Builder[] getBuilders(Class<?> typeClass, String... ignores) {
        return CACHE.get(typeClass.getName(), () -> {
            Method[] methods = typeClass.getMethods();
            ArrayList<Builder> result = new ArrayList<>(methods.length);

            for (Method method : methods) {
                String methodName = method.getName();
                if (isProperty(methodName)) {
                    String name = getName(methodName);
                    if (!"class".equals(name) && !contains(ignores, name) &&
                            method.getParameterCount() == 0 && !method.isAnnotationPresent(Transient.class))
                        result.add(new Builder(name, method, !typeClass.equals(method.getDeclaringClass())));
                }
            }

            Builder[] temp = new Builder[result.size()];
            result.toArray(temp);
            Arrays.sort(temp);
            return temp;
        });
    }

    private static boolean contains(Object[] array, Object obj) {
        if (array == null || array.length == 0 || obj == null)
            return false;
        for (Object t : array) {
            if (obj.equals(t))
                return true;
        }
        return false;
    }

    private static boolean isProperty(String methodName) {
        int nameLength = methodName.length();
        return (methodName.startsWith("get") && nameLength > 3) ||
                (methodName.startsWith("is") && nameLength > 2);
    }

    private static String getName(String methodName) {
        char[] name = methodName.toCharArray();
        if (name[0] == 'g') {
            name[3] += 32;
            return new String(name, 3, name.length - 3);
        } else {
            name[2] += 32;
            return new String(name, 2, name.length - 2);
        }
    }

    private static class Builder implements Comparable<Builder> {
        private static final BiConsumer<StringBuilder, Object> APPEND_OBJ = StringBuilder::append;
        private static final BiConsumer<StringBuilder, Object> APPEND_ARRAY = (sb, array) -> {
            sb.append('[');
            int length = Array.getLength(array);
            boolean tooLong = length > 140;
            length = tooLong ? 140 : length;
            for (int i = 0; i < length; i++)
                sb.append(Array.get(array, i)).append(',');
            if (tooLong)
                sb.append("......");
            sb.setCharAt(sb.length() - 1, ']');
        };

        public final String name;
        public final boolean superclass;
        private final Method method;
        private final BiConsumer<StringBuilder, Object> append;

        public void append(StringBuilder sb, Object obj) throws Exception {
            Object value = method.invoke(obj);
            if (value != null) {
                sb.append(name).append('=');
                append.accept(sb, value);
                sb.append(',');
            }
        }

        public Builder(String name, Method method, boolean superclass) {
            this.name = name;
            this.method = method;
            this.superclass = superclass;
            if (method.getReturnType().isArray()) {
                append = APPEND_ARRAY;
            } else {
                append = APPEND_OBJ;
            }
        }

        @Override
        public int compareTo(Builder that) {
            Class<?> thatType = that.method.getReturnType();
            if (Iterable.class.isAssignableFrom(thatType) || thatType.isArray())
                return -1;
            Class<?> thisType = this.method.getReturnType();
            if (Iterable.class.isAssignableFrom(thisType) || thisType.isArray())
                return 1;
            return 0;
        }
    }
}