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

    /** 排序,为空为代码顺序 */
    int index() default -1;

    /** 长度,为空取类型长度 */
    int length() default -1;

    /** 长度域的长度（1-4） */
    int lengthSize() default -1;

    /** 字符集 BCD、HEX、GBK、UTF-8等 */
    String charset() default "GBK";

    /** 描述 */
    String desc() default "";

    int[] version() default {-1, 0, 1};

    Class<? extends Schema> converter() default Schema.class;
}