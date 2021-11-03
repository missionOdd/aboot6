/*
 * copyleft © 2019-2021
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