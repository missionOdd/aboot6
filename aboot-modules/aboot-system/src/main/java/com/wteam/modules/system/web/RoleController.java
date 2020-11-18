/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.system.web;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import com.wteam.annotation.Log;
import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.vo.R;
import com.wteam.exception.BadRequestException;
import com.wteam.modules.system.domain.Role;
import com.wteam.modules.system.domain.criteria.RoleQueryCriteria;
import com.wteam.modules.system.domain.dto.RoleDTO;
import com.wteam.modules.system.domain.dto.RoleSmallDTO;
import com.wteam.modules.system.service.RoleService;
import com.wteam.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色
 * @author mission
 * @since 2019/07/13 15:42
 */
@SuppressWarnings({"rawtypes"})
@Api(value="角色Controller",tags={"系统：角色操作"})
@RequiredArgsConstructor
@RestController
@RequestMapping("api/role")
@PermissionGroup(value = "ROLES", aliasPrefix = "角色")
public class RoleController {
    
    private final RoleService roleService;

    @ApiOperation(value = "获取单个role")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize("@R.check('ROLES:all','ROLES:list')")
    public R getRoles(@PathVariable Long id){
        return R.ok(roleService.findDTOById(id));
    }


    @ApiOperation(value = "返回全部的角色，新增用户时下拉选择")
    @GetMapping(value = "/all")
    @PreAuthorize("@R.check('ROLES:all','USER:all','USER:add','USER:edit')")
    public R getAll(@PageableDefault(value = 2000, sort = {"level"}, direction = Sort.Direction.ASC) Pageable pageable){
        return R.ok(roleService.queryAll(pageable));
    }

    @ApiOperation(value = "查询角色列表")
    @Log("查询角色")
    @GetMapping(value = "/page")
    @PreAuthorize("@R.check('ROLES:all','ROLES:list')")
    public R getRoles(RoleQueryCriteria criteria, Pageable pageable){
        return R.ok(roleService.queryAll(criteria,pageable));
    }

    @ApiOperation(value = "查询角色级别")
    @GetMapping(value = "/level")
    public R getLevel(){
        return R.ok(Dict.create().set("level", getLevels(null)));
    }

    @ApiOperation(value = "新增角色")
    @Log("新增角色")
    @PostMapping(value = "/add")
    @PreAuthorize("@R.check('ROLES:all','ROLES:add')")
    public R create(@Validated @RequestBody Role resources){
        Assert.isNull(resources.getId(),"实体ID应为空");
        getLevels(resources.getLevel());
        return R.ok(roleService.create(resources));
    }

    @ApiOperation(value = "修改角色")
    @Log("修改角色")
    @PostMapping(value = "/edit")
    @PreAuthorize("@R.check('ROLES:all','ROLES:edit')")
    public R edit(@Validated(Role.Update.class) @RequestBody Role resources){
        getLevels(resources.getLevel());
        roleService.update(resources);
        return R.ok();
    }

    @ApiOperation(value = "修改角色权限")
    @Log("修改角色权限")
    @PostMapping(value = "/updatePermission")
    @PreAuthorize("@R.check('ROLES:all','ROLES:edit')")
    public R updatePermission(@RequestBody Role resources){
        RoleDTO role = roleService.findDTOById(resources.getId());
        getLevels(role.getLevel());
        roleService.updatePermission(resources);
        return R.ok();
    }

    @ApiOperation(value = "修改角色菜单")
    @Log("修改角色菜单")
    @PostMapping(value = "/updateMenu")
    @PreAuthorize("@R.check('ROLES:all','ROLES:edit')")
    public R updateMenu(@RequestBody Role resources){
        RoleDTO role = roleService.findDTOById(resources.getId());
        getLevels(role.getLevel());
        roleService.updateMenu(resources);
        return R.ok();
    }

    @ApiOperation(value = "删除角色")
    @Log("删除角色")
    @PostMapping(value = "/del")
    @PreAuthorize("@R.check('ROLES:all','ROLES:del')")
    public R delete(@RequestBody Set<Long> ids){
        if (CollectionUtil.contains(ids,1L)) {
            throw new BadRequestException("该角色系统不允许删除");
        }
        for (Long id : ids) {
            RoleDTO role = roleService.findDTOById(id);
            getLevels(role.getLevel());
        }
        roleService.delete(ids);
        return R.ok();
    }

    /**
     * 导出数据
     * @param criteria /
     * @return
     */
    @ApiOperation(value = "导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('ROLES:all','ROLES:list')")
    public void download(RoleQueryCriteria criteria, HttpServletResponse response) throws IOException {
        roleService.download(roleService.queryAll(criteria),response);
    }

    /**
     * 获取用户的角色级别
     * @return /
     */
    private int getLevels(Integer level){
        List<Integer> levels = roleService.findByUsersId(SecurityUtils.getId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if(level != null){
            if(level < min){
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
