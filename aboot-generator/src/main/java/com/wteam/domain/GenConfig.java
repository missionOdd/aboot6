/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.domain;


import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 生成代码配置
 * @author mission
 * @since 2019/07/16 8:34
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name = "gen_config")
public class GenConfig extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 表名
     */
    @NotBlank
    private String tableName;


    /**
     * 表注备名
     */
    private String tableComment;

    /**
     *  接口名称
     **/
    private String apiAlias;

    /**
     * 包路径
     */
    private String pack;

    /**
     * 模块名
     */
    @Column(name = "module_name")
    private String moduleName;

    /**
     * 前端文件路径
     */
    private String path;

    /**
     * 文件路径
     */
    private String apiPath;

    /**
     * 作者
     */
    private String author;

    /**
     * 表前缀
     */
    private String prefix;

    /**
     * 是否覆盖
     */
    private Boolean cover;


    public GenConfig(String tableName,String apiAlias) {
        this.cover = false;
        this.moduleName = "aboot-system";
        this.tableName = tableName;
        this.apiAlias = apiAlias;
    }

    /**
     * 代码模板
     */
    @Transient
    private List<GenTemplate> genTemplates;
}
