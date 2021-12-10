package io.github.yezhihao.protostar.annotation;

import java.lang.annotation.*;

/**
 * 合并父类属性
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MergeSuperclass {
}