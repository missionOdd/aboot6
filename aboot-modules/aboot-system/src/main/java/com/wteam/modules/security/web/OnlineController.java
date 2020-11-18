/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.security.web;

import com.wteam.annotation.Log;
import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.vo.R;
import com.wteam.modules.security.service.OnlineUserService;
import com.wteam.utils.EncryptUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author mission
 * @since 2019/11/02 17:27
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/online")
@Api(tags = "系统：在线用户管理")
@PermissionGroup(value = "ONLINE", aliasPrefix = "在线用户")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    public OnlineController(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @ApiOperation("查询在线用户")
    @GetMapping
    @PreAuthorize("@R.check('ONLINE:all','ONLINE:list')")
    public R getAll(String filter, Pageable pageable){
        return R.ok(onlineUserService.getAll(filter, pageable));
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('ONLINE:all','ONLINE:list')")
    public void download(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.download(onlineUserService.getAll(filter), response);
    }

    @ApiOperation("踢出用户")
    @PostMapping(value = "/del")
    @PreAuthorize("@R.check('ONLINE:all','ONLINE:del')")
    public R delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            // 解密Key
            key = EncryptUtils.desDecrypt(key);
            onlineUserService.kickOut(key);
        }
        return R.ok();
    }


}
