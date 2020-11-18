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
 * 重复索引统一异常
 *
 * @author mission
 * @since 2019/07/07 15:31
 */
public class EntityExistException extends RuntimeException {



    public EntityExistException(String entity, Object... saveBodyParamsMap) {
        super(generateMessage(entity, toMap(String.class, String.class, saveBodyParamsMap)));
    }


    private static String generateMessage(String entity, Map<String, String> saveBodyParams) {
        return StringUtils.capitalize(entity) +
            "已存在 " +
            JSON.toJSON(saveBodyParams);
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
