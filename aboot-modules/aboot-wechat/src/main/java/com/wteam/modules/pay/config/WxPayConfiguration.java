/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.pay.config;


import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.wteam.modules.pay.domain.dto.WxPayConfigDTO;
import com.wteam.modules.pay.service.WxPayConfigService;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.trimToNull;

/**
 * @author mission
 * @since  2020/02/04 5:18
 */
@Configuration
public class WxPayConfiguration {

    private static final Map<String, WxPayService> wxPayServiceMap=new ConcurrentHashMap<>();

    private static WxPayConfigService wxPayConfigService;

    public WxPayConfiguration(WxPayConfigService wxPayConfigService) {
        WxPayConfiguration.wxPayConfigService = wxPayConfigService;
    }

    public static WxPayService getPayService(String appid) {
        WxPayService wxPayService = wxPayServiceMap.get(appid);
        if (wxPayService!=null) {
            return wxPayService;
        }
        WxPayConfigDTO wxPayConfig = wxPayConfigService.findDTOByAppid(appid);
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(trimToNull(wxPayConfig.getAppid()));
        payConfig.setMchId(trimToNull(wxPayConfig.getMchId()));
        payConfig.setMchKey(trimToNull(wxPayConfig.getMchKey()));
        payConfig.setKeyPath(trimToNull(wxPayConfig.getMchPath()));

        wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        wxPayServiceMap.put(appid,wxPayService);
        return wxPayService;
    }


    public static void cleanCache(String appid){
        wxPayServiceMap.remove(appid);
    }

    public static void cleanAllCache(){
        wxPayServiceMap.clear();
    }
}
