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
 * 权限组生成 注解
 * @author mission
 * @since 2019/07/16 22:04
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionGroup {

    /*权限组名*/
    String value();

    /*权限别名前缀,中文名*/
    String aliasPrefix();

    /*权限父级名称,若parent为"",则该权限为顶级权限*/
    String parent() default "";

}
