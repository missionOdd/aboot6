/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.miniapp.config;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.wteam.modules.common.domain.dto.WxConfigDTO;
import com.wteam.modules.common.service.WxConfigService;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.trimToNull;

/**
 * 小程序配置
 * @author mission
 * @since 2020/02/04 5:18
 */
@Configuration
public class WxMaConfiguration {

    private static final Map<String,WxMaService> wxMaServiceMap=new ConcurrentHashMap<>();

    private static WxConfigService wxConfigService;

    public WxMaConfiguration(WxConfigService wxConfigService) {
        WxMaConfiguration.wxConfigService = wxConfigService;
    }

    public static WxMaService getMaService(String appid) {
        WxMaService wxMaService = wxMaServiceMap.get(appid);
        if (wxMaService!=null) {
            return wxMaService;
        }
        wxMaService = new WxMaServiceImpl();
        WxConfigDTO config = wxConfigService.getConfig(appid);
        WxMaDefaultConfigImpl wxMaDefaultConfig =new WxMaDefaultConfigImpl();
        wxMaDefaultConfig.setAppid(trimToNull(config.getAppid()));
        wxMaDefaultConfig.setSecret(trimToNull(config.getSecret()));
        wxMaDefaultConfig.setToken(trimToNull(config.getToken()));
        wxMaDefaultConfig.setAesKey(trimToNull(config.getAesKey()));
        wxMaDefaultConfig.setMsgDataFormat(trimToNull(config.getMsgDataFormat()));
        wxMaService.setWxMaConfig(wxMaDefaultConfig);
        wxMaServiceMap.put(appid,wxMaService);
        return wxMaService;
    }


    public static void cleanCache(String appid){
        wxMaServiceMap.remove(appid);
    }

    public static void cleanAllCache(){
        wxMaServiceMap.clear();
    }
}
