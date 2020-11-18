/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.monitor.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 缓存 可视化
 * @author mission
 * @since 2019/07/11 20:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisVo implements Serializable {

    private static final long serialVersionUID = -7534143788063468036L;

    @NotBlank
    private String key;

    @NotBlank
    private String value;

}
