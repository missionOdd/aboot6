/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wteam.annotation.permission.DataScope;
import com.google.common.collect.Lists;
import com.wteam.annotation.Query;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.*;


/**
 * 装配条件
 * @author mission
 * @since 2019/07/08 9:55
 */
@Slf4j
@SuppressWarnings({"unchecked","all"})
public class QueryHelper {
    /**
     * and 条件拼接
     * @param root 实例
     * @param query 查询条件
     * @param cb 条件构造器
     */
    public static <R,Q>Predicate andPredicate(Root<R> root, Q query, CriteriaBuilder cb){
        List<Predicate> list=new ArrayList<>();
        if (query==null){
            return cb.and(list.toArray(new Predicate[0]));
        }
        toPredicate(root, query, cb,list);
        return cb.and(list.toArray(new Predicate[0]));
    }


    /**
     * or 条件拼接
     * @param root 实例
     * @param query 查询条件
     * @param cb 条件构造器
     */
    public static <R,Q>Predicate orPredicate(Root<R> root, Q query, CriteriaBuilder cb){
        List<Predicate> list=new ArrayList<>();
        if (query==null){
            return cb.or(list.toArray(new Predicate[0]));
        }
        toPredicate(root, query, cb,list);
        return cb.or(list.toArray(new Predicate[0]));
    }

    /**
     * 获取所有条件
     */
    private static <R,Q>void toPredicate(Root<R> root, Q query, CriteriaBuilder cb, List<Predicate> list){
        try {

            // 数据权限验证
            DataScope permission = query.getClass().getAnnotation(DataScope.class);
            if(permission != null){
                // 获取数据权限
                List<Long> dataScopes = SecurityUtils.getDataScope();
                if(CollectionUtil.isNotEmpty(dataScopes)){
                    if(StringUtils.isNotBlank(permission.joinName()) && StringUtils.isNotBlank(permission.fieldName())) {
                        Join join = root.join(permission.joinName(), JoinType.LEFT);
                        list.add(getExpression(permission.fieldName(),join, root).in(dataScopes));
                    } else if (StringUtils.isBlank(permission.joinName()) && StringUtils.isNotBlank(permission.fieldName())) {
                        list.add(getExpression(permission.fieldName(),null, root).in(dataScopes));
                    }
                }
            }

            //获取查询类信息
            List<Field> fields=getAllFields(query.getClass(),new ArrayList<>());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                // 设置对象的访问权限，保证对private的属性的访
                field.setAccessible(true);
                Query q= field.getAnnotation(Query.class);
                if (q!=null){
                    Object val=field.get(query);
                    if (ObjectUtil.isNull(val)||"".equals(val)){
                        field.setAccessible(accessible);
                        continue;
                    }
                    String propName=q.propName();
                    String attributeName=isBlank(propName)?field.getName():propName;
                    Class<?> fieldType=field.getType();

                    // 关联查询处理
                    Join join = getJoin(root,q.joinName(),q.join());
                    //自定义JPQL查询处理
                    if (toJPQL(root, list, field, accessible, q, val, attributeName, join)) continue;
                    //通用子查询处理
                    if (toSubQuery(root, cb, list, field, accessible, q, val, attributeName, fieldType, join)) continue;
                    //通用子查询处理
                    list.add(getPredicate(root,cb,q,join,attributeName,fieldType,val));
                }

                field.setAccessible(accessible);
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(),e);
        }
    }


    /**
     * 关联查询处理
     * @return
     */
    private static <R> Join getJoin(Root<R> root,String joinName, Query.Join joinType) {
        Join join =null;
        if (isNotEmpty(joinName)){
            String[] joinNames = joinName.split(">");
            for (String name : joinNames) {
                switch (joinType){
                    case LEFT:
                        if(isNotEmpty(join)&&isNotEmpty(name)){
                            join = join.join(name, JoinType.LEFT);
                        } else {
                            join = root.join(name, JoinType.LEFT);
                        }
                        break;
                    case RIGHT:
                        if(isNotEmpty(join)&&isNotEmpty(name)){
                            join = join.join(name, JoinType.RIGHT);
                        } else {
                            join = root.join(name, JoinType.RIGHT);
                        }
                        break;
                    case INNER:
                        if(ObjectUtil.isNotNull(join) && isNotEmpty(name)){
                            join = join.join(name, JoinType.INNER);
                        } else {
                            join = root.join(name, JoinType.INNER);
                        }
                        break;
                    default: break;
                }
            }
        }
        return join;
    }

    /**
     * JPQL查询
     */
    private static <R> boolean toJPQL(Root<R> root, List<Predicate> list, Field field, boolean accessible, Query q, Object val, String attributeName, Join join) {
        String queryJPQL = q.queryJPQL();
        if (isNotEmpty(queryJPQL)){
            try {
                javax.persistence.Query queryJP = SpringContextUtil.getBean(EntityManager.class).createQuery(queryJPQL);
                if (val instanceof Map) {
                    Map<String,Object> map =  (( Map<String,Object>) val);
                    map.forEach(queryJP::setParameter);
                }else {
                    queryJP.setParameter(1,val);
                }
                list.add(getExpression(attributeName,join,root).in(queryJP.getResultList()));
            } catch (Exception e) {
                log.error("[子查询SQL错误]"+e.getMessage(),e);
            }
            field.setAccessible(accessible);
            return true;
        }
        return false;
    }

    /**
     * 通用子查询
     */
    private static <R> boolean toSubQuery(Root<R> root, CriteriaBuilder cb, List<Predicate> list, Field field, boolean accessible, Query q, Object val, String attributeName, Class<?> fieldType, Join join) {
        Query.SubQuery sub = q.sub();
        boolean isSubQuery = !sub.value().isInstance("");
        if (isSubQuery) {
            //子查询处理
            String subPropName=isBlank(sub.propName())?field.getName():sub.propName();
            String subPropSelect=isBlank(sub.propSelect())?subPropName:sub.propSelect();

            Subquery<Comparable> subquery = cb.createQuery().subquery(Comparable.class);
            Root<?> subRoot = subquery.from(sub.value());
            Join subJoin = getJoin(subRoot, sub.joinName(),sub.join());

            subquery
                    .select(subRoot.get(subPropSelect))
                    .where(
                            cb.and(
                                    cb.equal(getExpression(attributeName,join,root),subRoot.get(subPropSelect)),
                                    getPredicate(subRoot,cb,q,subJoin,subPropName,fieldType,val)
                            )
                    );

            list.add(cb.exists(subquery));
            field.setAccessible(accessible);
            return true;
        }
        return false;
    }



    /**
     * 获取Expression
     * @return
     */
    private static <T, R> Expression<T> getExpression(String attributeName, Join join, Root<R> root) {
        if (isNotEmpty(join)) {
            return join.get(attributeName);
        } else {
            return root.get(attributeName);
        }
    }

    /**
     * 获取单个Predicate
     * @return
     */
    private static <R> Predicate getPredicate(Root<R> root,CriteriaBuilder cb,Query q,Join join, String attributeName,Class<?> fieldType,Object val){
        // 模糊多字段搜索
        String blurry = q.blurry();
        if (isNotEmpty(blurry)) {
            String[] blurrys = blurry.split(",");
            List<Predicate> orPredicate = new ArrayList<>();
            for (String s : blurrys) {
                Join blurryJoin = null;
                if (s.contains(".")) {
                    String[] split = s.split("\\.");
                    s =split[split.length-1];
                    List<String> strings = Lists.newArrayList(split);
                    for (String s1 : strings.subList(0, split.length - 1)) {
                        if(isNotEmpty(blurryJoin)){
                            blurryJoin = blurryJoin.join(s1, JoinType.LEFT);
                        } else {
                            blurryJoin = root.join(s1, JoinType.LEFT);
                        }
                    }
                }
                orPredicate.add(cb.like(getExpression(s,blurryJoin,root)
                        .as(String.class), "%" + val.toString() + "%"));
            }
            Predicate[] ps = new Predicate[orPredicate.size()];
            return  cb.or(orPredicate.toArray(ps));
        }

        //搜索拼接条件
        switch (q.type()) {
            case EQUAL:
                return cb.equal(getExpression(attributeName, join, root)
                        .as((Class<? extends Comparable>) fieldType), val);

            case NQ:
                return cb.notEqual(getExpression(attributeName, join, root)
                        .as((Class<? extends Comparable>) fieldType), val);

            case GREATER_THAN:
                return cb.greaterThanOrEqualTo(getExpression(attributeName, join, root)
                        .as((Class<? extends Comparable>) fieldType), (Comparable) val);

            case LESS_THAN:
                return cb.lessThanOrEqualTo(getExpression(attributeName, join, root)
                        .as((Class<? extends Comparable>) fieldType), (Comparable) val);

            case GREATER_THAN_NQ:
                return cb.greaterThan(getExpression(attributeName, join, root)
                        .as((Class<? extends Comparable>) fieldType), (Comparable) val);

            case LESS_THAN_NQ:
                return cb.lessThan(getExpression(attributeName, join, root)
                        .as((Class<? extends Comparable>) fieldType), (Comparable) val);

            case INNER_LIKE:
                return cb.like(getExpression(attributeName, join, root)
                        .as(String.class), "%" + val.toString() + "%");

            case LEFT_LIKE:
                return cb.like(getExpression(attributeName, join, root)
                        .as(String.class), "%" + val.toString());

            case RIGHT_LIKE:
                return cb.like(getExpression(attributeName, join, root)
                        .as(String.class), val.toString() + "%");

            case IN:
                if (CollUtil.isNotEmpty((Collection<?>) val)) {
                    return getExpression(attributeName, join, root).in((Collection<?>) val);
                }
                break;
            case NOT_IN:
                if (CollUtil.isNotEmpty((Collection<?>) val)) {
                    return cb.not(getExpression(attributeName, join, root).in((Collection<?>) val));
                }
                break;
            case NOT_NULL:
                if ((Boolean) val) {
                    return cb.isNotNull(getExpression(attributeName, join, root)
                            .as((Class<? extends Comparable>) fieldType));
                }else {
                    return cb.isNull(getExpression(attributeName, join, root)
                            .as((Class<? extends Comparable>) fieldType));
                }
            case BETWEEN:
                List<Object> between = new ArrayList<>((List<Object>)val);
                return cb.between(getExpression(attributeName, join, root).as((Class<? extends Comparable>) between.get(0).getClass()),
                        (Comparable) between.get(0), (Comparable) between.get(1));

            case PEAK:
                Subquery subQuery = cb.createQuery().subquery(Comparable.class);
                Root from = subQuery.from(getModelClass(join,root));
                switch (val.toString().toUpperCase()){
                    case "MIN": subQuery.select(cb.min(from.get(attributeName)));break;
                    case "COUNT":subQuery.select(cb.count(from.get(attributeName)));break;
                    case "AVG": subQuery.select(cb.avg(from.get(attributeName)));break;
                    case "SUM":subQuery.select(cb.sum(from.get(attributeName)));break;
                    default: subQuery.select(cb.max(from.get(attributeName)));break;
                }
                return cb.equal(
                        getExpression(attributeName, join, root).as((Class<? extends Comparable>) fieldType), subQuery);

            default: break;
        }
        return cb.and(new Predicate[0]);
    }

    private static <R> Class getModelClass(Join join, Root<R> root) {
        if (isNotEmpty(join)) {
            return join.getModel().getBindableJavaType();
        } else {
            return root.getJavaType();
        }
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    private static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }


    private static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence) {
            return StrUtil.isEmpty((CharSequence)obj);
        } else if (obj instanceof Map) {
            return MapUtil.isEmpty((Map)obj);
        } else if (obj instanceof Iterable) {
            return IterUtil.isEmpty((Iterable)obj);
        } else if (obj instanceof Iterator) {
            return IterUtil.isEmpty((Iterator)obj);
        } else {
            return ArrayUtil.isArray(obj) && ArrayUtil.isEmpty(obj);
        }
    }

    private static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }
}
