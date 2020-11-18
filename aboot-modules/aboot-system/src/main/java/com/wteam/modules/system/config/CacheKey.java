
/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.modules.system.config;

/**
 * @author mission
 * @since 2019/07/07 11:51
 * @apiNote: 关于缓存的Key集合
 */
public interface CacheKey {

    /**
     * 内置 用户、岗位、应用、菜单、角色 相关key
     */
    String USER_MODIFY_TIME_KEY = "user:modify:time:key:";
    String APP_MODIFY_TIME_KEY = "app:modify:time:key:";
    String JOB_MODIFY_TIME_KEY = "job:modify:time:key:";
    String MENU_MODIFY_TIME_KEY = "menu:modify:time:key:";
    String ROLE_MODIFY_TIME_KEY = "role:modify:time:key:";
    String DEPT_MODIFY_TIME_KEY = "dept:modify:time:key:";

    /**
     * 用户
     */
    String USER_ID = "user::id:";
    String USER_NAME = "user::loadUserByUsername:";
    /**
     * 数据
     */
    String DATA_USER = "data::user:";

    /**
     * 角色
     */
    String ROLE_USER = "role::user:";
    /**
     * 菜单
     */
    String MENU_USER = "menu::user:";

    /**
     * 权限
     */
    String PERMISSION_USER = "menu::user:";
    /**
     * 角色信息
     */
    String ROLE_ID = "role::id:";

    /**
     * 角色信息
     */
    String PERMISSION_ID = "permission::id:";

    /**
     * 角色信息
     */
    String MENU_ID = "menu::id:";
}
