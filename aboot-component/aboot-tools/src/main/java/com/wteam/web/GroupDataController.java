/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.web;

import com.wteam.annotation.AnonymousAccess;
import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.GroupData;
import com.wteam.domain.criteria.GroupDataQueryCriteria;
import com.wteam.domain.vo.R;
import com.wteam.service.GroupDataService;
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
* 数据组 控制层.
* @author mission
* @since 2020-03-23
*/
@SuppressWarnings({"rawtypes"})
@Api(value="数据组Controller",tags={"新增：数据组操作"})
@RestController
@RequiredArgsConstructor
@RequestMapping("api/groupData")
@PermissionGroup(value = "GROUPDATA",aliasPrefix = "数据组")
public class GroupDataController {

    private final GroupDataService groupDataService;

    //@Log("查询数据组分页")
    @ApiOperation(value = "查询数据组分页")
    @GetMapping(value = "/page")
    @AnonymousAccess
    public R getGroupDatas(GroupDataQueryCriteria criteria, Pageable pageable){
        return R.ok(groupDataService.queryAll(criteria,pageable));
    }

    //@Log("查询数据组详情")
    @ApiOperation(value = "查询数据组详情")
    @AnonymousAccess
    public R get(@PathVariable Long id){
        return R.ok(groupDataService.findDTOById(id));
    }

    //@Log("新增数据组")
    @ApiOperation(value = "新增数据组")
    @PostMapping(value = "/add")
    @PreAuthorize("@R.check('GROUPDATA:all','GROUPDATA:add')")
    public R create(@Validated @RequestBody GroupData resources){
        return R.ok(groupDataService.create(resources));
    }

    //@Log("修改数据组")
    @ApiOperation(value = "修改数据组")
    @PostMapping(value = "/edit")
    @PreAuthorize("@R.check('GROUPDATA:all','GROUPDATA:edit')")
    public R update(@Validated(GroupData.Update.class) @RequestBody GroupData resources){
        groupDataService.update(resources);
        return R.ok();
    }

    //@Log("删除数据组")
    @ApiOperation(value = "删除数据组")
    @PostMapping(value = "/del")
    @PreAuthorize("@R.check('GROUPDATA:all','GROUPDATA:del')")
    public R delete(@RequestBody Long[] ids){
       groupDataService.deleteAll(ids);
        return R.ok();
    }

    //@Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('GROUPDATA:all','GROUPDATA:list')")
    public void download(HttpServletResponse response, GroupDataQueryCriteria criteria) throws IOException {
        groupDataService.download(groupDataService.queryAll(criteria), response);
    }



    //@Log("查询数据组详情")
    @ApiOperation(value = "查询数据组详情")
    @GetMapping(value = "/getData/{name}")
    @AnonymousAccess
    public R get(@PathVariable String name){
        return R.ok(groupDataService.findDTOByName(name));
    }

}