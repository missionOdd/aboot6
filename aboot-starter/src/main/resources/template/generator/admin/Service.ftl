/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 * without the prior written consent of Whale Cloud Inc.
 *
 */
package ${package}.service;

import ${package}.domain.${className};
import ${package}.domain.dto.${className}DTO;
import ${package}.domain.criteria.${className}QueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * ${tableComment} 业务层.
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${className}Service{

    /**
     * 查询数据分页
     *
     * @param criteria 查询条件
     * @param pageable 分页
     * @return Map<String, Object> {"content": List查询结果, "totalElements": 符合查询条件的数量}
     */
    Map<String, Object> queryAll(${className}QueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 查询条件
     * @return 查询结果
     */
    List<${className}DTO> queryAll(${className}QueryCriteria criteria, Sort sort);

    /**
     * 根据ID查询
     *
     * @param ${pkChangeColName} ID
     * @return ${className}DTO 查询结果
     */
    ${className}DTO findDTOById(${pkColumnType} ${pkChangeColName});

    /**
     * 创建
     *
     * @param resources 数据
     * @return ${className}DTO 创建结果
     */
    ${className}DTO create(${className} resources);

    /**
     * 编辑
     *
     * @param resources 数据
     */
    void update(${className} resources);

    /**
     * 多选删除
     *
     * @param ids id集合
     */
    void deleteAll(Set<${pkColumnType}> ids);

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<${className}DTO> queryAll, HttpServletResponse response) throws IOException;
}