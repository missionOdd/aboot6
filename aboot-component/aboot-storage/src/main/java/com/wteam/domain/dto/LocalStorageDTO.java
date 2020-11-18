/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* 存储 DTO类.
* @author mission
* @since 2019-11-03
*/
@Data
public class LocalStorageDTO implements Serializable {

    private static final long serialVersionUID = 2319602151465408618L;

    /*存储编号*/
    private Long id;
    /*文件名*/
    private String name;
    /*上传人*/
    private String operate;
    /*文件真实名*/
    private String realName;
    /*磁盘路径*/
    @JsonIgnore
    private String path;
    /*文件大小*/
    private String size;
    /*文件后缀*/
    private String suffix;
    /*文件类型*/
    private String type;
    /*文件MD5*/
    @JsonIgnore
    private String md5;
    /*文件相对路径*/
    private String url;
    /*创建时间*/
    private Timestamp createdAt;
}