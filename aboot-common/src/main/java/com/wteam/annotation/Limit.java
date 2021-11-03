/*
 * copyleft © 2019-2021
 */
package com.wteam.annotation;


import com.wteam.annotation.type.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口访问限制注解
 * @author mission
 * @since 2019/07/07 11:41
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

    /* 资源名称，用于描述接口功能*/
    String name() default "";

    /* 资源 key*/
    String key() default "";

    /*key prefix*/
    String prefix() default "";

    /* 时间的单位, 秒 */
    int period();

    /* 限制访问次数*/
    int count();

    /* 限制类型*/
    LimitType limitType() default LimitType.CUSTOMER;

}
