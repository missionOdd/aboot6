/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.annotation.permission;

import java.lang.annotation.*;

/**
 * 标记匿名访问方法
 * @author mission
 * @since 2019/12/07 11:41
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionData {

    /*权限名*/
    String value();

    /*权限别名*/
    String alias();

    /*权限父级名称, 若parent为"",则该权限为顶级权限*/
    String parent() default "";
}
