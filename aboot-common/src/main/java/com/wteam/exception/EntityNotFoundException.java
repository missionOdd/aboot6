/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.exception;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * 找不到资源统一异常
 *
 * @author mission
 * @since 2019/07/07 15:31
 */
public class EntityNotFoundException extends RuntimeException {



    public EntityNotFoundException(String entity, Object... searchParamsMap) {
        super(generateMessage(entity, toMap(String.class, String.class, searchParamsMap)));
    }


    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) +
            "不存在 " +
            JSON.toJSON(searchParams);
    }

    private static <K, V> Map<K, V> toMap(
        Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1) {
            throw new IllegalArgumentException("Invalid entries");
        }
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
            .collect(HashMap::new,
                (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                Map::putAll);
    }

}
