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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
* 存储 持久类.
* @author mission
* @since 2019-11-03
*/
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name="tool_local_storage")
@NoArgsConstructor
public class LocalStorage extends BaseEntity{

    public final static String ENTITY_NAME ="存储";

    @Id
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("上传人")
    private String operate;

    @ApiModelProperty("文件真实名")
    private String realName;

    @ApiModelProperty("磁盘路径")
    private String path;

    @ApiModelProperty("文件大小")
    private String size;

    @ApiModelProperty("suffix")
    private String suffix;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("文件MD5")
    private String md5;

    @ApiModelProperty("文件相对路径")
    private String url;


    public LocalStorage(String name, String operate, String realName, String path, String size, String suffix, String type, String md5, String url) {
        this.name = name;
        this.operate = operate;
        this.realName = realName;
        this.path = path;
        this.size = size;
        this.suffix = suffix;
        this.type = type;
        this.md5 = md5;
        this.url = url;
    }

    public void copy(LocalStorage source){
            BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
        }
}