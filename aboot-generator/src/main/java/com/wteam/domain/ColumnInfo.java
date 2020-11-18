/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.domain;

import com.wteam.utils.GenUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

/**
 * 列的数据信息
 * @author mission
 * @since 2019/07/16 8:44
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "gen_column_info")
public class ColumnInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** 数据库名称*/
    private String tableName;

    /** 数据库字段名称 */
    private String columnName;

    /** 数据库字段类型 */
    private String columnType;

    /** 数据库字段键类型 */
    private String keyType;

    /** 字段额外的参数 */
    private String extra;

    /** 数据库字段描述 */
    private String remark;

    /** 必填 */
    private Boolean notNull;

    /** 是否在列表显示 */
    private Boolean listShow;

    /** 是否表单显示 */
    private Boolean formShow;

    /** 表单类型 */
    private String formType;

    /** 查询 1:模糊 2：精确 */
    private String queryType;

    /** 字典名称 */
    private String dictName;

    /** 日期注解 */
    private String dateAnnotation;

    public ColumnInfo(String tableName, String columnName, Boolean notNull, String columnType, String remark, String keyType, String extra) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.columnType = columnType;
        this.keyType = keyType;
        this.extra = extra;
        this.notNull = notNull;
        if(GenUtil.PK.equalsIgnoreCase(keyType) && GenUtil.EXTRA.equalsIgnoreCase(extra)){
            this.notNull = false;
        }
        this.remark = remark;
        this.listShow = true;
        this.formShow = true;
    }
}
