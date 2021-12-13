package io.github.yezhihao.protostar.convert;

import io.github.yezhihao.protostar.annotation.Field;

public class Attr1 {

    @Field(lengthUnit = 1, desc = "名称")
    private String name;
    @Field(length = 2, desc = "ID")
    private int id;

    public Attr1() {
    }

    public Attr1(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Attr1{");
        sb.append("name='").append(name).append('\'');
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
