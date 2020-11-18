/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wteam.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * 登录用户工具类
 * @author mission
 * @since 2019/07/08 12:06
 */
public class SecurityUtils {

    /**
     * 获取当前用户 ,从缓存获取
     * @return UserDetails
     */
    public static UserDetails getUserDetails() {
        UserDetails userDetails = null;
        try {
            UserDetailsService userDetailsService = SpringContextUtil.getBean(UserDetailsService.class);
            userDetails = userDetailsService.loadUserByUsername(getUsername());
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "登录状态过期");
        }
        return userDetails;
    }

    /**
     * 获取系统用户名称
     * @return 系统用户名称
     */
    public static String getUsername(){
        UserDetails userDetails = getUserSimple();
        return userDetails.getUsername();
    }

    /**
     * 获取系统用户id
     */
    public static Long getId(){
        UserDetails obj = getUserSimple();
        return new JSONObject(obj).get("id", Long.class);
    }

    /**
     * 获取用户关键信息 ,从token获取
     */
    public static UserDetails getUserSimple(){
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "登录状态过期");
        }
        return userDetails;
    }

    /**
     * 获取系统用户的数据权限范围
     */
    public static List<Long> getDataScope() {
        UserDetails obj = getUserSimple();
        JSONArray array = JSONUtil.parseArray(new JSONObject(obj).get("dataScopes"));
        return JSONUtil.toList(array,Long.class);
    }
}
