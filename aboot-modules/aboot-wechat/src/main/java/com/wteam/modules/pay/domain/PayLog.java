/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.pay.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
/**
* 交易日志表 持久类.
* @author mission
* @since 2020-03-18
*/
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name="pay_log")
@org.hibernate.annotations.Table(appliesTo = "pay_log",comment = "交易日志表")
public class PayLog extends BaseEntity {

    public final static String ENTITY_NAME ="交易日志表";


    /** ID */
    @ApiModelProperty( "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** 应用id */
    @ApiModelProperty( "应用id")
    @Column(name = "app_id")
    @NotBlank(message = "应用id不能为空")
    private String appId;

    /** 应用方订单号 */
    @ApiModelProperty( "应用方订单号")
    @Column(name = "app_order_id")
    @NotBlank(message = "应用方订单号不能为空")
    private String appOrderId;

    /** 本次交易唯一id，整个支付系统唯一，生成他的原因主要是 order_id对于其它应用来说可能重复 */
    @ApiModelProperty( "本次交易唯一id，整个支付系统唯一，生成他的原因主要是 order_id对于其它应用来说可能重复")
    @Column(name = "transaction_id")
    @NotBlank(message = "本次交易唯一id，整个支付系统唯一，生成他的原因主要是 order_id对于其它应用来说可能重复不能为空")
    private String transactionId;

    /** 异常详细 */
    @ApiModelProperty( "异常详细")
    @Column(name = "exception_detail")
    private String exceptionDetail;

    /** 操作用户 */
    @ApiModelProperty( "操作用户")
    @Column(name = "username")
    private String username;

    /** 请求耗时 */
    @ApiModelProperty( "请求耗时")
    @Column(name = "time")
    private Long time;

    /** 方法名 */
    @ApiModelProperty( "方法名")
    @Column(name = "method")
    private String method;

    /** 支付的请求参数 */
    @ApiModelProperty( "支付的请求参数")
    @Column(name = "params")
    private String params;

    /** 日志类型，payment:支付; refund:退款; notify:异步通知; return:同步通知; query:查询 */
    @ApiModelProperty( "日志类型，payment:支付; refund:退款; notify:异步通知; return:同步通知; query:查询")
    @Column(name = "log_type")
    @NotBlank(message = "日志类型，payment:支付; refund:退款; notify:异步通知; return:同步通知; query:查询不能为空")
    private String logType;

    /** 请求ip */
    @ApiModelProperty( "请求ip")
    @Column(name = "request_ip")
    private String requestIp;

    /** 请求地址 */
    @ApiModelProperty( "请求地址")
    @Column(name = "address")
    private String address;

    /** 浏览器 */
    @ApiModelProperty( "浏览器")
    @Column(name = "browser")
    private String browser;

    public void copy(PayLog source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}