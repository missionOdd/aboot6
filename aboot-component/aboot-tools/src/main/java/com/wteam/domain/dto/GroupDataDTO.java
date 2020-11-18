/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* 数据组 DTO类.
* @author mission
* @since 2020-03-23
*/
@Data
public class GroupDataDTO implements Serializable {
    /** ID */
    @ApiModelProperty( "ID")
    private Long id;
    /** 对应的数据名称 */
    @ApiModelProperty( "对应的数据名称")
    private String groupName;
    /** 数据组对应的数据值（json数据） */
    @ApiModelProperty( "数据组对应的数据值（json数据）")
    private String value;
    /** 排序字段 */
    @ApiModelProperty( "排序字段")
    private Integer sort;
    /** 状态（1：开启；2：关闭；） */
    @ApiModelProperty( "状态（1：开启；2：关闭；）")
    private Boolean enabled;

    @ApiModelProperty( "数据组对应的数据值（json数据）")
    private Object map;

    /** 创建时间 */
    @ApiModelProperty( "创建时间")
    private Timestamp createdAt;

}