/*
 * copyleft © 2019-2021
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