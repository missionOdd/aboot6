/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 字典 持久类
 * @author mission
 * @since 2019/07/08 17:35
 */
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name = "sys_dict")
public class Dict extends BaseEntity {


    public final static String ENTITY_NAME ="字典";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    @Column(name = "id")
    private Long id;

    /**
     * 字典名字
     */
    @Column(name = "name",nullable = false,unique = true)
    @NotBlank
    private String name;

    /**
     * 描述
     */
    @Column(name = "remark")
    private String remark;

    @JsonIgnore
    @OneToMany(mappedBy = "dict",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
    private List<DictDetail> dictDetails;
}
