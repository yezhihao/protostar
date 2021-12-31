package io.github.yezhihao.protostar.util;

public class Info {

    protected int index;
    protected String desc;
    protected Object value;
    protected String raw;

    private Info(int index, String desc, Object value, String raw) {
        this.index = index;
        this.desc = desc;
        this.value = value;
        this.raw = raw;
    }

    public static Info field(int index, String desc, Object value, String raw) {
        return new Info(index, desc, value, raw);
    }

    public static Info lengthField(int index, String desc, int value, int lengthUnit) {
        return new Info(index, desc, value, StrUtils.leftPad(Integer.toHexString(value), 1 << lengthUnit, '0'));
    }

    public int getIndex() {
        return index;
    }

    public String getDesc() {
        return desc;
    }

    public String getRaw() {
        return raw;
    }

    public Object getValue() {
        return value;
    }

    public void setLength(int length, int lengthUnit) {
        this.value = length;
        this.raw = StrUtils.leftPad(Integer.toHexString(length), 1 << lengthUnit, '0');
    }

    @Override
    public String toString() {
        if (desc == null)
            return index + "\t[" + raw + "] [" + StrUtils.toString(value) + "]";
        return index + "\t[" + raw + "] [" + StrUtils.toString(value) + "] " + desc;
    }
}