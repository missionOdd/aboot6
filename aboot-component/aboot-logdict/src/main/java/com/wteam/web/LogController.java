/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.web;

import com.wteam.annotation.Log;
import com.wteam.aspect.LogType;
import com.wteam.domain.criteria.LogQueryCriteria;
import com.wteam.domain.vo.R;
import com.wteam.service.LogService;
import com.wteam.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 日志 控制层
 * @author mission
 * @since 2019/07/08 11:33
 */
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
@Api(tags="日志：日志操作接口")
@RestController
@RequestMapping("api/log")
public class LogController {

    private final LogService logService;


    @ApiOperation(value = "获取操作日志列表")
    @GetMapping("page")
    public R pageLogs(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType(LogType.INFO.name());
        return R.ok(logService.queryAll(criteria,pageable));
    }

    @ApiOperation(value = "获取登录用户日志列表")
    @GetMapping(value = "/page/user")
    public R getUserLogs(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType(LogType.INFO.name());
        criteria.setUsername(SecurityUtils.getUsername());
        return R.ok(logService.queryAllByUser(criteria,pageable));
    }

    @ApiOperation(value = "获取错误日志列表")
    @GetMapping(value = "/page/error")
    @PreAuthorize("@R.check()")
    public R getErrorLogs(LogQueryCriteria criteria, Pageable pageable){
        criteria.setLogType(LogType.ERROR.name());
        return R.ok(logService.queryAll(criteria,pageable));
    }

    @ApiOperation(value = "获取错误日志详情")
    @GetMapping(value = "/page/error/{id}")
    @PreAuthorize("@R.check()")
    public R getErrorLogs(@PathVariable Long id){
        return R.ok(logService.findByErrDetail(id));
    }


    @ApiOperation("删除所有ERROR日志")
    @Log("删除所有ERROR日志")
    @PostMapping(value = "/del/error")
    @PreAuthorize("@R.check()")
    public R delAllByError(){
        logService.delAllByError();
        return R.ok();
    }

    @ApiOperation("删除所有INFO日志")
    @Log("删除所有INFO日志")
    @PostMapping(value = "/del/info")
    @PreAuthorize("@R.check()")
    public R delAllByInfo(){
        logService.delAllByInfo();
        return R.ok();
    }


    @Log("导出INFO日志")
    @ApiOperation("导出INFO日志")
    @GetMapping(value = "/info/download")
    @PreAuthorize("@R.check()")
    public void infoDownload(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        criteria.setLogType(LogType.INFO.name());
        logService.download(logService.queryAll(criteria), response);
    }


    @Log("导出ERROR日志")
    @ApiOperation("导出ERROR日志")
    @GetMapping(value = "/error/download")
    @PreAuthorize("@R.check()")
    public void errorDownload(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        criteria.setLogType(LogType.ERROR.name());
        logService.download(logService.queryAll(criteria), response);
    }
}
