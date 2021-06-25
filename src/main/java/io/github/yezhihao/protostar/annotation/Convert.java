package io.github.yezhihao.protostar.annotation;

import io.github.yezhihao.protostar.converter.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用自定义消息转换
 * @author yezhihao
 * home https://gitee.com/yezhihao/jt808-server
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Convert {

    Class<? extends Converter> converter();

}