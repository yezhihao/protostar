package io.github.yezhihao.protostar.util;

import io.github.yezhihao.protostar.annotation.Field;

public class Info {

    private final int index;
    private final Field field;
    private final Object value;
    private final String raw;
    private final boolean lengthField;

    private Info(int index, Field field, Object value, String raw, boolean lengthField) {
        this.index = index;
        this.field = field;
        this.value = value;
        this.raw = raw;
        this.lengthField = lengthField;
    }

    public static Info field(int index, Field field, Object value, String raw) {
        return new Info(index, field, value, raw, false);
    }

    public static Info lengthField(int index, Field field, int value) {
        return new Info(index, field, value, StrUtils.leftPad(Integer.toHexString(value), field.lengthSize() << 1, '0'), true);
    }

    public int getIndex() {
        return index;
    }

    public Field getField() {
        return field;
    }

    public String getRaw() {
        return raw;
    }

    public Object getValue() {
        return value;
    }

    public boolean isLengthField() {
        return lengthField;
    }
}