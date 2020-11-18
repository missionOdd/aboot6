/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 数据组 持久类.
* @author mission
* @since 2020-03-23
*/
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name="tool_group_data")
public class GroupData extends BaseEntity{

    public final static String ENTITY_NAME ="数据组";

    /** ID */
    @ApiModelProperty( "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /** 对应的数据名称 */
    @ApiModelProperty( "对应的数据名称")
    @Column(name = "group_name",nullable = false)
    @NotBlank
    private String groupName;
    /** 数据组对应的数据值（json数据） */
    @ApiModelProperty( "数据组对应的数据值（json数据）")
    @Column(name = "value",nullable = false)
    @NotBlank
    private String value;
    /** 排序字段 */
    @ApiModelProperty( "排序字段")
    @Column(name = "sort")
    private Integer sort;
    /** 状态（1：开启；2：关闭；） */
    @ApiModelProperty( "状态（1：开启；2：关闭；）")
    @Column(name = "enabled",nullable = false)
    @NotNull
    private Boolean enabled;

    public void copy(GroupData source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}