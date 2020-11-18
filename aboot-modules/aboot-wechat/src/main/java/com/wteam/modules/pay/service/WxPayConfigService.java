/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.pay.service;


import com.wteam.modules.pay.domain.WxPayConfig;
import com.wteam.modules.pay.domain.criteria.WxPayConfigQueryCriteria;
import com.wteam.modules.pay.domain.dto.WxPayConfigDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
* 微信支付配置 业务层.
* @author aboot-wechat
* @since 2020-02-07
*/
public interface WxPayConfigService{

   /**
    * 查询数据分页
    * @param criteria
    * @param pageable
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(WxPayConfigQueryCriteria criteria, Pageable pageable);

   /**
    * 查询所有数据不分页
    * @param criteria
    * @return
    */
    List<WxPayConfigDTO> queryAll(WxPayConfigQueryCriteria criteria);

    /**
    * 根据ID查询
    * @param id ID
    * @return WxPayConfigDTO
    */
    WxPayConfigDTO findDTOById(Long id);

    /**
     * 根据ID查询
     * @param appid appid
     * @return WxPayConfigDTO
     */
    WxPayConfigDTO findDTOByAppid(String appid);
    /**
    * 创建
    * @param resources /
    * @return WxPayConfigDTO
    */

    WxPayConfigDTO create(WxPayConfig resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(WxPayConfig resources);

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
   void download(List<WxPayConfigDTO> queryAll, HttpServletResponse response) throws IOException;

 /**
  * 下载证书
  * @param mchPath 证书绝对路径
  * @param request /
  * @param response /
  */
    void downloadCredentials(String mchPath, HttpServletRequest request, HttpServletResponse response);
}