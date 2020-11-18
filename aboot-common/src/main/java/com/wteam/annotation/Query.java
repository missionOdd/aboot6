/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询策略注解
 * @author mission
 * @since 2019/07/07 11:34
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    /*基本对象的属性名*/
    String propName() default "";

    /*查询方式*/
    Type type() default Type.EQUAL;

    /*连接查询的属性名,如User类中的dept*/
    String joinName() default "";

    /*默认左连接*/
    Join join() default Join.LEFT;

    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开,关联查询用点指定, 如@Query(blurry = "email,username,role.name")
     * 如果是子查询，字段是属于子查询对象
     * @return
     */
    String blurry() default "";



    /**
     * 子查询
     * 例1: sub =@Query.SubQuery(value = Log.class,propSelect = "id",propName = "username")  查询规则对应type所设置
     * 例2: sub =@Query.SubQuery(queryJPQL = "SELECT id from Log where  username = :username") 若多个参数,则filed的类型为Map<String,Object>
     * @return
     */
    SubQuery sub() default @Query.SubQuery;

    /**
     * 自定义子查询,写JPQL语句，必须要设置参数
     * 若多个参数,则filed的类型为Map<String,Object>,key是参数,value是值 注意:参数类型要对应正确
     * 一旦指定使用自定义子查询，除了关联查询规则，该参数的其他规则失效
     */
    String queryJPQL() default "";


    enum Type {
        /**相等*/
        EQUAL
        /**不相等*/
        ,NQ
        /**  大于等于 */
        , GREATER_THAN
        /**  小于等于 */
        , LESS_THAN
        /**  大于 */
        , GREATER_THAN_NQ
        /**  小于 */
        , LESS_THAN_NQ
        /** 中模糊查询 */
        , INNER_LIKE
        /**  左模糊查询 */
        , LEFT_LIKE
        /**右模糊查询 */
        , RIGHT_LIKE
        /** 包含 */
        , IN
        /** 不包含 */
        , NOT_IN
        /** 空判断*/
       /* , NULL*/
        /** 不为空判断*/
        , NOT_NULL
        /** 在两者之间*/
        ,BETWEEN
        /** 极值 传入字符串 MAX MIN COUNT AVG SUM */
        ,PEAK
    }
    /**
     *  适用于简单连接查询，复杂的请自定义该注解，或者使用sql查询
     */
    enum Join {
        /* 左连接 */
        LEFT
        /* 右连接 */
        , RIGHT
        /* 内连接 */
        ,INNER
    }

    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface SubQuery{

            /**子查询对象 ,例: User.class*/
        Class<?> value() default String.class;

        /** 子查询条件属性名*/
        String propName() default "";

        /** 子查询要查的属性名*/
        String propSelect() default "";

        /*连接子查询的属性名,如User类中的dept*/
        String joinName() default "";

        /*默认左连接*/
        Join join() default Join.LEFT;

    }
}
