/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.modules.security.service;


import com.wteam.modules.system.domain.Permission;
import com.wteam.modules.system.domain.Role;
import com.wteam.modules.system.domain.dto.UserDTO;
import com.wteam.modules.system.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mission
 * @since 2019/07/11 14:56
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtPermissionService {

    private final RoleRepository roleRepository;


    /**
     * key的名称如有修改，请同步修改 UserServiceImpl 中的 update 方法
     * @param user
     * @return
     */
    public Collection<GrantedAuthority> mapToGrantedAuthorities(UserDTO user) {
        log.debug("--------------------loadPermissionByUser:" + user.getUsername() + "---------------------");
        Set<Role> roles = roleRepository.findByUsers_Id(user.getId());
        return roles.stream()
                .flatMap(JwtPermissionService::apply)
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private static Stream<? extends String> apply(Role role) {
        return Stream.concat(
                role.getPermissions().stream().map(Permission::getName),
                Stream.of(role.getAuthority())
        );
    }
}
