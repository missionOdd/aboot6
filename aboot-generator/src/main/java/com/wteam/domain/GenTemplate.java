/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.wteam.base.BaseCons;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
* 生成模板信息 持久类.
* @author mission
* @since 2019-09-29
*/
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name="gen_template")
@org.hibernate.annotations.Table(appliesTo = "gen_template",comment = "生成模板信息")
public class GenTemplate {

    public final static String ENTITY_NAME ="生成模板信息";


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

            // 模板名字
        @Column(name = "name")
        private String name;

            // 类型
        @Column(name = "type")
        private Integer type;

            // 是否生成该模板
        @Column(name = "enabled",nullable = false)
        private Boolean enabled;


        public void copy(GenTemplate source){
            BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
        }
}