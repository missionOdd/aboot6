/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.monitor.service;

import com.wteam.modules.monitor.domain.vo.RedisVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * redis 业务
 * @author mission
 * @since 2019/07/11 20:18
 */
public interface RedisService {

    /**
     * findById
     * @param key
     * @return
     */
    Page<RedisVo> findByKey(String key, Pageable pageable);

    /**
     * 查询验证码的值
     * @param key
     * @return
     */
    String getCodeVal(String key);

    /**
     * 保存验证码
     * @param key
     * @param val
     */
    void saveCode(String key, Object val);

    /**
     * delete
     * @param key
     */
    void delete(String key);

    /**
     * 清空所有缓存
     */
    void flushDb(Integer type);
}
