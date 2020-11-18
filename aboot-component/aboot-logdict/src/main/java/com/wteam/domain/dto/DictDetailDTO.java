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
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class DictDetailDTO implements Serializable {

    private Long id;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

    /**
     * 排序
     */
    private Integer sort;
}