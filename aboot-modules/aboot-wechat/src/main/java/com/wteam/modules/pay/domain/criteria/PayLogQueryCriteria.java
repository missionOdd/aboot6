/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.pay.domain.criteria;


import com.wteam.annotation.Query;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.time.LocalDateTime;

/**
* 交易日志表 搜索类.
* @author mission
* @since 2020-03-18
*/
@Data
public class PayLogQueryCriteria{

    /** 精确 */
    @ApiParam( "应用id精确查询")
    @Query
    private String appId;

    /** 精确 */
    @ApiParam( "应用方订单号精确查询")
    @Query
    private String appOrderId;

    /** 精确 */
    @ApiParam( "本次交易唯一id，整个支付系统唯一，生成他的原因主要是 order_id对于其它应用来说可能重复精确查询")
    @Query
    private String transactionId;

    /** 模糊 */
    @ApiParam( "操作用户模糊查询")
    @Query(type = Query.Type.INNER_LIKE)
    private String username;

    /** 大于等于 */
    @ApiParam( "创建时间大于等于查询")
    @Query(propName = "createdAt",type = Query.Type.GREATER_THAN)
    private LocalDateTime greatCreateTime;

    /** 小于等于 */
    @ApiParam( "创建时间小于等于查询")
    @Query(propName = "createdAt",type = Query.Type.LESS_THAN)
    private LocalDateTime lessCreateTime;
}