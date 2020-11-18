/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.miniapp.web;

import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.vo.R;
import com.wteam.modules.miniapp.domain.criteria.WxUserQueryCriteria;
import com.wteam.modules.miniapp.service.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* 微信用户 控制层.
* @author mission
* @since 2020-02-06
*/
@SuppressWarnings("rawtypes")
@Api(value="微信用户Controller",tags={"微信: 用户操作"})
@RestController
@RequiredArgsConstructor
@RequestMapping("api/wxUser")
@PermissionGroup(value = "WXUSER",aliasPrefix = "微信用户")
public class WxUserController {

    private final WxUserService wxUserService;

    //@Log("查询微信用户")
    @ApiOperation(value = "查询微信用户")
    @GetMapping(value = "/page")
    @PreAuthorize("@R.check('WXUSER:all','WXUSER:list')")
    public R getWxUsers(WxUserQueryCriteria criteria, Pageable pageable){
        return R.ok(wxUserService.queryAll(criteria,pageable));
    }

    //@Log("查询微信用户详情")
    @ApiOperation(value = "查询微信用户")
    @GetMapping(value = "/get/{uid}")
    @PreAuthorize("@R.check('GENTEMPLATE:all','GENTEMPLATE:list')")
    public R get(@PathVariable Long uid){
        return R.ok(wxUserService.findDTOById(uid));
    }


    //@Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('WXUSER:all','WXUSER:list')")
    public void download(HttpServletResponse response, WxUserQueryCriteria criteria) throws IOException {
        wxUserService.download(wxUserService.queryAll(criteria), response);
    }

}