package com.wteam.modules.system.config;

/**
 * 用户登录类型
 * @author mission
 * @since 2019/09/04 22:19
 */
public interface LoginType {

    /*超级管理*/
    int LOGIN_SYSTEM=0;

    /*系统管理*/
    int LOGIN_ADMIN=1;

    /*微信用户*/
    int LOGIN_WX=2;

}
