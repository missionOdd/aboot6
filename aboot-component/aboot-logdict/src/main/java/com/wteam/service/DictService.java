/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.service;

import com.wteam.domain.Dict;
import com.wteam.domain.criteria.DictQueryCriteria;
import com.wteam.domain.dto.DictDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 字典 业务层
 * @author mission
 * @since 2019/07/13 11:03
 */
public interface DictService {

    /**
     * 查询
     * @param dict /
     * @param pageable /
     * @return /
     */
    Object queryAll(DictDTO dict, Pageable pageable);

    /**
     * 查询
     * @return /
     */
    Map<String,Object> queryMap(DictQueryCriteria criteria);

    /**
     * 查询全部数据
     * @param criteria /
     * @return /
     */
    List<DictDTO> queryAll(DictQueryCriteria criteria);

    /**
     * findById
     * @param id /
     * @return
     */
    DictDTO findDTOById(Long id);

    /**
     * create
     * @param resources /
     * @return
     */
    DictDTO create(Dict resources);

    /**
     * update
     * @param resources /
     */
    void update(Dict resources);

    /**
     * delete
     * @param ids /
     */
    void delete(Long[] ids);

    /**
     * 导出数据
     * @param queryAll /
     * @param response /
     */
    void download(List<DictDTO> queryAll, HttpServletResponse response) throws IOException;
}