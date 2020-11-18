/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.web;

import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.annotation.Log;
import com.wteam.domain.vo.R;
import com.wteam.domain.DictDetail;
import com.wteam.domain.criteria.DictDetailQueryCriteria;
import com.wteam.service.DictDetailService;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 字典详情 控制层
 * @author mission
 * @since 2019/07/13 19:05
 */
@SuppressWarnings({"rawtypes"})
@RequiredArgsConstructor
@Api(value="字典详情Controller",tags={"系统：字典详情操作"})
@RestController
@RequestMapping("api/dictDetail")
@PermissionGroup(value = "DICT", aliasPrefix = "字典")
public class DictDetailController {

    private final DictDetailService dictDetailService;

    @ApiOperation(value = "查询字典详情")
    @Log("查询字典详情")
    @GetMapping("page")
    public R getDictDetails(DictDetailQueryCriteria criteria,
                            @PageableDefault(sort = {"sort"},direction = Sort.Direction.ASC) Pageable pageable){
        return R.ok(dictDetailService.queryAll(criteria,pageable));
    }


    @ApiOperation(value = "查询多个字典详情")
    @Log("查询多个字典详情")
    @GetMapping("map")
    public R getDictDetailMaps(DictDetailQueryCriteria criteria,
                               @PageableDefault(sort = {"sort"},direction = Sort.Direction.ASC) Pageable pageable){
        String[] names=criteria.getDictName().split(",");
        Map<String,Object> map =new HashMap<>();
        for (String name : names) {
            criteria.setDictName(name);
            map.put(name,dictDetailService.queryAll(criteria,pageable).get("content"));
        }
        return R.ok(map);
    }


    @ApiOperation(value = "新增字典详情")
    @Log("新增字典详情")
    @PostMapping("add")
    @PreAuthorize("@R.check('DEPT:all','DEPT:add')")
    public R create(@Validated @RequestBody DictDetail resources){
        Assert.isNull(resources.getId(),"实体ID应为空");
        return R.ok(dictDetailService.create(resources));
    }

    @ApiOperation(value = "修改字典详情")
    @Log("修改字典详情")
    @PostMapping("edit")
    @PreAuthorize("@R.check('DICT:all','DICT:edit')")
    public R edit(@Validated(DictDetail.Update.class) @RequestBody DictDetail resources){
        dictDetailService.update(resources);
        return R.ok();
    }

    @ApiOperation(value = "删除字典详情")
    @Log("删除字典详情")
    @PostMapping("del")
    @PreAuthorize("@R.check('DICT:all','DICT:del')")
    public R delete(@RequestBody Long[] ids){
        dictDetailService.delete(ids);
        return R.ok();
    }

}
