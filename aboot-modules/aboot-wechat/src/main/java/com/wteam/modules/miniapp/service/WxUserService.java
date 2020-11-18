/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.miniapp.service;

import com.wteam.modules.miniapp.domain.WxUser;
import com.wteam.modules.miniapp.domain.criteria.WxUserQueryCriteria;
import com.wteam.modules.miniapp.domain.dto.WxLoginDTO;
import com.wteam.modules.miniapp.domain.dto.WxUserDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 微信用户 业务层.
 * @author aboot-system
 * @since 2020-02-06
 */
public interface WxUserService{

    /**
     * 查询数据分页
     * @param criteria
     * @param pageable
     * @return Map<String,Object>
     */
    Map<String,Object> queryAll(WxUserQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria
     * @return
     */
    List<WxUserDTO> queryAll(WxUserQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param uid ID
     * @return WxUserDTO
     */
    WxUserDTO findDTOById(Long uid);

    /**
     * 创建
     * @param resources /
     * @return WxUserDTO
     */
    WxUserDTO create(WxUser resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(WxUser resources);

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
    void download(List<WxUserDTO> queryAll, HttpServletResponse response) throws IOException;


    /**
     * 解密并获取微信用户
     * @param wxLoginDTO
     * @return
     */
    WxUser load(WxLoginDTO wxLoginDTO);

    /**
     * 检查是否注册
     * @param wxLoginDTO
     * @return
     */
    WxUser checkWxReg(WxLoginDTO wxLoginDTO);
    /**
     * 根据openId获取微信用户
     * @param openid
     * @return
     */
    WxUser findByOpenId(String openid);
}
