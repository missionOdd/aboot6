/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.service;

import com.wteam.domain.GroupData;
import com.wteam.domain.criteria.GroupDataQueryCriteria;
import com.wteam.domain.dto.GroupDataDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* 数据组 业务层.
* @author mission
* @since 2020-03-23
*/
public interface GroupDataService{

   /**
    * 查询数据分页
    * @param criteria /
    * @param pageable /
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(GroupDataQueryCriteria criteria, Pageable pageable);

   /**
    * 查询所有数据不分页
    * @param criteria /
    * @return
    */
    List<GroupDataDTO> queryAll(GroupDataQueryCriteria criteria);

    /**
    * 根据ID查询
    * @param id ID
    * @return GroupDataDTO
    */
    GroupDataDTO findDTOById(Long id);

    /**
    * 创建
    * @param resources /
    * @return GroupDataDTO
    */
    GroupDataDTO create(GroupData resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(GroupData resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

   /**
   * 导出数据
   * @param queryAll 待导出的数据
   * @param response /
   * @throws IOException /
   */
   void download(List<GroupDataDTO> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 根据名字查询
     * @param name /
     * @return /
     */
    GroupDataDTO findDTOByName(String name);
}