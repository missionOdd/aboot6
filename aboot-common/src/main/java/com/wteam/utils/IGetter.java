/*
 * copyleft © 2019-2021
 */
package com.wteam.utils;

import java.io.Serializable;


/**
 * getter方法接口定义
 * @author mission
 * @since 2020/07/29 17:29
 */
@FunctionalInterface
public interface IGetter<T> extends Serializable {
    Object apply(T source);
}
