/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.web;

import com.wteam.domain.ColumnInfo;
import com.wteam.service.GenConfigService;
import com.wteam.service.GeneratorService;
import com.wteam.domain.vo.R;
import com.wteam.exception.BadRequestException;
import com.wteam.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author mission
 * @since 2019/09/10 9:12
 */
@Api(tags = "工具：代码生成器")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/generator")
public class GeneratorController {

    private final GeneratorService generatorService;

    private final GenConfigService genConfigService;


    @Value("${generator.enabled}")
    private Boolean generatorEnabled;


    @ApiOperation("查询所有数据库数据")
    @GetMapping(value = "/tables/all")
    public R getTables(){
        return R.ok(generatorService.getTables());
    }

    @ApiOperation("查询数据库数据")
    @GetMapping(value = "/tables")
    public R getTables(@RequestParam(required = false) String name,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "10") Integer size){
        int[] startEnd = PageUtil.transToStartEnd(page,size);
        return R.ok(generatorService.getTables(name,startEnd));
    }

    @ApiOperation("保存字段数据")
    @PostMapping(value ="/columns/edit")
    public R save(@RequestBody List<ColumnInfo> columnInfos){
        generatorService.save(columnInfos);
        return R.ok();
    }

    @ApiOperation("查询字段数据")
    @GetMapping(value = "/columns/{tableName}")
    public R getColumns(@PathVariable String tableName){
        return R.ok(generatorService.getColumns(tableName));
    }

    @ApiOperation("同步字段数据")
    @PostMapping(value = "sync")
    public R sync(@RequestBody List<String> tables){
        for (String table : tables) {
            generatorService.sync(generatorService.getColumns(table), generatorService.query(table));
        }
        return R.ok();
    }

    @ApiOperation("生成代码")
    @PostMapping(value = "/handle/{tableName}/{type}")
    public R generator(@PathVariable String tableName, @PathVariable Integer type, HttpServletRequest request, HttpServletResponse response){
        if (!generatorEnabled){
            throw new BadRequestException("此环境不允许代码生成");
        }
        switch (type){
            // 生成代码
            case 0: generatorService.generator(genConfigService.find(tableName), generatorService.getColumns(tableName));
                break;
            // 预览
            case 1: return R.ok(
                    generatorService.preview(genConfigService.find(tableName), generatorService.getColumns(tableName))
                    );
            // 打包
            case 2: generatorService.download(genConfigService.find(tableName), generatorService.getColumns(tableName), request, response);
                break;
            default: throw new BadRequestException("没有这个选项");
        }
        return R.ok();
    }
}
