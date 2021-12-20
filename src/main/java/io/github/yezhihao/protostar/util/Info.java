package io.github.yezhihao.protostar.util;

public class Info {

    protected int index;
    protected String desc;
    protected Object value;
    protected String raw;
    protected boolean lengthField;

    private Info(int index, String desc, Object value, String raw, boolean lengthField) {
        this.index = index;
        this.desc = desc;
        this.value = value;
        this.raw = raw;
        this.lengthField = lengthField;
    }

    public static Info field(int index, String desc, Object value, String raw) {
        return new Info(index, desc, value, raw, false);
    }

    public static Info lengthField(int index, String desc, int value, int lengthUnit) {
        return new Info(index, desc, value, StrUtils.leftPad(Integer.toHexString(value), 1 << lengthUnit, '0'), true);
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

    public boolean isLengthField() {
        return lengthField;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Info{");
        sb.append("desc='").append(desc).append('\'');
        sb.append(", value=").append(value);
        sb.append(", raw='").append(raw).append('\'');
        sb.append('}');
        return sb.toString();
    }
}