/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.service.impl;

import com.wteam.domain.ColumnInfo;
import com.wteam.domain.GenConfig;
import com.wteam.domain.vo.TableInfo;
import com.wteam.repository.ColumnInfoRepository;
import com.wteam.service.GeneratorService;
import com.wteam.utils.GenUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import com.wteam.exception.BadRequestException;
import com.wteam.utils.FileUtil;
import com.wteam.utils.PageUtil;
import com.wteam.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代码生成器 业务实现类
 * @author mission
 * @since 2019/07/16 9:08
 */
@Service
@SuppressWarnings({"unchecked","all"})
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true,rollbackFor = Exception.class)
public class GeneratorServiceImpl implements GeneratorService {

    @PersistenceContext
    private EntityManager em;

    private final ColumnInfoRepository columnInfoRepository;

    @Override
    public List<TableInfo> getTables() {
        // 使用预编译防止sql注入
        String sql = "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
                "where table_schema = (select database()) " +
                "order by create_time desc";
        Query query = em.createNativeQuery(sql);
        List<Object[]> result =query.getResultList();
        List<TableInfo> tableInfos = new ArrayList<>();
        /*领域模式转换*/
        for (Object[] obj : result) {
            tableInfos.add(new TableInfo(obj[0],obj[1],obj[2],obj[3],
                    ObjectUtil.isNotNull(obj[4])?obj[4]:"-"));
        }
        return tableInfos;
    }

    @Override
    public Object getTables(String name, int[] startEnd) {
        //使用预编译防止sql注入

        String sql = "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
            "where table_schema = (select database()) " +
            "and table_name like ? order by create_time desc";
        Query query = em.createNativeQuery(sql);
        query.setFirstResult(startEnd[0]);
        query.setMaxResults(startEnd[1]-startEnd[0]);
        query.setParameter(1, StringUtils.isNotBlank(name)?("%"+name+"%"):"%%");
        List<Object[]> result =query.getResultList();
        List<TableInfo> tableInfos = new ArrayList<>();
        /*领域模式转换*/
        for (Object[] obj : result) {
            tableInfos.add(new TableInfo(obj[0],obj[1],obj[2],obj[3],
                ObjectUtil.isNotNull(obj[4])?obj[4]:"-"));
        }
        Query query1 = em.createNativeQuery("SELECT COUNT(*) FROM information_schema.tables where table_schema = (SELECT database())");
        Object totalElements = query1.getSingleResult();
        return PageUtil.toPage(tableInfos,totalElements);
    }

    @Override
    public List<ColumnInfo> getColumns(String tableName) {
        List<ColumnInfo> columnInfos = columnInfoRepository.findByTableNameOrderByIdAsc(tableName);
        if(CollectionUtil.isNotEmpty(columnInfos)){
            return columnInfos;
        } else {
            columnInfos = query(tableName);
            return columnInfoRepository.saveAll(columnInfos);
        }
    }

    @Override
    public List<ColumnInfo> query(String tableName) {
        // 使用预编译防止sql注入
        String sql = "select column_name, is_nullable, data_type, column_comment, column_key, extra from information_schema.columns " +
                "where table_name = ? and table_schema = (select database()) order by ordinal_position";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1,tableName);
        List result = query.getResultList();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            columnInfos.add(
                    new ColumnInfo(
                            tableName,
                            arr[0].toString(),
                            "NO".equals(arr[1]),
                            arr[2].toString(),
                            ObjectUtil.isNotNull(arr[3]) ? arr[3].toString() : null,
                            ObjectUtil.isNotNull(arr[4]) ? arr[4].toString() : null,
                            ObjectUtil.isNotNull(arr[5]) ? arr[5].toString() : null)
            );
        }
        return columnInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sync(List<ColumnInfo> columnInfosOfOld, List<ColumnInfo> columnInfosOfNew) {
        //情况1: 数据库类字段改变或者新增字段
        for (ColumnInfo columnInfo : columnInfosOfNew){
            //根据字段名称查找
            List<ColumnInfo> columns = new ArrayList<ColumnInfo>(columnInfosOfOld.stream()
                    .filter(c->c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList()));
            // 如果能找到,就修改部分字段
            if (CollectionUtil.isNotEmpty(columns)) {
                ColumnInfo column =columns.get(0);
                column.setColumnType(columnInfo.getColumnType());
                column.setExtra(columnInfo.getExtra());
                column.setKeyType(columnInfo.getKeyType());
                if (StringUtils.isBlank(column.getRemark())) {
                    column.setRemark(columnInfo.getRemark());
                }
                columnInfoRepository.save(column);
            }else {
                //如果找不到,则保存新字段信息
                columnInfoRepository.save(columnInfo);
            }
        }
        //情况2: 数据库字段删除
        for (ColumnInfo columnInfo : columnInfosOfOld){
            //根据字段名称查找
            List<ColumnInfo> columns = new ArrayList<ColumnInfo>(columnInfosOfNew.stream()
                    .filter(c-> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList()));
            //如果找不到,就代表字段被删除,则需要删除该字段
            if (CollectionUtil.isEmpty(columns)) {
                columnInfoRepository.delete(columnInfo);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<ColumnInfo> columnInfos) {
        columnInfoRepository.saveAll(columnInfos);
    }


    @Override
    public void generator(GenConfig genConfig, List<ColumnInfo> columnInfos) {
        if(genConfig.getId() == null){
            throw new BadRequestException("请先配置生成器");
        }
        try {
            GenUtil.generatorCode(columnInfos,genConfig);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("生成失败，请手动处理已生成的文件");
        }
    }

    @Override
    public Object preview(GenConfig genConfig, List<ColumnInfo> columns) {
        if(genConfig.getId() == null){
            throw new BadRequestException("请先配置生成器");
        }
        List<Map<String,Object>> genList =  GenUtil.preview(columns, genConfig);
        return genList;
    }

    @Override
    public void download(GenConfig genConfig, List<ColumnInfo> columns, HttpServletRequest request, HttpServletResponse response) {
        if(genConfig.getId() == null){
            throw new BadRequestException("请先配置生成器");
        }
        try {
            File file = new File(GenUtil.download(columns, genConfig));
            String zipPath = file.getPath()  + ".zip";
            ZipUtil.zip(file.getPath(), zipPath);
            FileUtil.downloadFile(request, response, new File(zipPath), true);
        } catch (IOException e) {
            throw new BadRequestException("打包失败");
        }
    }
}
