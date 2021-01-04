/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.wechat.web;


import com.google.common.collect.Sets;
import com.wteam.annotation.AnonymousAccess;
import com.wteam.annotation.Log;
import com.wteam.annotation.rest.AnonymousPostMapping;
import com.wteam.domain.vo.JwtUser;
import com.wteam.domain.vo.R;
import com.wteam.exception.BadRequestException;
import com.wteam.modules.miniapp.domain.WxUser;
import com.wteam.modules.miniapp.domain.dto.WxLoginDTO;
import com.wteam.modules.miniapp.service.WxUserService;
import com.wteam.modules.security.service.OnlineUserService;
import com.wteam.modules.security.util.JwtTokenUtil;
import com.wteam.modules.system.config.LoginType;
import com.wteam.modules.system.domain.Role;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.dto.UserDTO;
import com.wteam.modules.system.service.UserService;
import com.wteam.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mission
 * @since 2020/02/04 3:42
 */
@SuppressWarnings("rawtypes")
@Api(tags={"微信：授权操作"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class WxAuthController {

    private final WxUserService wxUserService;

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    private final PasswordEncoder passwordEncoder;

    private final RedisUtils redisUtils;

    private final OnlineUserService onlineUserService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 登录授权
     * @param wxLoginDTO /
     * @return /
     */
    @ApiOperation(value = "微信用户登录",notes = "先检查微信是否注册，如果注册,则不需要调用获取手机权限")
    @Log("微信用户登录")
    @AnonymousPostMapping(value = "loginWx")
    public R loginWx(@Validated @RequestBody WxLoginDTO wxLoginDTO, HttpServletRequest request){
        String openId = handleSaveUser(wxLoginDTO);

        //登录
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(openId, openId);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //获取jwt
        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        // 是否冻结
        if (!jwtUser.isEnabled()) {
            throw new BadRequestException("该账户已被冻结,请联系管理员");
        }
        // 生成令牌
        String token = jwtTokenUtil.createToken(jwtUser);

        log.debug("昵称:{}-{}",jwtUser.getNickname(),jwtUser.getUsername());

        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);

        //踢掉之前已经登录的token
        onlineUserService.checkLoginOnUser(jwtUser.getUsername(),token);

        //清空code缓存
        redisUtils.del(wxLoginDTO.getCode());

        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token",token);
            put("user", jwtUser);
        }};
        // 返回 token
        return R.ok(authInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    public String handleSaveUser(@RequestBody @Validated WxLoginDTO wxLoginDTO) {
        WxUser wxUser = (WxUser) redisUtils.get(wxLoginDTO.getCode());
        if (wxUser == null) {
            wxUser = wxUserService.load(wxLoginDTO);
            redisUtils.set(wxLoginDTO.getCode(),wxUser);
        }
        String openId = wxUser.getOpenId();
        User user =new User();
        user.setLastLoginTime(Timestamp.valueOf(LocalDateTime.now()));

        //判断是否注册
        if (wxUser.getUid()==null) {
            //创建
            user.setUsername(openId);
            user.setPassword(passwordEncoder.encode(openId));
            user.setNickname(wxUser.getNickName());
            user.setAvatar(wxUser.getAvatarUrl());
            user.setSex(wxUser.getGender());
            user.setPhone(wxUser.getPhoneNumber());
            user.setLoginType(LoginType.LOGIN_WX);
            user.setEnabled(true);
            user.setRoles(Sets.newHashSet(new Role(2L)));
            UserDTO userDTO = userService.create(user);
            wxUser.setUid(userDTO.getId());
            wxUserService.create(wxUser);
        }else {
            //更新
            user.setId(wxUser.getUid());
            userService.update(user);
            wxUserService.update(wxUser);
        }
        return openId;
    }

    /**
     * 登录授权
     * @param wxLoginDTO /
     * @return /
     */
    @ApiOperation(value = "检查微信用户是否注册",notes = "参数只传入 appid,code")
    @AnonymousAccess
    @PostMapping(value = "checkWxReg")
    public R checkWxReg(@Validated @RequestBody WxLoginDTO wxLoginDTO,HttpServletRequest request){
        WxUser wxUser = wxUserService.checkWxReg(wxLoginDTO);
        if (wxUser==null||wxUser.getOpenId()==null) {
          return R.ok(new HashMap<String,Object>(){{
                put("isLogin",false);
            }});
        }
        String openId =wxUser.getOpenId();
        //登录
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(openId, openId);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //获取jwt
        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        // 生成令牌
        String token = jwtTokenUtil.createToken(jwtUser);

        log.debug("昵称:{}-{}",jwtUser.getNickname(),jwtUser.getUsername());

        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);

        //踢掉之前已经登录的token
        onlineUserService.checkLoginOnUser(jwtUser.getUsername(),token);

        //清空code缓存
        redisUtils.del(wxLoginDTO.getCode());
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("isLogin",true);
            put("token",token);
            put("user", jwtUser);
        }};
        return R.ok(authInfo);
    }

}
