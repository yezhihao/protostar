package io.github.yezhihao.protostar.annotation;

import java.lang.annotation.*;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Message {

    int[] value() default {};

    String desc() default "";

}