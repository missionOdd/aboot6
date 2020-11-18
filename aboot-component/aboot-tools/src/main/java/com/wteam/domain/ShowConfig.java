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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
* 前端配置 持久类.
* @author mission
* @since 2019-10-15
*/
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name="tool_show_config")
public class ShowConfig extends BaseEntity{

    public final static String ENTITY_NAME ="前端配置";


         // 编号
        @Id
        @NotNull(groups = Update.class)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

         // 配置
        @Column(name = "enabled")
        private Boolean enabled;

         // 配置名
        @Column(name = "name")
        private String name;

        // 配置值
        @Column(name = "value")
        private String value;


    public void copy(ShowConfig source){
            BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
        }
}