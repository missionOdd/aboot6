/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.web;


import com.wteam.domain.criteria.GenTemplateQueryCriteria;
import com.wteam.service.GenTemplateService;
import com.wteam.domain.GenTemplate;
import com.wteam.domain.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* 生成模板信息 控制层.
* @author mission
* @since 2019-09-29
*/
@Api(tags = "工具：生成模板信息管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/genTemplate")
public class GenTemplateController {

    private final GenTemplateService genTemplateService;


    //@Log("查询生成模板信息")
    @ApiOperation(value = "查询生成模板信息")
    @GetMapping(value = "/list")
    public R getGenTemplates(GenTemplateQueryCriteria criteria){
        return R.ok(genTemplateService.queryAll(criteria));
    }

    //@Log("查询生成模板信息详情")
    @ApiOperation(value = "查询生成模板信息")
    @GetMapping(value = "/get/{id}")
    public R get(@PathVariable Long id){
    return R.ok(genTemplateService.findDTOById(id));
    }

    //@Log("新增生成模板信息")
    @ApiOperation(value = "新增生成模板信息")
    @PostMapping(value = "/add")
    public R create(@Validated @RequestBody GenTemplate resources){
        return R.ok(genTemplateService.create(resources));
    }

    //@Log("修改生成模板信息")
    @ApiOperation(value = "修改生成模板信息")
    @PostMapping(value = "/edit")
    public R update(@Validated @RequestBody GenTemplate resources){
        genTemplateService.update(resources);
        return R.ok();
    }

    //@Log("删除生成模板信息")
    @ApiOperation(value = "删除生成模板信息")
    @PostMapping(value = "/del")
    public R delete(@RequestBody Long[] ids){
        genTemplateService.delete(ids);
        return R.ok();
    }
}