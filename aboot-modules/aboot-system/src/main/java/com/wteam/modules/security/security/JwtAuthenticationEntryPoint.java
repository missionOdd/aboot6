
/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.security.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zheng Jie
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException{

        /**
         * 当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送401 响应
         */
        String errorMsg="无权访问,请登录";
        if (e !=null) {
            errorMsg=errorMsg+" {\"reason\":\""+e.getMessage()+"\"}";
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,errorMsg);
    }
}
