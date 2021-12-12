package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Cache;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SchemaRegistry {

    private static final Cache<String, Schema> USER_SCHEMA = new Cache<>();

    private static final Map<String, Schema> SYS_SCHEMA = new HashMap<>(128);

    private static final Set<String> NUMBER = new HashSet<>(12);

    static {
        NUMBER.add(byte.class.getName());
        NUMBER.add(Byte.class.getName());
        NUMBER.add(short.class.getName());
        NUMBER.add(Short.class.getName());
        NUMBER.add(int.class.getName());
        NUMBER.add(Integer.class.getName());
        NUMBER.add(long.class.getName());
        NUMBER.add(Long.class.getName());

        register(byte.class,         /**/NumberSchema.BYTE_BYTE, 1);
        register(Byte.class,         /**/NumberSchema.BYTE_BYTE, 1);
        register(short.class,        /**/NumberSchema.BYTE_SHORT, 1);
        register(Short.class,        /**/NumberSchema.BYTE_SHORT, 1);
        register(int.class,          /**/NumberSchema.BYTE_INT, 1);
        register(Integer.class,      /**/NumberSchema.BYTE_INT, 1);

        register(short.class,        /**/NumberSchema.WORD_SHORT, 2);
        register(Short.class,        /**/NumberSchema.WORD_SHORT, 2);
        register(int.class,          /**/NumberSchema.WORD_INT, 2);
        register(Integer.class,      /**/NumberSchema.WORD_INT, 2);

        register(int.class,          /**/NumberSchema.DWORD_INT, 4);
        register(Integer.class,      /**/NumberSchema.DWORD_INT, 4);
        register(long.class,         /**/NumberSchema.DWORD_LONG, 4);
        register(Long.class,         /**/NumberSchema.DWORD_LONG, 4);

        register(long.class,         /**/NumberSchema.QWORD_LONG, 8);
        register(Long.class,         /**/NumberSchema.QWORD_LONG, 8);

        register(byte.class,         /**/NumberSchema.BYTE_BYTE);
        register(Byte.class,         /**/NumberSchema.BYTE_BYTE);
        register(short.class,        /**/NumberSchema.WORD_SHORT);
        register(Short.class,        /**/NumberSchema.WORD_SHORT);
        register(int.class,          /**/NumberSchema.DWORD_INT);
        register(Integer.class,      /**/NumberSchema.DWORD_INT);
        register(long.class,         /**/NumberSchema.QWORD_LONG);
        register(Long.class,         /**/NumberSchema.QWORD_LONG);

        register(float.class,        /**/NumberSchema.DWORD_FLOAT);
        register(Float.class,        /**/NumberSchema.DWORD_FLOAT);
        register(double.class,       /**/NumberSchema.QWORD_DOUBLE);
        register(Double.class,       /**/NumberSchema.QWORD_DOUBLE);

        register(boolean.class,      /**/NumberSchema.BOOL);
        register(Boolean.class,      /**/NumberSchema.BOOL);
        register(char.class,         /**/NumberSchema.CHAR);
        register(Character.class,    /**/NumberSchema.CHAR);

        register(byte[].class,       /**/ArraySchema.BYTES);
        register(char[].class,       /**/ArraySchema.CHARS);
        register(short[].class,      /**/ArraySchema.SHORTS);
        register(int[].class,        /**/ArraySchema.INTS);
        register(long[].class,       /**/ArraySchema.LONGS);
        register(float[].class,      /**/ArraySchema.FLOATS);
        register(double[].class,     /**/ArraySchema.DOUBLES);

        register(LocalDateTime.class,/**/DateTimeSchema.BCD2DateTime, "BCD");
        register(LocalDateTime.class,/**/DateTimeSchema.BYTES2DateTime);
        register(LocalDate.class,    /**/DateTimeSchema.BYTES2Date);
        register(LocalTime.class,    /**/DateTimeSchema.BYTES2Time);

        register(ByteBuffer.class,   /**/ByteBufferSchema.INSTANCE);
    }

    public static void register(Class typeClass, Schema schema, int length) {
        SYS_SCHEMA.put(typeClass.getName() + "/" + length, schema);
    }

    public static void register(Class typeClass, Schema schema, String charset) {
        SYS_SCHEMA.put(typeClass.getName() + "/" + charset, schema);
    }

    public static void register(Class typeClass, Schema schema) {
        SYS_SCHEMA.put(typeClass.getName(), schema);
    }

    public static Schema getCustom(Class<? extends Schema> clazz) {
        return USER_SCHEMA.get(clazz.getName(), () -> {
            try {
                return clazz.getDeclaredConstructor((Class[]) null).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Schema get(Class typeClass, Field field) {
        if (Schema.class != field.converter())
            return getCustom(field.converter());

        String name = typeClass.getName();

        if (NUMBER.contains(name)) {
            int length = field.length();
            if (length > 0)
                name = name + "/" + length;
            return SYS_SCHEMA.get(name);
        }

        String charset = field.charset().toUpperCase();
        if (CharSequence.class.isAssignableFrom(typeClass)) {
            return StringSchema.getInstance(charset);
        }
        if (Temporal.class.isAssignableFrom(typeClass)) {
            if (charset.equals("BCD"))
                return SYS_SCHEMA.get(name + "/BCD");
            return SYS_SCHEMA.get(name);
        }
        return SYS_SCHEMA.get(name);
    }
}