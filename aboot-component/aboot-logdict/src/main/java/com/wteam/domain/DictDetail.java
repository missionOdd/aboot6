/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.domain;

import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 字典详情 持久类
 * @author mission
 * @since 2019/07/08 17:39
 */
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name = "sys_dict_detail")
public class DictDetail extends BaseEntity {

    public final static String ENTITY_NAME ="字典详细";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    @Column(name = "id")
    private Long id;

    /**
     * 字典标签
     */
    @Column(nullable = false)
    private String label;

    /**
     * 字典值
     */
    @Column(nullable = false)
    private String value;

    /**
     * 排序
     */
    @Column
    private Integer sort=999;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_id")
    private Dict dict;

}
