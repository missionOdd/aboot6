/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.miniapp.domain;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信用户
 * @author mission
 * @since 2020/02/03 9:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name = "wx_user")
@NoArgsConstructor
public class WxUser extends BaseEntity {

    public final static String ENTITY_NAME ="顾客";

    /*与用户id同值*/
    @Id
    @Column(name = "uid",nullable = false)
    private Long uid;

    /*openId*/
    @Column(name = "openid",nullable = false)
    private String openId;

    /*appid*/
    @Column(name = "appid",nullable = false)
    private String appId;
    /*昵称*/
    @Column(name = "nickname")
    private  String nickName;

    /*手机号*/
    @Column(name = "phone")
    private  String phoneNumber;

    /*区号*/
    @Column(name = "country_code")
    private  String countryCode;

    /*区号*/
    @Column(name = "gender")
    private  Integer gender;

    /*头像*/
    @Column(name = "avatar")
    private  String avatarUrl;

    /*省份*/
    @Column(name = "province")
    private  String province;

    /*城市*/
    @Column(name = "city")
    private  String city;

    /*语言*/
    @Column(name = "language")
    private  String language;

    public void copy(WxUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));;
    }

    public void copy(WxMaUserInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
        this.gender=Integer.valueOf(source.getGender());
    }

    public void copy(WxMaPhoneNumberInfo source){
        this.phoneNumber=source.getPhoneNumber();
        this.countryCode=source.getCountryCode();
    }
}
