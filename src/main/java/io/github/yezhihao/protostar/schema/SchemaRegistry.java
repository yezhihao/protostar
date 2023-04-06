package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.field.*;
import io.github.yezhihao.protostar.util.DateTool;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class SchemaRegistry {

    private static final Map<String, Function<DateTool, BasicField>> TIME_SCHEMA = new HashMap<>(6);

    private static final Map<String, Supplier<BasicField>> NO_ARGS = new HashMap<>(128);

    private static final Map<String, Integer> NUMBER = new HashMap<>(12);

    static {
        NUMBER.put(boolean.class.getName(), 1);
        NUMBER.put(char.class.getName(), 2);
        NUMBER.put(byte.class.getName(), 1);
        NUMBER.put(short.class.getName(), 2);
        NUMBER.put(int.class.getName(), 4);
        NUMBER.put(long.class.getName(), 8);
        NUMBER.put(float.class.getName(), 4);
        NUMBER.put(double.class.getName(), 8);

        NUMBER.put(Boolean.class.getName(), 1);
        NUMBER.put(Character.class.getName(), 2);
        NUMBER.put(Byte.class.getName(), 1);
        NUMBER.put(Short.class.getName(), 2);
        NUMBER.put(Integer.class.getName(), 4);
        NUMBER.put(Long.class.getName(), 8);
        NUMBER.put(Float.class.getName(), 4);
        NUMBER.put(Double.class.getName(), 8);

        register(short.class,        /**/NumberPSchema.WORD2ShortLE::new, 2, "LE");
        register(int.class,          /**/NumberPSchema.WORD2IntLE::new, 2, "LE");
        register(int.class,          /**/NumberPSchema.MEDIUM2IntLE::new, 3, "LE");
        register(int.class,          /**/NumberPSchema.DWORD2IntLE::new, 4, "LE");
        register(long.class,         /**/NumberPSchema.DWORD2LongLE::new, 4, "LE");
        register(long.class,         /**/NumberPSchema.QWORD2LongLE::new, 8, "LE");
        register(short.class,        /**/NumberPSchema.WORD2ShortLE::new, "LE");
        register(int.class,          /**/NumberPSchema.DWORD2IntLE::new, "LE");
        register(long.class,         /**/NumberPSchema.QWORD2LongLE::new, "LE");
        register(float.class,        /**/NumberPSchema.DWORD2FloatLE::new, "LE");
        register(double.class,       /**/NumberPSchema.QWORD2DoubleLE::new, "LE");
        register(byte.class,         /**/NumberPSchema.BYTE2Byte::new, 1);
        register(short.class,        /**/NumberPSchema.BYTE2Short::new, 1);
        register(int.class,          /**/NumberPSchema.BYTE2Int::new, 1);
        register(short.class,        /**/NumberPSchema.WORD2Short::new, 2);
        register(int.class,          /**/NumberPSchema.WORD2Int::new, 2);
        register(int.class,          /**/NumberPSchema.MEDIUM2Int::new, 3);
        register(int.class,          /**/NumberPSchema.DWORD2Int::new, 4);
        register(long.class,         /**/NumberPSchema.DWORD2Long::new, 4);
        register(long.class,         /**/NumberPSchema.QWORD2Long::new, 8);
        register(boolean.class,      /**/NumberPSchema.BOOL::new);
        register(char.class,         /**/NumberPSchema.CHAR::new);
        register(byte.class,         /**/NumberPSchema.BYTE2Byte::new);
        register(short.class,        /**/NumberPSchema.WORD2Short::new);
        register(int.class,          /**/NumberPSchema.DWORD2Int::new);
        register(long.class,         /**/NumberPSchema.QWORD2Long::new);
        register(float.class,        /**/NumberPSchema.DWORD2Float::new);
        register(double.class,       /**/NumberPSchema.QWORD2Double::new);

        register(Short.class,        /**/NumberSchema.WORD2ShortLE::new, 2, "LE");
        register(Integer.class,      /**/NumberSchema.WORD2IntLE::new, 2, "LE");
        register(Integer.class,      /**/NumberSchema.MEDIUM2IntLE::new, 3, "LE");
        register(Integer.class,      /**/NumberSchema.DWORD2IntLE::new, 4, "LE");
        register(Long.class,         /**/NumberSchema.DWORD2LongLE::new, 4, "LE");
        register(Long.class,         /**/NumberSchema.QWORD2LongLE::new, 8, "LE");
        register(Short.class,        /**/NumberSchema.WORD2ShortLE::new, "LE");
        register(Integer.class,      /**/NumberSchema.DWORD2IntLE::new, "LE");
        register(Long.class,         /**/NumberSchema.QWORD2LongLE::new, "LE");
        register(Float.class,        /**/NumberSchema.DWORD2FloatLE::new, "LE");
        register(Double.class,       /**/NumberSchema.QWORD2DoubleLE::new, "LE");
        register(Byte.class,         /**/NumberSchema.BYTE2Byte::new, 1);
        register(Short.class,        /**/NumberSchema.BYTE2Short::new, 1);
        register(Integer.class,      /**/NumberSchema.BYTE2Int::new, 1);
        register(Short.class,        /**/NumberSchema.WORD2Short::new, 2);
        register(Integer.class,      /**/NumberSchema.WORD2Int::new, 2);
        register(Integer.class,      /**/NumberSchema.MEDIUM2Int::new, 3);
        register(Integer.class,      /**/NumberSchema.DWORD2Int::new, 4);
        register(Long.class,         /**/NumberSchema.DWORD2Long::new, 4);
        register(Long.class,         /**/NumberSchema.QWORD2Long::new, 8);
        register(Boolean.class,      /**/NumberSchema.BOOL::new);
        register(Character.class,    /**/NumberSchema.CHAR::new);
        register(Byte.class,         /**/NumberSchema.BYTE2Byte::new);
        register(Short.class,        /**/NumberSchema.WORD2Short::new);
        register(Integer.class,      /**/NumberSchema.DWORD2Int::new);
        register(Long.class,         /**/NumberSchema.QWORD2Long::new);
        register(Float.class,        /**/NumberSchema.DWORD2Float::new);
        register(Double.class,       /**/NumberSchema.QWORD2Double::new);

        register(byte[].class,       /**/ArraySchema.ByteArray::new);
        register(char[].class,       /**/ArraySchema.CharArray::new);
        register(short[].class,      /**/ArraySchema.ShortArray::new);
        register(int[].class,        /**/ArraySchema.IntArray::new);
        register(long[].class,       /**/ArraySchema.LongArray::new);
        register(float[].class,      /**/ArraySchema.FloatArray::new);
        register(double[].class,     /**/ArraySchema.DoubleArray::new);
        register(ByteBuffer.class,   /**/BufferSchema.ByteBufferSchema::new);
        register(ByteBuf.class,      /**/BufferSchema.ByteBufSchema::new);

        register(LocalTime.class,    /**/DateTimeSchema.Time2::new, 2);
        register(LocalTime.class,    /**/DateTimeSchema.Time3::new);
        register(LocalTime.class,    /**/DateTimeSchema.Time3::new, 3);
        register(LocalDate.class,    /**/DateTimeSchema.Date3::new);
        register(LocalDate.class,    /**/DateTimeSchema.Date3::new, 3);
        register(LocalDate.class,    /**/DateTimeSchema.Date4::new, 4);
        register(LocalDateTime.class,/**/DateTimeSchema.DateTime6::new);
        register(LocalDateTime.class,/**/DateTimeSchema.DateTime6::new, 6);
        register(LocalDateTime.class,/**/DateTimeSchema.DateTime7::new, 7);
    }

    public static void register(Class typeClass, Supplier<BasicField> supplier, int length, String charset) {
        NO_ARGS.put(typeClass.getName() + "/" + length + "/" + charset, supplier);
    }

    public static void register(Class typeClass, Supplier<BasicField> supplier, int length) {
        NO_ARGS.put(typeClass.getName() + "/" + length, supplier);
    }

    public static void register(Class typeClass, Supplier<BasicField> supplier, String charset) {
        NO_ARGS.put(typeClass.getName() + "/" + charset, supplier);
    }

    public static void register(Class typeClass, Supplier schema) {
        NO_ARGS.put(typeClass.getName(), schema);
    }

    public static void register(Class typeClass, Function<DateTool, BasicField> field) {
        TIME_SCHEMA.put(typeClass.getName(), field);
    }

    public static void register(Class typeClass, Function<DateTool, BasicField> field, int length) {
        TIME_SCHEMA.put(typeClass.getName() + "/" + length, field);
    }

    public static Schema getCustom(Class<? extends Schema> clazz) {
        try {
            return clazz.getDeclaredConstructor((Class[]) null).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static BasicField get(Field field, java.lang.reflect.Field f) {
        Class typeClass = f.getType();
        String name = typeClass.getName();
        String charset = field.charset().toUpperCase();
        int length = field.length();
        BasicField schema = null;

        if (NUMBER.containsKey(name)) {
            if (length > 0)
                name += "/" + length;
            if (charset.equals("LE"))
                name += "/LE";
            schema = NO_ARGS.get(name).get();
        } else if (String.class.isAssignableFrom(typeClass)) {
            schema = StringSchema.getInstance(charset, length, field.lengthUnit());
        } else if (Temporal.class.isAssignableFrom(typeClass)) {
            if (length > 0)
                name += "/" + length;
            schema = TIME_SCHEMA.get(name).apply(charset.equals("BCD") ? DateTool.BCD : DateTool.BYTE);
        } else if (Schema.class != field.converter()) {
            schema = get(field, f, getCustom(field.converter()));
        } else {
            Supplier<BasicField> supplier = NO_ARGS.get(name);
            if (supplier != null) {
                schema = get(field, f, supplier.get());
            }
        }

        if (!field.lengthExpression().isEmpty()) {
            return new ExpressionLengthField(schema, field.lengthExpression());
        }
        return schema;
    }

    public static BasicField get(Field field, java.lang.reflect.Field f, Schema schema) {
        Class typeClass = f.getType();
        if (field.totalUnit() > 0) {
            if (Collection.class.isAssignableFrom(typeClass)) {
                return new TotalCollectionField(schema, field.totalUnit());
            }
            if (Map.class.isAssignableFrom(typeClass)) {
                return new TotalMapField((MapSchema) schema, field.totalUnit(), typeClass);
            }
            if (typeClass.isArray()) {
                typeClass = typeClass.getComponentType();
                if (typeClass.isPrimitive())
                    return new TotalArrayPrimitiveField(schema, field.totalUnit(), typeClass);
                return new TotalArrayObjectField(schema, field.totalUnit(), typeClass);
            }
        }

        if (field.lengthUnit() > 0) {
            if (Collection.class.isAssignableFrom(typeClass))
                return new LengthUnitCollectionField(schema, field.lengthUnit());
            return new LengthUnitField(schema, field.lengthUnit());
        }

        if (field.length() > 0) {
            return new LengthField(schema, field.length());
        }
        if (Collection.class.isAssignableFrom(typeClass)) {
            return new CollectionField(schema);
        } else if (Map.class.isAssignableFrom(typeClass)) {
            return new MapField((MapSchema) schema, typeClass);
        }
        return (BasicField) schema;
    }

    public static int getLength(Class typeClass) {
        Integer len = NUMBER.get(typeClass.getName());
        if (len == null)
            return -1;
        return len;
    }
}