/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.domain.dto;

import lombok.Data;

import java.io.Serializable;


/**
* 前端配置 DTO类.
* @author mission
* @since 2019-10-15
*/
@Data
public class ShowConfigDTO implements Serializable {

    private static final long serialVersionUID = -6984851792436211225L;
    /*编号*/
    private Long id;

    /*启用*/
    private Boolean enabled;

    /*配置名*/
    private String name;

    /*配置值*/
    private String value;

}