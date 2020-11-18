/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.miniapp.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
* 微信用户 DTO类.
* @author aboot-system
* @since 2020-02-06
*/
@Data
public class WxUserDTO implements Serializable {
    /** 用户ID */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long uid;

    /** 平台ID */
    private String openId;
    /** 昵称 */
    private String nickname;
    /** 头像 */
    private String avatar;
    /** 性别 */
    private Integer gender;
    /** 省份 */
    private String province;
    /** 城市 */
    private String city;
    /** 电话 */
    private String phone;
    /** 区号 */
    private String countryCode;
    /** 语言 */
    private String language;
    /** 创建时间 */
    private Timestamp createdAt;
    /** 创建人 */
    private Long createdBy;
    /** 逻辑删除 0:正常 1:已删除 */
    private Timestamp deletedAt;
    /** 修改时间 */
    private Timestamp updatedAt;
    /** 修改人 */
    private Long editor;
}