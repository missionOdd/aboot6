/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.system.web;


import com.wteam.annotation.Log;
import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.vo.R;
import com.wteam.modules.system.config.DataService;
import com.wteam.modules.system.domain.Dept;
import com.wteam.modules.system.domain.criteria.DeptQueryCriteria;
import com.wteam.modules.system.domain.dto.DeptDTO;
import com.wteam.modules.system.service.DeptService;
import com.wteam.utils.SecurityUtils;
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
 * 部门 控制层
 * @author mission
 * @since 2019/07/13 11:24
 */
@SuppressWarnings({"rawtypes"})
@Api(value="部门Controller",tags={"系统：部门操作"})
@RequiredArgsConstructor
@RestController
@RequestMapping("api/dept")
@PermissionGroup(value = "DEPT", aliasPrefix = "部门")
public class DeptController {

    private final DeptService deptService;
    private final DataService dataService;


    @ApiOperation(value = "查询部门")
    @Log("查询部门")
    @GetMapping(value = "/get")
    @PreAuthorize("@R.check('USER:all','USER:list','DEPT:all','DEPT:list')")
    public R getDepts(DeptQueryCriteria criteria){
        //数据权限
        criteria.setIds(dataService.getDeptIds(SecurityUtils.getId()));
        List<DeptDTO> deptDTOS =deptService.queryAll(criteria);
        return R.ok(deptService.buildTree(deptDTOS));
    }

    @ApiOperation(value = "新增部门")
    @Log("新增部门")
    @PostMapping("add")
    @PreAuthorize("@R.check('DEPT:all','DEPT:add')")
    public R create(@Validated @RequestBody Dept resources){
        Assert.isNull(resources.getId(),"实体ID应为空");
        return R.ok(deptService.create(resources));
    }

    @ApiOperation(value = "修改部门")
    @Log("修改部门")
    @PostMapping(value = "/edit")
    @PreAuthorize("@R.check('DEPT:all','DEPT:edit')")
    public R edit(@Validated(Dept.Update.class) @RequestBody Dept resources){
        deptService.update(resources);
        return R.ok();
    }

    @ApiOperation(value = "删除部门")
    @Log("删除部门")
    @PostMapping("del")
    @PreAuthorize("@R.check('DEPT:all','DEPT:del')")
    public R delete(@RequestBody Long[] ids){
        deptService.delete(ids);
        return R.ok();
    }

    @ApiOperation("导出部门数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('DEPT:all','DEPT:list')")
    public void download(HttpServletResponse response, DeptQueryCriteria criteria) throws IOException {
        deptService.download(deptService.queryAll(criteria), response);
    }
}
