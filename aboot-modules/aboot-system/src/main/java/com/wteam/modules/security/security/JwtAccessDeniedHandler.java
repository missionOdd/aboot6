/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.security.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mission
 * @since 2019/12/26 15:01
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
      //当用户在没有授权的情况下访问受保护的REST资源时，将调用此方法发送403 Forbidden响应
      String errorMsg="权限不足,无法操作";
      if (e !=null) {
         errorMsg=errorMsg+" {\"reason\":\""+e.getMessage()+"\"}";
      }
      response.sendError(HttpServletResponse.SC_FORBIDDEN, errorMsg);
   }
}