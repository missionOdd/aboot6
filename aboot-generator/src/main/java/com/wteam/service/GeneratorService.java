/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.service;

import com.wteam.domain.ColumnInfo;
import com.wteam.domain.GenConfig;
import com.wteam.domain.vo.TableInfo;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 代码生成器 业务层.
 * @author mission
 * @since 2019/07/16 8:40
 */
public interface GeneratorService {


    /**
     * 获取所有table
     * @return /
     */
    List<TableInfo> getTables();

    /**
     * 查询数据库元数据
     * @param name
     * @param startEnd
     * @return
     */
    Object getTables(String name, int[] startEnd);

    /**
     * 得到数据表的元数据
     * @param tableName 表名
     * @return
     */
    List<ColumnInfo> getColumns(String tableName);

    /**
     * 查询数据库的表字段数据数据
     * @param tableName /
     * @return /
     */
    List<ColumnInfo> query(String tableName);

    /**
     * 同步表数据
     * @param columnInfosOfOld /
     * @param columnInfosOfNew /
     */
    @Async
    void sync(List<ColumnInfo> columnInfosOfOld, List<ColumnInfo> columnInfosOfNew);


    /**
     * 保存数据
     * @param columnInfos /
     */
    void save(List<ColumnInfo> columnInfos);

    /**
     * 代码生成
     * @param genConfig 配置信息
     * @param columns 字段信息
     */
    void generator(GenConfig genConfig, List<ColumnInfo> columns);

    /**
     * 预览
     * @param genConfig 配置信息
     * @param columns 字段信息
     * @return /
     */
    Object preview(GenConfig genConfig, List<ColumnInfo> columns);

    /**
     * 打包下载
     * @param genConfig 配置信息
     * @param columns 字段信息
     * @param request
     * @param response
     */
    void download(GenConfig genConfig, List<ColumnInfo> columns, HttpServletRequest request, HttpServletResponse response);

}