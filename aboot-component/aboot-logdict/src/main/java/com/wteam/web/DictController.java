/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.web;


import com.wteam.annotation.Log;
import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.Dict;
import com.wteam.domain.criteria.DictQueryCriteria;
import com.wteam.domain.dto.DictDTO;
import com.wteam.domain.vo.R;
import com.wteam.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 字典 控制层
 * @author mission
 * @since 2019/07/14 8:39
 */
@SuppressWarnings("rawtypes")
@Api(value="字典Controller",tags={"系统：字典操作"})
@RestController
@RequestMapping("api/dict")
@PermissionGroup(value = "DICT", aliasPrefix = "字典")
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }


    @ApiOperation(value = "查询字典")
    @Log("查询字典")
    @GetMapping("page")
    @PreAuthorize("@R.check('DICT:all','DICT:list')")
    public R getDicts(DictDTO resources, Pageable pageable){
        return R.ok(dictService.queryAll(resources,pageable));
    }

    @ApiOperation(value = "新增字典")
    @Log("新增字典")
    @PostMapping("add")
    @PreAuthorize("@R.check('DICT:all','DICT:add')")
    public R create(@Validated @RequestBody Dict resources){
        Assert.isNull(resources.getId(),"实体ID应为空");
        return R.ok(dictService.create(resources));
    }

    @ApiOperation(value = "修改字典")
    @Log("修改字典")
    @PostMapping("edit")
    @PreAuthorize("@R.check('DICT:all','DICt:del')")
    public R update(@Validated({Dict.Update.class}) @RequestBody Dict resources){
        dictService.update(resources);
        return R.ok();
    }

    @ApiOperation(value = "删除字典")
    @Log("删除字典")
    @PostMapping("del")
    @PreAuthorize("@R.check('DICT:all','DICT:del')")
    public R delete(@RequestBody Long[] ids){
        dictService.delete(ids);
        return R.ok();
    }

    @ApiOperation("导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('DICT:all','DICT:list')")
    public void download(HttpServletResponse response, DictQueryCriteria criteria) throws IOException {
        dictService.download(dictService.queryAll(criteria), response);
    }

}
