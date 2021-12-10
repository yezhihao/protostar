package io.github.yezhihao.protostar.util;

import io.github.yezhihao.protostar.annotation.Field;

public class Info {

    private int index;
    private Field field;
    private String hex;
    private Object value;
    private boolean lengthField;

    public static Info field(int index, Field field, String hex, Object value) {
        Info result = new Info();
        result.index = index;
        result.field = field;
        result.hex = hex;
        result.value = value;
        return result;
    }

    public static Info lengthField(int index, Field field, String hex, Object value) {
        Info result = field(index, field, hex, value);
        result.lengthField = true;
        return result;
    }

    public int getIndex() {
        return index;
    }

    public Field getField() {
        return field;
    }

    public String getHex() {
        return hex;
    }

    public Object getValue() {
        return value;
    }

    public boolean isLengthField() {
        return lengthField;
    }
}