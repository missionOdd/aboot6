/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 * without the prior written consent of Whale Cloud Inc.
 *
 */
package ${package}.domain.dto;

import lombok.Data;
<#if hasTimestamp>
import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
import java.io.Serializable;
<#if !auto && pkColumnType = 'Long'>
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
</#if>
import io.swagger.annotations.ApiModelProperty;

/**
 * ${tableComment} DTO类.
 *
 * @author ${author}
 * @since ${date}
 */
@Data
public class ${className}DTO implements Serializable {
<#if columns??>
    <#list columns as column>
    <#if
    column.changeColumnName = 'deletedAt'||
    column.changeColumnName = 'updatedAt'||
    column.changeColumnName = 'updatedBy'><#else>
    <#if column.remark != ''>

    /** ${column.remark} */
    @ApiModelProperty("${column.remark}")
    </#if>
    <#if column.columnKey = 'PRI'>
    <#if !auto && pkColumnType = 'Long'>
    /** 防止精度丢失 */
    @JsonSerialize(using = ToStringSerializer.class)
    </#if>
    </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#if>
    </#list>
</#if>
}