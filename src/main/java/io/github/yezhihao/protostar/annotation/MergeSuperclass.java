package io.github.yezhihao.protostar.annotation;

import java.lang.annotation.*;

/**
 * 将父类的字段合并到子类
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MergeSuperclass {

    /** 合并父类属性到当前类的属性之前 */
    boolean addFirst() default false;
}