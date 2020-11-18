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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
* 微信支付配置 持久类.
* @author aboot-wechat
* @since 2020-02-07
*/
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name="wx_pay_config")
@org.hibernate.annotations.Table(appliesTo = "wx_pay_config",comment = "微信支付配置")
public class WxPayConfig extends BaseEntity {

    public final static String ENTITY_NAME ="微信支付配置";

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /** 设置微信公众号或者小程序等的appid */
    @Column(name = "appid",nullable = false)
    @NotBlank
    private String appid;
    /** 微信支付商户号 */
    @Column(name = "mch_id",nullable = false)
    @NotBlank
    private String mchId;
    /** 微信支付商户密钥 */
    @Column(name = "mch_key")
    @NotBlank
    private String mchKey;
    /** apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定 */
    @Column(name = "mch_path",nullable = false)
    private String mchPath;
    /** 注备 */
    @Column(name = "remark")
    private String remark;

    public void copy(WxPayConfig source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}