/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.domain.criteria;


import com.wteam.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;


/**
* 存储 搜索类.
* @author mission
* @since 2019-11-03
*/
@Data
public class LocalStorageQueryCriteria{

    // 模糊
    @Query(blurry = "name,suffix,type,operate,size")
    private String blurry;

    @ApiModelProperty("操作时间 >大于")
    @Query(propName = "createdAt",type = Query.Type.GREATER_THAN)
    private Timestamp greatTime;

    @ApiModelProperty("操作时间 <小于")
    @Query(propName = "createdAt",type = Query.Type.LESS_THAN)
    private Timestamp lessTime;
}