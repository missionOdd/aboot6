/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.monitor.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.wteam.modules.monitor.domain.vo.RedisVo;
import com.wteam.modules.monitor.service.RedisService;
import com.wteam.modules.system.config.CacheKey;
import com.wteam.utils.PageUtil;
import com.wteam.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存监控
 * @author mission
 * @since 2019/07/11 20:18
 */
@SuppressWarnings("unchecked")
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    @Value("${login-code.expiration}")
    private Long expiration;

    private final RedisUtils redisUtils;


    @Override
    public Page<RedisVo> findByKey(String key, Pageable pageable) {
        List<RedisVo> redisVos=new ArrayList<>();
        if (!"*".equals(key)){
            key="*" + key + "*";
        }
        for (String s : redisUtils.scan(key)) {
            //过滤掉权限的缓存
            if (s.contains(CacheKey.USER_NAME)){
                continue;
            }
            Object o = redisUtils.get(s);
            RedisVo redisVo = new RedisVo(s, ObjectUtil.isNotNull(o)?o.toString():"");
            redisVos.add(redisVo);
        }

        return new PageImpl<RedisVo>(
            PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(),redisVos),
            pageable,
            redisVos.size());
    }

    @Override
    public String getCodeVal(String key) {
    try {
        return redisUtils.get(key).toString();
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public void saveCode(String key, Object val) {
        redisUtils.set(key,val);
        redisUtils.expire(key,expiration, TimeUnit.MINUTES);
    }

    @Override
    public void delete(String key) {
        redisUtils.del(key);
    }

    @Override
    public void flushDb(Integer type) {
        switch (type){
            case 0:redisUtils.flushDb();
                break;
            case 1:redisUtils.delByPattern("*::*");
                break;
        }
    }

}
