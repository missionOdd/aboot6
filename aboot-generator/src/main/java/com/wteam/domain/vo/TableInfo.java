/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表的数据信息
 * @author mission
 * @since 2019/07/16 9:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo {

    /**
     * 表名称
     */
    private Object tableName;

    /**
     * 创建日期
     */
    private Object createdAt;

    /**
     * 数据库引擎
     */
    private Object engine;

    /**
     * 编码集
     */
    private Object coding;

    /**
     * 备注
     */
    private Object remark;
}
