/*
 * copyleft © 2019-2021
 */

package com.wteam.repository;


import com.wteam.domain.ColumnInfo;
import com.wteam.base.BaseRepository;

import java.util.List;

/**
 * @author mission
 * @since 2019/12/29 19:57
 */
public interface ColumnInfoRepository extends BaseRepository<ColumnInfo,Long> {

    /**
     * 查询表信息
     * @param tableName 表格名
     * @return 表信息
     */
    List<ColumnInfo> findByTableNameOrderByIdAsc(String tableName);
}
