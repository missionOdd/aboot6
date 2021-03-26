/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 * without the prior written consent of Whale Cloud Inc.
 *
 */
package ${package}.domain;

<#if isNotNullColumns??>
import javax.validation.constraints.*;
</#if>
<#if hasTimestamp>
import java.time.*;
import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if hasDateAnnotation>
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
</#if>
import org.hibernate.annotations.Where;
import javax.persistence.*;
import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * ${tableComment} 持久类.
 *
 * @author ${author}
 * @since ${date}
 */
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name="${tableName}")
public class ${className} extends BaseEntity{

    public final static String ENTITY_NAME = "${tableComment}";

<#if columns??>
    <#list columns as column>
    <#if
     column.changeColumnName = 'createdBy'||
     column.changeColumnName = 'createdAt'||
     column.changeColumnName = 'deletedAt'||
     column.changeColumnName = 'updatedAt'||
     column.changeColumnName = 'updatedBy'><#else>
    <#if column.remark != ''>
    /** ${column.remark} */
    @ApiModelProperty("${column.remark}")
    </#if>
    <#if column.columnKey = 'PRI'>
    @Id
    <#if column.columnType = 'String'>
    @NotBlank(groups = Update.class)
    <#else>
    @NotNull(groups = Update.class)
    </#if>
    <#if auto>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    </#if>
    @Column(name = "${column.columnName}"<#if column.columnKey = 'UNI'>, unique = true</#if><#if column.istNotNull && column.columnKey != 'PRI'>, nullable = false</#if>)
    <#if column.istNotNull && column.columnKey != 'PRI'>
    <#if column.columnType = 'String'>
    @NotBlank(message = "${column.remark}不能为空", groups = Create.class)
    <#else>
    @NotNull(message = "${column.remark}不能为空", groups = Create.class)
    </#if>
    </#if>
    <#if column.dateAnnotation??>
    <#if column.dateAnnotation = 'CreationTimestamp'>
    @CreationTimestamp
    <#else>
    @UpdateTimestamp
    </#if>
    </#if>
    private ${column.columnType} ${column.changeColumnName};

    </#if>
    </#list>
</#if>
    public void copy(${className} source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}