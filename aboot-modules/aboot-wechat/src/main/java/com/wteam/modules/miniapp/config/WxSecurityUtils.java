/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.miniapp.config;


import com.wteam.exception.BadRequestException;
import com.wteam.modules.miniapp.domain.WxUser;
import com.wteam.modules.miniapp.service.WxUserService;
import com.wteam.utils.SecurityUtils;
import com.wteam.utils.SpringContextUtil;
import org.springframework.http.HttpStatus;

/**
 * 登录用户工具类
 * @author mission
 * @since 2019/07/08 12:06
 */
public class WxSecurityUtils extends SecurityUtils {

    /**
     * 获取当前用户 ,从缓存获取
     * @return /
     */

    public static WxUser getWxUser() {

        WxUser wxUser = null;
        try {
            WxUserService wxUserService = SpringContextUtil.getBean(WxUserService.class);
            wxUser = wxUserService.findByOpenId(getUsername());
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "登录状态过期");
        }
        return wxUser;
    }


}
