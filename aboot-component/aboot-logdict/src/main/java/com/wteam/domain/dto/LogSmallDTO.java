/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 操作日志
 * @author mission
 * @since 2019/07/26 17:43
 */
@Data
public class LogSmallDTO implements Serializable {

    /**
     * 描述
     */
    private String description;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 请求ip地址
     */
    private String address;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 请求耗时
     */
    private Long time;

    /**
     * 创建日期
     */
    private Timestamp createdAt;
}
