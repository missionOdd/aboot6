/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.monitor.web;


import com.wteam.annotation.rest.AnonymousGetMapping;
import com.wteam.domain.vo.R;
import com.wteam.modules.monitor.service.VisitsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问记录 控制层
 * @author mission
 * @since 2019/07/26 16:43
 */
@Api(value = "访问记录Controller",tags = {"监控：访问记录操作接口"})
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/visit")
public class VisitsController {


    private final VisitsService visitsService;

    public VisitsController(VisitsService visitsService) {
        this.visitsService = visitsService;
    }

    @ApiOperation("获取访问记录")
    @GetMapping("get")
    public R get(){
        return R.ok(visitsService.get());
    }


    @ApiOperation("添加访问记录")
    @AnonymousGetMapping("ip.pv")
    public R create(HttpServletRequest request){
        visitsService.count(request);
        return R.ok();
    }

    @ApiOperation("获取访问记录统计表")
    @GetMapping("chartData")
    public R chartData(){
        return R.ok(visitsService.getChartData());
    }
}
