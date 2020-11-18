/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.web;

import com.wteam.annotation.AnonymousAccess;
import com.wteam.annotation.Log;
import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.ShowConfig;
import com.wteam.domain.criteria.ShowConfigQueryCriteria;
import com.wteam.domain.vo.R;
import com.wteam.service.ShowConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
* 前端配置 控制层.
* @author mission
* @since 2019-10-15
*/
@SuppressWarnings({"rawtypes"})
@Api(value="前端配置Controller",tags={"工具：前端配置操作接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping("api/showConfig")
@PermissionGroup(value = "SHOWCONFIG", aliasPrefix = "前端配置")
public class ShowConfigController {

    private final ShowConfigService showConfigService;

    //@Log("查询前端配置")
    @ApiOperation(value = "查询前端配置")
    @GetMapping(value = "/page")
    public R getShowConfigs(ShowConfigQueryCriteria criteria, Pageable pageable){
        return R.ok(showConfigService.queryAll(criteria,pageable));
    }

    //@Log("查询前端配置详情")
    @ApiOperation(value = "查询前端配置")
    @GetMapping(value = "/get/{id}")
    public R get(@PathVariable Long id){
    return R.ok(showConfigService.findDTOById(id));
    }


    //@Log("查询前端配置详情")
    @ApiOperation(value = "查询前端配置")
    @GetMapping(value = "/getByName/{name}")
    public R get(@PathVariable String name){
        return R.ok(showConfigService.findByName(name));
    }

    //@Log("新增前端配置")
    @ApiOperation(value = "更改logo")
    @PostMapping(value = "/uploadlogo")
    @PreAuthorize("@R.check('SHOWCONFIG:all','SHOWCONFIG:add')")
    public R create(@RequestParam MultipartFile file, String name){
        return R.ok(showConfigService.upload(file,name,"logo"));    }



    //@Log("修改前端配置")
    @ApiOperation(value = "修改前端配置")
    @PostMapping(value = "/edit")
    @PreAuthorize("@R.check('SHOWCONFIG:all','SHOWCONFIG:edit')")
    public R update(@Validated @RequestBody ShowConfig resources){
        showConfigService.update(resources);
        return R.ok();
    }

    //@Log("删除前端配置")
    @ApiOperation(value = "删除前端配置")
    @PostMapping(value = "/del")
    @PreAuthorize("@R.check('SHOWCONFIG:all','SHOWCONFIG:del')")
    public R delete(@RequestBody Long[] ids){
        showConfigService.delete(ids);
        return R.ok();
    }


    @Log("更改菜单顶部")
    @ApiOperation(value = "更改菜单顶部")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", dataType = "File", allowMultiple = true),
            @ApiImplicitParam(name = "name",value ="文件名")
    })
    @PostMapping(value = "/uploadMenuTopLogo")
    @PreAuthorize("@R.check('SHOWCONFIG:all','SHOWCONFIG:add')")
    public R uploadMenuTopLogo(@RequestParam(required = false) String name, @RequestParam("file") MultipartFile file){
        return R.ok(showConfigService.upload(file,name,"menuTopLogo"));
    }


    @Log("更改登录背景图")
    @ApiOperation(value = "更改登录背景图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", dataType = "File", allowMultiple = true),
            @ApiImplicitParam(name = "filename",value ="文件名")
    })
    @PostMapping(value = "/uploadLoginBackground")
    @PreAuthorize("@R.check('SHOWCONFIG:all','SHOWCONFIG:add')")
    public R uploadLoginBackground(@RequestParam(required = false) String filename, @RequestParam("file") MultipartFile file){
        return R.ok(showConfigService.upload(file,filename,"loginBackground"));
    }

    @ApiOperation(value = "获取菜单顶部Logo")
    @GetMapping(value = "/getMenuTopLogo")
    @AnonymousAccess
    public R getMenuTopLogo(){
        return R.ok(showConfigService.findByName("menuTopLogo"));
    }

    @ApiOperation(value = "获取登录背景图")
    @GetMapping(value = "/getLoginBackground")
    @AnonymousAccess
    public R getLoginBackground(){
        return R.ok(showConfigService.findByName("loginBackground"));
    }
}