/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 * without the prior written consent of Whale Cloud Inc.
 *
 */
package ${package}.service.impl;

import ${package}.service.${className}Service;
import ${package}.domain.${className};
import ${package}.domain.dto.${className}DTO;
import ${package}.domain.criteria.${className}QueryCriteria;
import ${package}.domain.mapper.${className}Mapper;
import ${package}.repository.${className}Repository;
import com.wteam.exception.BadRequestException;
<#if hasUNI>
import com.wteam.exception.EntityExistException;
</#if>
<#if !auto && pkColumnType = 'Long'>
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
</#if>
<#if !auto && pkColumnType = 'String'>
import cn.hutool.core.util.IdUtil;
</#if>
import com.wteam.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.*;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * ${tableComment} 业务实现层.
 *
 * @author ${author}
 * @since ${date}
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "${changeClassName}")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ${className}ServiceImpl implements ${className}Service {

    private final ${className}Repository ${changeClassName}Repository;

    private final ${className}Mapper ${changeClassName}Mapper;

    private final RedisUtils redisUtils;

    @Override
    public Map<String, Object> queryAll(${className}QueryCriteria criteria, Pageable pageable) {
        Page<${className}> page = ${changeClassName}Repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelper.andPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(${changeClassName}Mapper::toDto));
    }

    @Override
    public List<${className}DTO> queryAll(${className}QueryCriteria criteria) {
        return ${changeClassName}Mapper.toDto(${changeClassName}Repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelper.andPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public ${className}DTO findDTOById(${pkColumnType} ${pkChangeColName}) {
        ${className} ${changeClassName} = ${changeClassName}Repository.findById(${pkChangeColName}).orElse(null);
        ValidUtil.notNull(${changeClassName}, ${className}.ENTITY_NAME, "${pkChangeColName}", ${pkChangeColName});
        return ${changeClassName}Mapper.toDto(${changeClassName});
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${className}DTO create(${className} resources) {
    <#if !auto && pkColumnType = 'Long'>
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.set${pkCapitalColName}(snowflake.nextId());
    </#if>
    <#if !auto && pkColumnType = 'String'>
        resources.set${pkCapitalColName}(IdUtil.simpleUUID());
    </#if>
    <#if columns??>
        <#list columns as column>
            <#if column.columnKey = 'UNI'>
        if(${changeClassName}Repository.findBy${column.capitalColumnName}(resources.get${column.capitalColumnName}()) != null){
           throw new EntityExistException(${className}.ENTITY_NAME,"${column.columnName}",resources.get${column.capitalColumnName}());
        }
            </#if>
        </#list>
    </#if>
        return ${changeClassName}Mapper.toDto(${changeClassName}Repository.save(resources));
    }

    @Override
    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(${className} resources) {
        ${className} ${changeClassName} = ${changeClassName}Repository.findById(resources.get${pkCapitalColName}()).orElse(null);
        ValidUtil.notNull(${changeClassName}, ${className}.ENTITY_NAME, "id", resources.get${pkCapitalColName}());

    <#if hasUNI>
        ${className} ${changeClassName}1 = null;
    </#if>
    <#if columns??>
        <#list columns as column>
            <#if column.columnKey = 'UNI'>
        ${changeClassName}1 = ${changeClassName}Repository.findBy${column.capitalColumnName}(resources.get${column.capitalColumnName}());
        if(${changeClassName}1 != null && !${changeClassName}1.get${pkCapitalColName}().equals(${changeClassName}.get${pkCapitalColName}())){
            throw new EntityExistException(${className}.ENTITY_NAME, "${column.columnName}", resources.get${column.capitalColumnName}());
        }
            </#if>
        </#list>
    </#if>
        ${changeClassName}.copy(resources);
        ${changeClassName}Repository.save(${changeClassName});
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Set<${pkColumnType}> ids) {
        redisUtils.delByKeys("${changeClassName}::id:", ids);
        ${changeClassName}Repository.logicDeleteInBatchById(ids);
    }

    @Override
    public void download(List<${className}DTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (${className}DTO ${changeClassName} : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            <#list columns as column>
            <#if
            column.changeColumnName = 'createdBy'||
            column.changeColumnName = 'deletedAt'||
            column.changeColumnName = 'updatedAt'||
            column.changeColumnName = 'updatedBy'><#else>
            <#if column.columnKey != 'PRI'>
            <#if column.remark != ''>
            map.put("${column.remark}", ${changeClassName}.get${column.capitalColumnName}());
            <#else>
            </#if>
            </#if>
            </#if>
            </#list>
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}