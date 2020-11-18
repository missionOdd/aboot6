/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.system.web;


import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.vo.R;
import com.wteam.annotation.Log;
import com.wteam.modules.system.domain.Permission;
import com.wteam.modules.system.domain.criteria.PermissionQueryCriteria;
import com.wteam.modules.system.domain.dto.PermissionDTO;
import com.wteam.modules.system.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 权限 控制层
 * @author mission
 * @since 2019/07/13 17:00
 */
@SuppressWarnings({"rawtypes"})
@Api(value="权限Controller",tags={"系统：权限操作"})
@RestController
@RequiredArgsConstructor
@RequestMapping("api/permission")
@PermissionGroup(value = "PERMISSION", aliasPrefix = "权限")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 返回全部的权限, 新增角色时下拉选择框
     * @return /
     */
    @ApiOperation(value = "返回全部的权限, 新增角色时下拉选择框")
    @GetMapping("tree")
    @PreAuthorize("@R.check('PERMISSION:add','PERMISSION:edit','ROLES:list','ROLES:all')")
    public R getTree(){
        return R.ok(permissionService.getPermissionTree(permissionService.findByPid(0L)));
    }

    @ApiOperation(value = "查询权限")
    @Log("查询权限")
    @GetMapping("/get")
    @PreAuthorize("@R.check('PERMISSION:all','PERMISSION:list')")
    public R getPermissions(PermissionQueryCriteria criteria){
        List<PermissionDTO> permissionDTOS = permissionService.queryAll(criteria);
        return R.ok(permissionService.buildTree(permissionDTOS));
    }


    @ApiOperation(value = "新增权限")
    @Log("新增权限")
    @PostMapping("/add")
    @PreAuthorize("@R.check('PERMISSION:all','PERMISSION:add')")
    public R create(@Validated @RequestBody Permission resources){
        Assert.isNull(resources.getId(),"实体ID应为空");
        return R.ok(permissionService.create(resources));
    }

    @ApiOperation("修改权限")
    @Log("修改权限")
    @PostMapping("/edit")
    @PreAuthorize("@R.check('PERMISSION:all','PERMISSION:edit')")
    public R update(@Validated(Permission.Update.class) @RequestBody Permission resources){
        permissionService.update(resources);
        return R.ok();
    }


    @ApiOperation(value = "删除权限")
    @Log("删除权限")
    @PostMapping("/del")
    @PreAuthorize("@R.check('PERMISSION:all','PERMISSION:del')")
    public R delete(@RequestBody Long[] ids){
        permissionService.delete(ids);
        return R.ok();
    }

    @ApiOperation(value = "导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('MENU:all','MENU:list')")
    public void download(HttpServletResponse response, PermissionQueryCriteria criteria) throws IOException {
        permissionService.download(permissionService.queryAll(criteria), response);
    }
}
