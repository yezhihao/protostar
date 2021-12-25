package io.github.yezhihao.protostar.utiil;

import io.github.yezhihao.protostar.util.ArrayMap;

import java.util.Arrays;

public class ArrayMapTest {

    public static void main(String[] args) {
        ArrayMap a = new ArrayMap();
        a.put(-1, "-1");
        a.defaultValue("def");
        a.put(0, "0");
        a.put(1, "1");
        a.put(9, "9");
        System.out.println(a.values());

        a = new ArrayMap();
        a.put(-1, "-1");
        a.put(9, "9");
        a.defaultValue("def");
        a.put(1, "1");
        a.put(0, "0");
        System.out.println(a.values());

        a = new ArrayMap();
        a.defaultValue("def");
        a.put(0, "0");
        a.put(9, "9");
        a.put(1, "1");
        a.put(-1, "-1");
        System.out.println(a.values());

        a = new ArrayMap();
        a.put(0, "0");
        a.put(-1, "-1");
        a.put(9, "9");
        a.defaultValue("def");
        a.put(1, "1");
        System.out.println(a.values());

        a = new ArrayMap();
        a.put(1, "1");
        a.defaultValue("def");
        a.put(9, "9");
        a.put(0, "0");
        a.put(-1, "-1");
        System.out.println(a.values());

        a = new ArrayMap();
        a.put(1, "1");
        a.put(-1, "-1");
        a.defaultValue("def");
        a.put(9, "9");
        a.put(0, "0");
        System.out.println(a.values());

        a = new ArrayMap();
        a.put(9, "9");
        a.put(1, "1");
        a.put(0, "0");
        a.defaultValue("def");
        a.put(-1, "-1");
        System.out.println(a.values());

        a = new ArrayMap();
        a.put(9, "9");
        a.defaultValue("def");
        a.put(-1, "-1");
        a.put(1, "1");
        a.put(0, "0");
        System.out.println(a.values());
        System.out.println(Arrays.toString(a.keys()));
    }
}