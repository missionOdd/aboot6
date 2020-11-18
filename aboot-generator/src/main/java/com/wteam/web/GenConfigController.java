/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.web;


import com.wteam.domain.GenConfig;
import com.wteam.service.GenConfigService;
import com.wteam.domain.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author mission
 * @since 2019/09/10 8:59
 */
@Api(tags = "工具：代码生成配置")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/genConfig")
public class GenConfigController {

    private final GenConfigService genConfigService;


    @ApiOperation("查询数据库生成配置")
    @GetMapping(value = "/get/{tableName}")
    public R get(@PathVariable String tableName){
        return R.ok(genConfigService.find(tableName));
    }

    @ApiOperation("编辑数据库生成配置")
    @PostMapping("/edit")
    public R edit(@Validated @RequestBody GenConfig genConfig){
/*        if (genConfig.getCover()) {
            throw new BadRequestException("不允许覆盖文件");
        }*/
        return R.ok(genConfigService.update(genConfig));
    }

}
