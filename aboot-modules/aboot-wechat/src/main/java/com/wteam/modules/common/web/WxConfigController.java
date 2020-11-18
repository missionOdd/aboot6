/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.modules.common.web;

import com.wteam.annotation.Log;
import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.vo.R;
import com.wteam.modules.common.domain.WxConfig;
import com.wteam.modules.common.domain.criteria.WxConfigQueryCriteria;
import com.wteam.modules.common.service.WxConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* 微信配置 控制层.
* @author aboot-wechat
* @since 2020-02-06
*/
@SuppressWarnings("rawtypes")
@Api(value="微信配置Controller",tags={"微信: 配置操作"})
@RestController
@RequiredArgsConstructor
@RequestMapping("api/wxConfig")
@PermissionGroup(value = "WXCONFIG",aliasPrefix = "微信配置")
public class WxConfigController {

    private final WxConfigService wxConfigService;


    //@Log("查询微信配置")
    @ApiOperation(value = "查询微信配置")
    @GetMapping(value = "/page")
    @PreAuthorize("@R.check('WXCONFIG:all','WXCONFIG:list')")
    public R getWxConfigs(WxConfigQueryCriteria criteria, Pageable pageable){
        return R.ok(wxConfigService.queryAll(criteria,pageable));
    }

    @Log("查询微信配置详情")
    @ApiOperation(value = "查询微信配置")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize("@R.check('GENTEMPLATE:all','GENTEMPLATE:list')")
    public R get(@PathVariable Long id){
    return R.ok(wxConfigService.findDTOById(id));
    }

    @Log("新增微信配置")
    @ApiOperation(value = "新增微信配置")
    @PostMapping(value = "/add")
    @PreAuthorize("@R.check('WXCONFIG:all','WXCONFIG:add')")
    public R create(@Validated @RequestBody WxConfig resources){
        return R.ok(wxConfigService.create(resources));
    }

    @Log("修改微信配置")
    @ApiOperation(value = "修改微信配置")
    @PostMapping(value = "/edit")
    @PreAuthorize("@R.check('WXCONFIG:all','WXCONFIG:edit')")
    public R update(@Validated @RequestBody WxConfig resources){
        wxConfigService.update(resources);
        return R.ok();
    }

   @Log("删除微信配置")
    @ApiOperation(value = "删除微信配置")
    @PostMapping(value = "/del")
    @PreAuthorize("@R.check('WXCONFIG:all','WXCONFIG:del')")
    public R delete(@RequestBody Long[] ids){
            wxConfigService.deleteAll(ids);
        return R.ok();
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('WXCONFIG:all','WXCONFIG:list')")
    public void download(HttpServletResponse response, WxConfigQueryCriteria criteria) throws IOException {
        wxConfigService.download(wxConfigService.queryAll(criteria), response);
    }

}