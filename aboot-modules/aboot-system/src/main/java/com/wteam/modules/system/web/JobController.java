/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.system.web;


import cn.hutool.core.collection.CollectionUtil;
import com.wteam.annotation.Log;
import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.vo.R;
import com.wteam.exception.BadRequestException;
import com.wteam.modules.system.config.DataService;
import com.wteam.modules.system.domain.Job;
import com.wteam.modules.system.domain.criteria.JobQueryCriteria;
import com.wteam.modules.system.service.JobService;
import com.wteam.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 岗位 控制层
 * @author mission
 * @since 2019/07/13 17:26
 */
@SuppressWarnings({"rawtypes"})
@Api(value="岗位Controller",tags={"系统：岗位操作"})
@RequiredArgsConstructor
@RestController
@RequestMapping("api/job")
@PermissionGroup(value = "JOB", aliasPrefix = "岗位")
public class JobController {

    private final JobService jobService;

    private final DataService dataScope;


    @ApiOperation(value = "查询岗位列表")
    @Log("查询岗位")
    @GetMapping("page")
    @PreAuthorize("@R.check('JOB:all','USERJOB:list','USER:all','USER:list')")
    public R getJobs(JobQueryCriteria criteria, Pageable pageable){
        //数据权限
        criteria.setDeptIds(dataScope.getDeptIds(SecurityUtils.getId()));
        return R.ok(jobService.queryAll(criteria,pageable));
    }


    @ApiOperation(value = "新增岗位")
    @Log("新增岗位")
    @PostMapping("add")
    @PreAuthorize("@R.check('USERJOB:all','USERJOB:add')")
    public R create(@Validated @RequestBody Job resources){
        Assert.isNull(resources.getId(),"实体ID应为空");
        return R.ok(jobService.create(resources));
    }

    @ApiOperation(value = "修改岗位")
    @Log("修改岗位")
    @PostMapping("edit")
    @PreAuthorize("@R.check('USERJOB:all','USERJOB:edit')")
    public R edit(@Validated(Job.Update.class) @RequestBody Job resources){
        jobService.update(resources);
        return R.ok();
    }

    @ApiOperation(value = "删除岗位")
    @Log("删除岗位")
    @PostMapping("del")
    @PreAuthorize("@R.check('USERJOB:all','USERJOB:del')")
    public R del(@RequestBody Set<Long> ids){

        if (CollectionUtil.contains(ids,1L)) {
            throw new BadRequestException("该岗位系统不允许删除");
        }
        jobService.delete(ids);
        return R.ok();
    }

    /**
     * 导出数据
     * @param criteria /
     * @return
     */
    @ApiOperation(value = "导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('USERJOB:all','USERJOB:list','USER:all','USER:list')")
    public void download(JobQueryCriteria criteria, HttpServletResponse response) throws IOException {
        jobService.download(jobService.queryAll(criteria),response);
    }
}
