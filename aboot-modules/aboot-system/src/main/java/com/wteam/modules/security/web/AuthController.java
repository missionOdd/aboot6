/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.security.web;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.wteam.annotation.Log;
import com.wteam.annotation.rest.AnonymousGetMapping;
import com.wteam.annotation.rest.AnonymousPostMapping;
import com.wteam.domain.vo.JwtUser;
import com.wteam.domain.vo.R;
import com.wteam.exception.BadRequestException;
import com.wteam.modules.monitor.service.RedisService;
import com.wteam.modules.security.domain.vo.LoginUser;
import com.wteam.modules.security.service.OnlineUserService;
import com.wteam.modules.security.util.JwtTokenUtil;
import com.wteam.modules.system.utils.CaptchaUtil;
import com.wteam.utils.SecurityUtils;
import com.wteam.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 授权、根据token获取用户详细信息
 * @author mission
 * @since 2019/07/11 20:14
 */
@SuppressWarnings("rawtypes")
@Api(tags={"系统：授权操作"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {


    @Value("${jwt.code-key}")
    private String codeKey;
    @Value("${rsa.private-key}")
    private String privateKey;
    @Value("${jwt.single-login}")
    private Boolean singleLogin;

    private final JwtTokenUtil jwtTokenUtil;

    private final RedisService redisService;

    private final OnlineUserService onlineUserService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;



    /**
     * 登录授权
     * @param loginUser /
     * @return /
     */
    @ApiOperation(value = "用户登录")
    @Log("用户登录")
    @AnonymousPostMapping(value = "login")
    public R login(@Validated @RequestBody LoginUser loginUser, HttpServletRequest request){
        // 查询验证码
        String code = redisService.getCodeVal(loginUser.getUuid());
        // 清除验证码
        redisService.delete(loginUser.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(loginUser.getVcode()) || !loginUser.getVcode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        log.info(loginUser.getUsername());

        //密码解密
        RSA rsa = new RSA(privateKey, null);
        String password = new String(rsa.decrypt(loginUser.getPassword(), KeyType.PrivateKey));
        //登录
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser.getUsername().trim(), password);
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

        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);
        if(singleLogin){
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(loginUser.getUsername(),token);
        }

        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token",token);
            put("user", jwtUser);
        }};
        return R.ok(authInfo);
    }

    /**
     * 获取用户信息
     * @return /
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = "info")
    public R getUserInfo(){
        return R.ok(SecurityUtils.getUserDetails());
    }


    /**
     * 获取验证码
     */
    @ApiOperation(value = "获取验证码,返回BASE64格式")
    @AnonymousGetMapping("vCode")
    public Object getCode() {
        CaptchaUtil.Validate v = CaptchaUtil.getRandomCode();
        String result = v.getValue();
        String uuid = codeKey + IdUtil.simpleUUID();
        redisService.saveCode(uuid,result);
        // 返回验证码信息
        return new HashMap<String, String>(2) {{
            put("img", v.getBase64Str());
            put("uuid", uuid);
        }};
    }

    @Log(value = "用户退出")
    @ApiOperation("用户退出")
    @PostMapping(value = "logout")
    public R logout(HttpServletRequest request){
       onlineUserService.logout(jwtTokenUtil.getToken(request));
       return R.ok();
    }

    @ApiOperation(value = "测试使用")
    @AnonymousGetMapping(value = "admin")
    public R login(HttpServletRequest request){
        //登录
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin", "123456");
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        // 生成令牌
        String token = jwtTokenUtil.createToken(jwtUser);
        System.out.println(token);


        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);
        if(singleLogin){
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser("admin",token);
        }
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token",token);
            put("user", jwtUser);
        }};
        return R.ok(authInfo);
    }
}
