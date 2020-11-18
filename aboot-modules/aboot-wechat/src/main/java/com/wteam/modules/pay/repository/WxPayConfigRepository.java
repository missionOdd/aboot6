/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.pay.repository;


import com.wteam.base.BaseRepository;
import com.wteam.modules.pay.domain.WxPayConfig;

/**
* 微信支付配置 存储层.
* @author mission
* @since 2020-02-07
*/
public interface WxPayConfigRepository extends BaseRepository<WxPayConfig, Long> {

    /**
     * 根据appid查询
     * @param appid
     * @return
     */
    WxPayConfig findByAppid(String appid);
}