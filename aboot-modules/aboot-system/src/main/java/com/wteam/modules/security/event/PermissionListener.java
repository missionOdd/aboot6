/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.security.event;


import com.wteam.annotation.permission.PermissionData;
import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.modules.system.domain.Permission;
import com.wteam.modules.system.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 启动时 启动时 在数据库中生成权限
 * @author mission
 * @since 2019/07/16 22:03
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "permission",value = {"generate"},havingValue = "true")
@RequiredArgsConstructor
public class PermissionListener implements ApplicationListener<ContextRefreshedEvent> {

    private final PermissionRepository permissionRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(PermissionGroup.class);
        //遍历Bean
        Set<Map.Entry<String, Object>> entries = beans.entrySet();
        for (Map.Entry<String, Object> map : entries) {
            Class<?> aClass = map.getValue().getClass();
            PermissionGroup permissionGroup = AnnotationUtils.findAnnotation(aClass, PermissionGroup.class);
            if (null != permissionGroup) {
                //获取注解数据
                String value = permissionGroup.value();
                String prefix = permissionGroup.aliasPrefix();
                String parentName = permissionGroup.parent();
                //插入到数据库中
                savePermission(new Permission(value + ":all", prefix + "管理"), parentName);
                savePermission(new Permission(value + ":add", prefix + "创建"), value + ":all");
                savePermission(new Permission(value + ":edit", prefix + "编辑"), value + ":all");
                savePermission(new Permission(value + ":list", prefix + "查询"), value + ":all");
                savePermission(new Permission(value + ":del", prefix + "删除"), value + ":all");
            }

            Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(aClass);
            for (Method method : methods) {
                PermissionData permissionData = AnnotationUtils.findAnnotation(method, PermissionData.class);
                if (null != permissionData) {
                    //插入到数据库中
                    savePermission(new Permission(permissionData.value(), permissionData.alias()), permissionData.parent());
                }
            }
        }
    }

    private void savePermission(Permission resources,String parentName){
        if (!parentName.equals("")){
            Long parent=permissionRepository.findIdByName(parentName);
            if (parent != null){
                resources.setParentId(parent);
            }else {
                resources.setParentId(0L);
            }
        }else {
            resources.setParentId(0L);
        }
        Permission permission = permissionRepository.findByName(resources.getName());
        if (permission !=null){
            if (permission.getAlias().equals(resources.getAlias())&&permission.getParentId().equals(resources.getParentId())){
                return;
            }else {
                resources.setId(permission.getId());
                resources.setRoles(permission.getRoles());
            }
        }
        log.info("设置权限: {}------{}",resources.getName(),resources.getAlias());
        permissionRepository.save(resources);
    }

}
