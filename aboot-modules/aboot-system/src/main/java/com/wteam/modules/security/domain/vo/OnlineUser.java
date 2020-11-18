/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.security.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 在线用户
 * @author mission
 * @since 2019/11/02 16:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser implements Serializable {

  //登录名
  private String username;


  //用户昵称
  private String nickname;

  //登录部门
  private String job;

  //登录浏览器
  private String browser;

  //登录系统
  private String os;

  //登录ip
  private String ip;

  //登录地址
  private String address;

  //加密token
  private String key;

  //登录时间
  private Timestamp loginTime;
}
