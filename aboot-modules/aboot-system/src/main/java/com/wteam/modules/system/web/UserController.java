/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.system.web;


import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.wteam.annotation.Log;
import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.dto.LocalStorageDTO;
import com.wteam.domain.vo.JwtUser;
import com.wteam.domain.vo.R;
import com.wteam.exception.BadRequestException;
import com.wteam.modules.system.config.DataService;
import com.wteam.modules.system.config.LoginType;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.criteria.UserQueryCriteria;
import com.wteam.modules.system.domain.dto.PasswordDTO;
import com.wteam.modules.system.domain.dto.RoleSmallDTO;
import com.wteam.modules.system.domain.dto.UserEmailDTO;
import com.wteam.modules.system.service.DeptService;
import com.wteam.modules.system.service.RoleService;
import com.wteam.modules.system.service.UserService;
import com.wteam.service.LocalStorageService;
import com.wteam.utils.PageUtil;
import com.wteam.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户 控制层
 * @author mission
 * @since 2019/07/12 15:00
 */
@SuppressWarnings({"rawtypes"})
@Api(value="用户Controller",tags={"系统：用户操作"})
@RequiredArgsConstructor
@RestController
@RequestMapping("api/user")
@PermissionGroup(value = "USER", aliasPrefix = "用户")
public class UserController {

    @Value("${rsa.private-key}")
    private String privateKey;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final LocalStorageService localStorageService;

    private final DataService dataService;

    private final DeptService deptService;

    private final RoleService roleService;


    @ApiOperation(value = "查询用户列表")
    @Log("查询用户")
    @GetMapping("/page")
    @PreAuthorize("@R.check('USER:all','USER:list')")
    public R getUsers(UserQueryCriteria criteria, Pageable pageable){
        Set<Long> deptSet=new HashSet<>();
        Set<Long> result=new HashSet<>();

        if (!ObjectUtils.isEmpty(criteria.getDeptId())){
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataService.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }

        //数据权限
        Set<Long> deptIds =dataService.getDeptIds(SecurityUtils.getId());

        //查询条件不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)){
            result.addAll(deptSet);
            //取交集
            result.retainAll(deptIds);

            criteria.setDeptIds(result);
            //若无交集
            if (result.size() == 0){
                return R.ok(PageUtil.toPage(null,0));
            }else {
                return R.ok(userService.queryAll(criteria,pageable));
            }
            //否则取并集
        }else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            return R.ok(userService.queryAll(criteria,pageable));
        }
    }

    @ApiOperation(value = "新增用户")
    @Log("新增用户")
    @PostMapping("add")
    @PreAuthorize("@R.check('USER:all','USER:edit')")
    public R create(@Validated @RequestBody User resources){
        Assert.isNull(resources.getId(),"实体ID应为空");
        checkLevel(resources);
        //设置密码
        resources.setPassword(passwordEncoder.encode("123456"));
        //设置登录类型
        resources.setLoginType(LoginType.LOGIN_SYSTEM);
        return R.ok(userService.create(resources));
    }

    @ApiOperation(value = "个人中心:用户资料修改")
    @PostMapping("center")
    public R center(@RequestBody User resources){
        resources.setId(SecurityUtils.getId());
        userService.updateCenter(resources);
        return R.ok();
    }


    @ApiOperation(value = "修改用户")
    @Log("修改用户")
    @PostMapping("edit")
    @PreAuthorize("@R.check('USER:all','USER:edit')")
    public R edit(@Validated(User.Update.class) @RequestBody User resources){
        checkLevel(resources);
        userService.update(resources);
        return R.ok();
    }

    /**
     * 修改密码
     * @param resource /
     * @return /
     */
    @ApiOperation(value = "修改密码")
    @PostMapping("updatePassword")
    public R updatePassword(@RequestBody PasswordDTO resource){
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        String oldPass = new String(rsa.decrypt(resource.getOldPass(), KeyType.PrivateKey));
        String newPass = new String(rsa.decrypt(resource.getNewPass(), KeyType.PrivateKey));
        JwtUser jwtUser = (JwtUser) SecurityUtils.getUserDetails();
        if(!passwordEncoder.matches(oldPass, jwtUser.getPassword())){
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if(passwordEncoder.matches(newPass, jwtUser.getPassword())){
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(jwtUser.getUsername(),passwordEncoder.encode(newPass));
        return R.ok();
    }

    /**
     * 修改头像
     * @param file /
     * @return /
     */
    @ApiOperation(value = "修改头像")
    @PostMapping("updateAvatar")
    public R updateAvatar(@RequestParam MultipartFile file){
        LocalStorageDTO storage = localStorageService.create(file.getName(), file);
        userService.updateAvatar(SecurityUtils.getUsername(),storage.getUrl());
        return R.ok();
    }

    /**
     * 修改邮箱
     * @param resources /
     */
    @ApiOperation(value = "修改邮箱")
    @Log("修改邮箱")
    @PostMapping("updateMail")
    public R updateEmail(@RequestBody UserEmailDTO resources){
        // 密码解密
        RSA rsa = new RSA(privateKey, null);
        String password = new String(rsa.decrypt(resources.getPassword(), KeyType.PrivateKey));
        JwtUser jwtUser = (JwtUser) SecurityUtils.getUserDetails();
        if(!passwordEncoder.matches(password, jwtUser.getPassword())){
            throw new BadRequestException("密码错误");
        }
        userService.updateEmail(jwtUser.getUsername(),resources.getEmail());
        return R.ok();
    }

    /**
     * 删除用户
     * @param ids /
     * @return /
     */
    @ApiOperation(value = "批量删除用户")
    @Log("批量删除用户")
    @PostMapping(value = "del")
    @PreAuthorize("@R.check('USER:all','USER:del')")
    public R delete(@RequestBody Set<Long> ids){
        Long currentId = SecurityUtils.getId();
        for (Long id : ids) {
            if (id.equals(currentId)){
                throw new BadRequestException("无法删除自己");
            }
            Integer currentLevel =  Collections.min(roleService.findByUsersId(currentId).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
            Integer optLevel =  Collections.min(roleService.findByUsersId(id).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足,无法删除"+userService.findDTOById(id).getUsername());
            }
        }
        userService.delete(ids);
        return R.ok();
    }


    @ApiOperation(value = "导出用户数据")
    @Log("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('USER:all','USER:list')")
    public void download(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        Set<Long> deptSet=new HashSet<>();
        Set<Long> result=new HashSet<>();
        if (!ObjectUtils.isEmpty(criteria.getDeptId())){
            deptSet.add(criteria.getDeptId());
            deptSet.addAll(dataService.getDeptChildren(deptService.findByPid(criteria.getDeptId())));
        }

        //数据权限
        Set<Long> deptIds =dataService.getDeptIds(SecurityUtils.getId());

        //查询条件不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)){

            result.addAll(deptSet);
            //取交集
            result.retainAll(deptIds);

            criteria.setDeptIds(result);
            //若无交集
            if (result.size() == 0){
                userService.download(new ArrayList<>(), response);
            }else {
                userService.download(userService.queryAll(criteria), response);
            }
            //否则取并集
        }else {
            result.addAll(deptSet);
            result.addAll(deptIds);
            criteria.setDeptIds(result);
            userService.download(userService.queryAll(criteria), response);
        }

    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     * @param resources /
     */
    private void checkLevel(User resources) {
        Integer currentLevel =  Collections.min(roleService.findByUsersId(SecurityUtils.getId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findLevelByUser(resources);
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }

}
