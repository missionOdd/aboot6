/*
 * copyleft © 2019-2021
 */

package com.wteam.repository;


import com.wteam.domain.GenConfig;
import com.wteam.base.BaseRepository;
import org.springframework.data.domain.Sort;

/**
 * 生成代码 存储
 * @author mission
 * @since 2019/07/16 8:33
 */
public interface GenConfigRepository extends BaseRepository<GenConfig,Long> {

    /**
     * 查询表配置
     * @param tableName 表名
     * @return /
     */
    GenConfig findByTableName(String tableName);

    /**
     * 排序极值查询
     * @param sort
     * @return
     */
    GenConfig findFirstOrderBy(Sort sort);
}
