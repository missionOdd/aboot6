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
     * @param criteria /
     * @param pageable /
     * @return Map<String,Object> /
     */
    Map<String,Object> queryAll(${className}QueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria /
     * @return /
     */
    List<${className}DTO> queryAll(${className}QueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param ${pkChangeColName} ID /
     * @return ${className}DTO /
     */
    ${className}DTO findDTOById(${pkColumnType} ${pkChangeColName});

    /**
     * 创建
     *
     * @param resources /
     * @return ${className}DTO /
     */
    ${className}DTO create(${className} resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(${className} resources);

    /**
     * 多选删除
     *
     * @param ids /
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