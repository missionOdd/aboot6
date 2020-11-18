/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.config;

import com.wteam.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限检测
 * @author mission
 * @since 2019/12/01 15:16
 */
@Service(value = "R")
public class AuthoritiesConfig {

    public Boolean check(String ...permissions){
        // 获取当前用户的所有权限
        List<String> authorities = SecurityUtils.getUserSimple().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限,超级管理员ADMIN直接通过
        return authorities.contains("ADMIN") || Arrays.stream(permissions).anyMatch(authorities::contains);
    }

}
