/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.modules.common.repository;

import com.wteam.base.BaseRepository;
import com.wteam.modules.common.domain.WxConfig;

/**
* 微信配置 存储层.
* @author mission
* @since 2020-02-06
*/
public interface WxConfigRepository extends BaseRepository<WxConfig, Long> {

    /**
     * 根据appid 查询
     * @param appid /
     * @return /
     */
    WxConfig findByAppid(String appid);
}