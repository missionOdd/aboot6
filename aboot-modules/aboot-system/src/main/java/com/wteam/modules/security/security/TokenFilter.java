/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.security.security;

import com.wteam.modules.security.util.JwtTokenUtil;
import com.wteam.modules.security.config.JwtProperties;
import com.wteam.modules.security.domain.vo.OnlineUser;
import com.wteam.modules.security.service.OnlineUserService;
import com.wteam.modules.security.service.UserCacheClean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 权限拦截器
 * @author mission
 * @since 2019/07/10 11:52
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtProperties jwtProperties;
    private final OnlineUserService onlineUserService;
    private final UserCacheClean userCacheClean;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        OnlineUser onlineUser = null;
        String token= null;
        String requestUri = request.getRequestURI();
        String bearerToken = request.getHeader(jwtProperties.getHeader());

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token= bearerToken.substring(7);
            boolean cleanUserCache = false;
            try{
                onlineUser=onlineUserService.getOne(jwtProperties.getOnlineKey()+token);
            }catch (Exception e){
                log.error("Error Bearer Token: {}",token);
                log.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || Objects.isNull(onlineUser)) {
                    userCacheClean.cleanUserCache(jwtTokenUtil.getUsernameFromToken(token));
                }
            }
        }

        if (onlineUser !=null && jwtTokenUtil.validateToken(token)){
            //验证
            Authentication authentication = jwtTokenUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //续期
            jwtTokenUtil.checkRenewal(token);
            log.debug("{}: the Authentication is '{}', uri: {}",request.getMethod(), authentication.getName(), requestUri);
        }else {
            log.debug("{}: no valid JWT token found, uri: {}",request.getMethod(), requestUri);
        }
        //放行
        chain.doFilter(request, response);
    }


}
