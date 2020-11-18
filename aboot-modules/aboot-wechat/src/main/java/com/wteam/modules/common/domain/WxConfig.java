/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.modules.common.domain;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 微信配置 持久类.
* @author aboot-wechat
* @since 2020-02-07
*/
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name="wx_config")
public class WxConfig extends BaseEntity {

    public final static String ENTITY_NAME ="微信配置";

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /** 配置类型 公众号0 小程序1 */
    @Column(name = "type",nullable = false)
    @NotNull
    private Integer type;
    /** 应用ID */
    @Column(name = "appid",nullable = false)
    @NotBlank
    private String appid;
    /** 应用密钥 */
    @Column(name = "secret",nullable = false)
    @NotBlank
    private String secret;
    /** 应用Token */
    @Column(name = "token")
    private String token;
    /** 解密密钥 */
    @Column(name = "aes_key")
    private String aesKey;
    /** 返回格式 */
    @Column(name = "msg_data_format")
    private String msgDataFormat ="JSON";

    /** 页面logo*/
    @Column(name = "logo")
    private String logo;

    /** 简介*/
    @Column(name = "remark")
    private String remark;

    public void copy(WxConfig source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}