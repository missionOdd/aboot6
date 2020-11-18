/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.domain.dto;

import lombok.Data;

import java.io.Serializable;


/**
* 生成模板信息 DTO类.
* @author mission
* @since 2019-09-29
*/
@Data
public class GenTemplateDTO implements Serializable {

    private Long id;

    // 模板名字
    private String name;

    // 类型
    private Integer type;

    // 是否生成该模板
    private Boolean enabled;
}