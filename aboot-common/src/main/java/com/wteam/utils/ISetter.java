/*
 * copyleft © 2019-2021
 */
package com.wteam.utils;

import java.io.Serializable;


/**
 * setter方法接口定义
 * @author mission
 * @since 2020/07/29 17:29
 */
@FunctionalInterface
public interface ISetter<T, U> extends Serializable {
    void accept(T t, U u);
}