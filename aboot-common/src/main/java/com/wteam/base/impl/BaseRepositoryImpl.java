/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.base.impl;

import com.wteam.base.BaseRepository;
import com.wteam.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 通用Repository实现类
 * @author mission
 * @since 2019/07/26 16:28
 * @param <T> 实体类
 * @param <ID> ID
 */
@Slf4j
@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    private final EntityManager entityManager;
    private final JpaEntityInformation<T, ID> entityInformation;


    public BaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }



    /**
     * 重置自增Id
     * @author missionary
     * @since 2019/6/6 0006
     */
    @Override
    @Transactional
    public void resetId(String tableName) {
        if(entityInformation.getIdType()== Long.class){
            this.entityManager
                    .createQuery("alter table " + tableName+ " auto_increment=1")
                    .executeUpdate();
        }
    }

    /**
     * 提供默认复杂查询支持
     * @return
     */
    public SpecificationSupport<T> support(){
        return SpecificationSupport.of();
    }

    @Override
    @Transactional
    public <S extends T> S update(S entity, boolean nullCover) {
        if (!nullCover) {
            return save(entity);
        }
        T mergedEntity;
        //获取ID
        ID entityId = (ID) this.entityInformation.getId(entity);
        Assert.notNull(entityId, "The id not be null!");
        entityManager.merge(entity);
        mergedEntity = entity;
        return entity;
    }

    /**
     * 通用save方法 ：新增/选择性更新
     */
    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        //获取ID
        ID entityId = (ID) this.entityInformation.getId(entity);

        T managedEntity;
        T mergedEntity;
        if(entityId == null){
            entityManager.persist(entity);
            mergedEntity = entity;
        }else{
            managedEntity = this.findById(entityId).orElse(null);
            if (managedEntity == null) {
                entityManager.merge(entity);
                mergedEntity = entity;
            } else {
                BeanUtil.copyPropertiesNotNull(entity, managedEntity);
                entityManager.merge(managedEntity);
                mergedEntity = managedEntity;
            }
        }
        return entity;
    }


    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#saveAndFlush(java.lang.Object)
     */
    @Override
    @Transactional
    public <S extends T> S saveAndFlush(S entity) {
        S result = save(entity);
        flush();

        return result;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#save(java.lang.Iterable)
     */
    @Override
    @Transactional
    public <S extends T> List<S> saveAll(Iterable<S> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        List<S> result = new ArrayList<S>();

        for (S entity : entities) {
            result.add(save(entity));
        }

        return result;
    }



    /**
     * 执行自定义sql语句
     * @param sqls /
     */
    @Override
    @Transactional
    public void executeSql(String... sqls) {
        for (String sql : sqls) {
            if (!StringUtils.isBlank(sql)) {
                entityManager.createNativeQuery(sql).executeUpdate();
            }
        }
    }

    /**
     * 根据ids批量删除
     * @param ids /
     * @return /
     */
    @Override
    public int deleteInBatchById(Iterable<ID> ids) {
        Assert.notNull(ids, "The given Iterable of ids not be null!");

        if (!ids.iterator().hasNext()) {
            return -1;
        }
        return deleteApplyAndBind(QueryUtils.getQueryString( "delete from %s x", entityInformation.getEntityName()), ids, entityManager)
                .executeUpdate();
    }



    /**
     * 自定义多条件动态查询
     * @param likeMap  key是字段名,value为待查询的值
     * @param eqMap  key是字段名,value为待查询的值
     * @return /
     */
    @Override
    public List<T> listByMap(Map<String, Object> likeMap, Map<String, Object> eqMap) {
        Specification<T> sf = getSpecification(likeMap,eqMap);
        return findAll(sf);
    }


    /**
     * 自定义多条件动态查询
     * @param likeMap  key是字段名,value为待查询的值
     * @param eqMap  key是字段名,value为待查询的值
     * @param pageable  分页查询
     * @return /
     */
    @Override
    public Page<T> pageByMap(Map<String, Object> likeMap, Map<String, Object> eqMap, Pageable pageable) {
        Specification<T> sf = getSpecification(likeMap,eqMap);
        return findAll(sf, pageable);
    }

    /**
     *  封装条件
     * @param likeMap  key是字段名,value为待查询的值
     * @param eqMap key是字段名,value为待查询的值
     * @return /
     */
    private Specification<T> getSpecification(Map<String, Object> likeMap, Map<String, Object> eqMap){
        return (root,criteriaQuery,criteriaBuilder ) ->{
            List<Predicate> list = new ArrayList<>();
            for (String key : likeMap.keySet()) {
                if (StringUtils.isNotEmpty(key)) {
                    list.add(criteriaBuilder.like(root.get(key).as(String.class),"%"+likeMap.get(key)+"%"));
                }
            }
            for (String key : eqMap.keySet()) {
                if (StringUtils.isNotEmpty(key)) {
                    list.add(criteriaBuilder.equal(root.get(key).as(String.class),eqMap.get(key)));
                }
            }
            Predicate[] pre = new Predicate[list.size()];
            criteriaQuery.where(list.toArray(pre));
            return criteriaQuery.getRestriction();
        };
    }

    /**
     * 逻辑删除 /
     * @param id /
     */
    @Override
    @Transactional
    public Integer logicDelete(ID id) {
        String sb = String.format("UPDATE %s SET deletedAt = ?1 WHERE %s=?2",
                this.entityInformation.getEntityName(),
                Objects.requireNonNull(this.entityInformation.getIdAttribute()).getName());
        return this.entityManager
                .createQuery(sb)
                .setParameter(1,Timestamp.valueOf(LocalDateTime.now()))
                .setParameter(2,id)
                .executeUpdate();
    }


    /**
     * 批量删除
     * @param entities /
     * @return /
     */
    @Override
    @Transactional
    public int logicDeleteInBatch(Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        if (!entities.iterator().hasNext()) {
            return -1;
        }
        String queryString= QueryUtils.getQueryString("update %s x set deletedAt ="+Timestamp.valueOf(LocalDateTime.now()), entityInformation.getEntityName());

        Iterator<T> iterator = entities.iterator();

        if (!iterator.hasNext()) {
            entityManager.createQuery(queryString);
        }
        String alias = "x";
        StringBuilder builder = new StringBuilder(queryString);
        builder.append(" where");

        int i = 0;

        while (iterator.hasNext()) {

            iterator.next();

            builder.append(String.format(" %s = ?%d", alias, ++i));

            if (iterator.hasNext()) {
                builder.append(" or");
            }
        }

        Query query = entityManager.createQuery(builder.toString());

        iterator = entities.iterator();
        i = 0;

        while (iterator.hasNext()) {
            query.setParameter(++i, iterator.next());
        }
        return query.executeUpdate();
    }


    /**
     * 根据ids批量删除
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public int logicDeleteInBatchById(Iterable<ID> ids) {
        Assert.notNull(ids, "The given Iterable of ids not be null!");

        if (!ids.iterator().hasNext()) {
            return -1;
        }
        Integer result=0;
        for (ID id : ids) {
            result +=logicDelete(id);
        }
        return result;
    }

    /**
     * 批量删除处理
     */
    private Query deleteApplyAndBind(String queryString, Iterable<ID> ids, EntityManager entityManager) {
        Assert.notNull(queryString, "Querystring must not be null!");
        Assert.notNull(ids, "Iterable of ids must not be null!");
        Assert.notNull(entityManager, "EntityManager must not be null!");

        Iterator<ID> iterator = ids.iterator();

        if (!iterator.hasNext()) {
            return entityManager.createQuery(queryString);
        }

        StringBuilder builder = new StringBuilder(queryString);
        builder.append(" where");


        int i = 0;
        builder.append(String.format(" %s in (", entityInformation.getIdAttribute().getName()));
        while (iterator.hasNext()) {
            iterator.next();
            builder.append(String.format(" ?%d", ++i));
            if (iterator.hasNext()) {
                builder.append(" ,");
            }
        }
        builder.append(" )");
        log.debug(builder.toString());
        Query query = entityManager.createQuery(builder.toString());

        iterator = ids.iterator();
        i = 0;

        while (iterator.hasNext()) {
            query.setParameter(++i, iterator.next());
        }
        return query;
    }


}
