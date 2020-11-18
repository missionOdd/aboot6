/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.domain;

import com.wteam.base.BaseEntity;
import com.wteam.base.BaseCons;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import javax.persistence.*;

/**
 * 日志 持久类
 * @author mission
 * @since 2019/07/08 9:34
 */
@Entity
@Getter
@Setter
@Table(name = "sys_log")
@Where(clause = BaseCons.SOFT_DELETE)
@NoArgsConstructor
public class Log extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作用户
     */
    @Column(name = "username",length = 64)
    private String username;

    /**
     * 描述
     */
    private String description;

    /**
     * 方法名
     */
    @Column(name = "method",length = 64)
    private String method;

    /**
     * 参数
     */
    @Column(columnDefinition = "text")
    private String params;

    /**
     * 日志类型
     */
    @Column(name = "log_type",length = 64)
    private String logType;

    /**
     * 请求ip
     */
    @Column(name = "request_ip",length = 64)
    private String requestIp;

    /**
     * 地址
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
     * 异常详细
     */
    @Column(name = "exception_detail", columnDefinition = "text")
    private byte[] exceptionDetail;

    public Log(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
