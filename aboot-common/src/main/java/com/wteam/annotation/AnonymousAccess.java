/*
 * copyleft © 2019-2021
 */
package com.wteam.annotation;

import java.lang.annotation.*;

/**
 * @author mission
 *  用于标记匿名访问方法
 */
@Inherited
@Documented
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnonymousAccess {

}
