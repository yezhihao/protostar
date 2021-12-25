package io.github.yezhihao.protostar.annotation;

import io.github.yezhihao.protostar.Schema;

import java.lang.annotation.*;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Repeatable(Fs.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {

    /** 排序,默认使用代码顺序 */
    int index() default 0;

    /** 长度,默认使用类型长度,-1读取剩余长度 */
    int length() default -1;

    /** 该字段的前置长度单位 1.BYTE 2.WORD 3.MEDIUM 4.DWORD */
    int lengthUnit() default -1;

    /** 该字段的前置数量单位 1.BYTE 2.WORD 3.MEDIUM 4.DWORD */
    int totalUnit() default -1;

    /** 字符集 BCD、HEX、GBK、UTF-8等 */
    String charset() default "GBK";

    /** 描述 */
    String desc() default "";

    /** 版本号,默认不区分 */
    int[] version() default {};

    /** 自定义转换器 */
    Class<? extends Schema> converter() default Schema.class;
}