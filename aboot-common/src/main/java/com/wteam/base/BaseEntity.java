
/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * 实体类继承类
 * @author mission
 * @since 2019/6/6 14:19
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    /**
     * 创建人
     */
    @CreatedBy
    @ApiModelProperty(hidden = true)
    @Column(nullable = false,columnDefinition = "bigint(20) default 0 comment '创建人'")
    private Long createdBy;

    /**
     * 修改人
     */
    @LastModifiedBy
    @ApiModelProperty(hidden = true)
    @Column(columnDefinition = "bigint(20) comment '修改人'")
    private Long updatedBy;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间", hidden = true)
    @Column(nullable = false, columnDefinition = "timestamp not null default CURRENT_TIMESTAMP comment '创建时间'")
    private Timestamp createdAt;

    /**
     * 修改时间
     */
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间", hidden = true)
    @Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '修改时间'")
    private Timestamp updatedAt;


    /**
     * 逻辑删除时间
     */
    @JsonIgnore
    @JSONField(serialize=false)
    @ApiModelProperty(value = "逻辑删除时间", hidden = true)
    @Column(columnDefinition = "timestamp null comment '逻辑删除时间'")
    private Timestamp deletedAt;

    /* 分组校验 */
    public @interface Create {}

    /* 分组校验 */
    public @interface Update {}

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        return builder.toString();
    }
}
