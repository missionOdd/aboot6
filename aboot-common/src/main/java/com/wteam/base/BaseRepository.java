/*
 * copyleft © 2019-2021
 */
package com.wteam.base;

import com.wteam.base.impl.SpecificationSupport;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 通用Repository
 * @author mission
 * @since 2019/07/26 16:29
 * @param <T> 实体类
 * @param <ID> ID
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * 重置自增ID
     */
    void resetId(String tableName);

    /**
     * 构造查询条件
     */
    SpecificationSupport<T> support();

    /**
     *  更新实体
     * @param entity 待更新实体
     * @param nullCover 更新策略,是否空覆盖
     * @return
     */
    <S extends T> S update(S entity, boolean nullCover);

    /**
     * 执行自定义sql
     * @param sqls
     */
    void executeSql(String... sqls);

    /**
     * 根据ids批量删除
     * @param ids
     * @return
     */
    int deleteInBatchById(Iterable<ID> ids);


    /**
     * 自定义多条件动态查询
     * @param likeMap  key是字段名,value为待查询的值
     * @param eqMap  key是字段名,value为待查询的值
     * @return
     */
    List<T> listByMap(Map<String, Object> likeMap, Map<String, Object> eqMap);

    /**
     * 自定义多条件动态查询
     * @param likeMap  key是字段名,value为待查询的值
     * @param eqMap  key是字段名,value为待查询的值
     * @param pageable  分页查询
     * @return
     */
    Page<T> pageByMap(Map<String, Object> likeMap, Map<String, Object> eqMap, Pageable pageable);


    /**
     * 逻辑删除
     * @param id
     */
    Integer logicDelete(ID id);

    /**
     * 批量逻辑删除
     * @param entities
     */
    int logicDeleteInBatch(Iterable<T> entities);

    /**
     * 根据ids批量删除
     * @param ids
     * @return
     */
    int logicDeleteInBatchById(Iterable<ID> ids);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<T> findByIdForLock(ID id);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<T> findAllForLock();

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<T> findAllForLock(Sort var1);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<T> findAllByIdForLock(Iterable<ID> var1);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    <S extends T> List<S> findAllForLock(Example<S> var1);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    <S extends T> List<S> findAllForLock(Example<S> var1, Sort var2);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<T> findAllForLock(@Nullable Specification<T> var1);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Page<T> findAllForLock(@Nullable Specification<T> var1, Pageable var2);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<T> findAllForLock(@Nullable Specification<T> var1, Sort var2);

}
