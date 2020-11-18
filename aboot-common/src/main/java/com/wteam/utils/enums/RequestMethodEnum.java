/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RequestMethodEnum
 * @author mission
 * @since 2020/07/28 11:57
 */
@Getter
@AllArgsConstructor
public enum RequestMethodEnum {

    /**
     * 搜寻 @AnonymousGetMapping
     */
    GET("GET"),

    /**
     * 搜寻 @AnonymousPostMapping
     */
    POST("POST"),

    /**
     * 搜寻 @AnonymousPutMapping
     */
    PUT("PUT"),

    /**
     * 搜寻 @AnonymousPatchMapping
     */
    PATCH("PATCH"),

    /**
     * 搜寻 @AnonymousDeleteMapping
     */
    DELETE("DELETE"),

    /**
     * 否则就是所有 Request 接口都放行
     */
    ALL("All");

    /**
     * Request 类型
     */
    private final String type;

    public static RequestMethodEnum find(String type) {
        for (RequestMethodEnum value : RequestMethodEnum.values()) {
            if (type.equals(value.getType())) {
                return value;
            }
        }
        return ALL;
    }
}
