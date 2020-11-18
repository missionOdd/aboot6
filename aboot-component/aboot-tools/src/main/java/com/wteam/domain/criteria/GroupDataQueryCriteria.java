/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.domain.criteria;


import com.wteam.annotation.Query;
import io.swagger.annotations.ApiParam;
import lombok.Data;


/**
* 数据组 搜索类.
* @author mission
* @since 2020-03-23
*/
@Data
public class GroupDataQueryCriteria{
    /** 精确 */
    @ApiParam( "对应的数据名称精确查询")
    @Query
    private String groupName;
    /** 精确 */
    @ApiParam( "状态（1：开启；2：关闭；）精确查询")
    @Query
    private Boolean enabled;

}