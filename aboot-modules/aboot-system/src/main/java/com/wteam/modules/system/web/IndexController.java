/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.system.web;

import com.wteam.annotation.Limit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mission
 * @since 2020/01/18 20:20
 */
@Api(value="主页Controller",tags={"系统：系统主页"})
@RestController
public class IndexController {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    @GetMapping("/")
    private String index(){
        return "Welcome Aboot System !";
    }

    /**
     * 测试限流注解，下面配置说明该接口 60秒内最多只能访问 10次，保存到redis的键名为 limit_test，
     */
    @ApiOperation("测试")
    @GetMapping("test")
    @Limit(key = "test", period = 60, count = 10, name = "testLimit", prefix = "limit")
    public int test() {
        return ATOMIC_INTEGER.incrementAndGet();
    }
}
