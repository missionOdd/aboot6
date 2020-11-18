/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.monitor.web;

import com.wteam.annotation.Log;
import com.wteam.domain.vo.R;
import com.wteam.modules.monitor.domain.vo.RedisVo;
import com.wteam.modules.monitor.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * redis缓存 控制层
 * @author mission
 * @since 2019/07/11 21:39
 */
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
@Api(tags="监控：缓存操作接口")
@RestController
@RequestMapping("api/redis")
public class RedisController {

    private final RedisService redisService;

    @Log("查询Redis缓存")
    @ApiOperation("查询Redis缓存")
    @GetMapping("/page")
    @PreAuthorize("@R.check('REDIS:all','REDIS:list')")
    public R getRedis(String key, Pageable pageable){
        return R.ok(redisService.findByKey(key,pageable));
    }

    @Log("删除Redis缓存")
    @ApiOperation("删除Redis缓存")
    @PostMapping("/del")
    @PreAuthorize("@R.check('REDIS:all','REDIS:del')")
    public R delete(@RequestBody RedisVo resource){
        redisService.delete(resource.getKey());
        return R.ok();
    }


    @Log("清空Redis缓存")
    @ApiOperation("清空Redis缓存")
    @PostMapping("/delAll/{type}")
    @PreAuthorize("@R.check('REDIS:all','REDIS:del')")
    public R delAll(@PathVariable Integer type){
        redisService.flushDb(type);
        return R.ok();
    }
}
