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

/**
 * 日志查询类
 * @author Zheng Jie
 * @since 2019-6-4 09:23:07
 */
@Data
public class LogQueryCriteria {

    @ApiModelProperty(value = "用户名,按用户名模糊查询")
    @Query(type = Query.Type.INNER_LIKE)
    private String username;

    @ApiModelProperty(value = "日志类型, 无需传入")
    @Query
    private String logType;

    @ApiModelProperty(value = "描述,按描述模糊查询")
    @Query(type = Query.Type.INNER_LIKE)
    private String description;
}
