/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.pay.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* 交易日志表 DTO类.
* @author mission
* @since 2020-03-18
*/
@Data
public class PayLogDTO implements Serializable {

    /** ID */
    @ApiModelProperty( "ID")
    private Long id;

    /** 应用id */
    @ApiModelProperty( "应用id")
    private String appId;

    /** 应用方订单号 */
    @ApiModelProperty( "应用方订单号")
    private String appOrderId;

    /** 本次交易唯一id，整个支付系统唯一，生成他的原因主要是 order_id对于其它应用来说可能重复 */
    @ApiModelProperty( "本次交易唯一id，整个支付系统唯一，生成他的原因主要是 order_id对于其它应用来说可能重复")
    private String transactionId;

    /** 异常详细 */
    @ApiModelProperty( "异常详细")
    private String exceptionDetail;

    /** 操作用户 */
    @ApiModelProperty( "操作用户")
    private String username;

    /** 请求耗时 */
    @ApiModelProperty( "请求耗时")
    private Long time;

    /** 方法名 */
    @ApiModelProperty( "方法名")
    private String method;

    /** 支付的请求参数 */
    @ApiModelProperty( "支付的请求参数")
    private String params;

    /** 日志类型，payment:支付; refund:退款; notify:异步通知; return:同步通知; query:查询 */
    @ApiModelProperty( "日志类型，payment:支付; refund:退款; notify:异步通知; return:同步通知; query:查询")
    private String logType;

    /** 请求ip */
    @ApiModelProperty( "请求ip")
    private String requestIp;

    /** 请求地址 */
    @ApiModelProperty( "请求地址")
    private String address;

    /** 浏览器 */
    @ApiModelProperty( "浏览器")
    private String browser;

    /** 创建时间 */
    @ApiModelProperty( "创建时间")
    private Timestamp createdAt;
}