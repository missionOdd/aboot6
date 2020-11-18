/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.pay.service;


import com.wteam.modules.pay.domain.PayLog;
import com.wteam.modules.pay.domain.criteria.PayLogQueryCriteria;
import com.wteam.modules.pay.domain.dto.PayLogDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
* 交易日志表 业务层.
* @author mission
* @since 2020-03-18
*/
public interface PayLogService{

   /**
    * 查询数据分页
    * @param criteria
    * @param pageable
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(PayLogQueryCriteria criteria, Pageable pageable);

   /**
    * 查询所有数据不分页
    * @param criteria
    * @return
    */
    List<PayLogDTO> queryAll(PayLogQueryCriteria criteria);

    /**
    * 根据ID查询
    * @param id ID
    * @return PayLogDTO
    */
    PayLogDTO findDTOById(Long id);

    /**
    * 创建
    * @param resources /
    * @return PayLogDTO
    */
    PayLogDTO create(PayLog resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(PayLog resources);

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
   void download(List<PayLogDTO> queryAll, HttpServletResponse response) throws IOException;
}