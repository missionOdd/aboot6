/*
 * copyleft © 2019-2021
 */

package com.wteam.modules.pay.domain.criteria;


import com.wteam.annotation.Query;
import lombok.Data;

/**
* 微信支付配置 搜索类.
* @author aboot-wechat
* @since 2020-02-07
*/
@Data
public class WxPayConfigQueryCriteria{
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String appid;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String mchId;
}