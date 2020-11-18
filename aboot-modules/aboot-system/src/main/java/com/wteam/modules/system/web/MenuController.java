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
import com.wteam.utils.SecurityUtils;
import com.wteam.annotation.Log;
import com.wteam.modules.system.domain.Menu;
import com.wteam.modules.system.domain.criteria.MenuQueryCriteria;
import com.wteam.modules.system.domain.dto.MenuDTO;
import com.wteam.modules.system.service.MenuService;
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
 * 菜单 控制层
 * @author mission
 * @since 2019/07/09 15:33
 */
@SuppressWarnings({"rawtypes"})
@RequiredArgsConstructor
@Api(value="菜单Controller",tags={"系统：菜单操作"})
@RestController
@RequestMapping("api/menu")
@PermissionGroup(value = "MENU", aliasPrefix = "菜单")
public class MenuController {

    private final MenuService menuService;


    /**
     * 构建前端路由所需要的菜单
     * @return /
     */
    @ApiOperation(value = "构建前端路由所需要的菜单")
    @GetMapping(value = "build")
    public R buildMenus(){
        return R.ok(menuService.findByUser(SecurityUtils.getId()));
    }

    /**
     * 返回全部的菜单
     * @return
     */
    @ApiOperation(value = "返回全部的菜单")
    @GetMapping(value = "/tree")
    @PreAuthorize("@R.check('MENU:all','MENU:add','MENU:edit','ROLES:list','ROLES:all')")
    public R getMenuTree(){
        return R.ok(menuService.getMenuTree(menuService.findByPid(0L)));
    }


    @ApiOperation(value = "查询菜单")
    @Log("查询菜单")
    @GetMapping(value = "get")
    @PreAuthorize("@R.check('MENU:all','MENU:list')")
    public R getMenus(MenuQueryCriteria criteria){
        List<MenuDTO> menuDTOList = menuService.queryAll(criteria);
        return R.ok(menuService.buildTree(menuDTOList));
    }

    @ApiOperation(value = "新增菜单")
    @Log("新增菜单")
    @PostMapping(value = "/add")
    @PreAuthorize("@R.check('MENU:all','MENU:add')")
    public R create(@Validated @RequestBody Menu resources){
        Assert.isNull(resources.getId(),"实体ID应为空");
        return  R.ok(menuService.create(resources));
    }

    @ApiOperation(value = "修改菜单")
    @Log("修改菜单")
    @PostMapping("/edit")
    @PreAuthorize("@R.check('MENU:all','MENU:edit')")
    public R update(@Validated(Menu.Update.class) @RequestBody Menu resources){
        menuService.update(resources);
        return R.ok();
    }


    @ApiOperation(value = "删除菜单")
    @Log("删除菜单")
    @PostMapping("/del")
    @PreAuthorize("@R.check('MENU:all','MENU:del')")
    public R delete(@RequestBody Long[] ids){
        menuService.delete(ids);
        return R.ok();
    }

    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('MENU:all','MENU:list')")
    public void download(HttpServletResponse response, MenuQueryCriteria criteria) throws IOException {
        menuService.download(menuService.queryAll(criteria), response);
    }
}
