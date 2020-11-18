/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.security.service;

import com.wteam.domain.vo.JwtUser;
import com.wteam.exception.BadRequestException;
import com.wteam.modules.system.domain.dto.DeptSmallDTO;
import com.wteam.modules.system.domain.dto.JobSmallDTO;
import com.wteam.modules.system.domain.dto.UserDTO;
import com.wteam.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mission
 * @since 2019/07/11 14:56
 */
@RequiredArgsConstructor
@Service("userDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final JwtPermissionService permissionService;

    /**
     * 用户信息缓存
     *
     * @see {@link UserCacheClean}
     */
    static Map<String, JwtUser> userDtoCache = new ConcurrentHashMap<>();


    @Override
    public UserDetails loadUserByUsername(String username) {
        JwtUser jwtUser = null;
        if (userDtoCache.containsKey(username)) {
            jwtUser = userDtoCache.get(username);
        }else {
            UserDTO user=userService.findByName(username);
            if (user==null){
                throw new BadRequestException("账号不存在");
            }
            if (!user.getEnabled()) {
                throw new BadRequestException("账号未激活！");
            }
            jwtUser=createJwtUser(user);

            userDtoCache.put(username, jwtUser);
        }
        return jwtUser;
    }

    public JwtUser createJwtUser(UserDTO user){
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getPassword(),
                user.getSex(),
                user.getAvatar(),
                user.getEmail(),
                user.getPhone(),
                user.getLoginType(),
                user.getEnabled(),
                user.getLastPasswordResetTime(),
                Optional.ofNullable(user.getDept()).map(DeptSmallDTO::getName).orElse(null),
                Optional.ofNullable(user.getJob()).map(JobSmallDTO::getName).orElse(null),
                permissionService.mapToGrantedAuthorities(user)
        );
    }
}
