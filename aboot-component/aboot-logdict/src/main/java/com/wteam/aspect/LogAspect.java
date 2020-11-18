/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.aspect;

import com.wteam.domain.Log;
import com.wteam.service.LogService;
import com.wteam.utils.HttpHolder;
import com.wteam.utils.SecurityUtils;
import com.wteam.utils.StringUtils;
import com.wteam.utils.ThrowableUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志 Aop
 * @author mission
 * @since 2019/07/08 9:40
 */
@Component
@Aspect
public class LogAspect {

    private final LogService logService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.wteam.annotation.Log)")
    public void logPointcut(){}

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable{
        Object result =null;
        currentTime.set(System.currentTimeMillis());
        result=joinPoint.proceed();
        Log log=new Log(LogType.INFO.name(),System.currentTimeMillis() - currentTime.get());
        HttpServletRequest request = HttpHolder.getHttpServletRequest();
        logService.save(getUsername(),  StringUtils.getBrowser(request), StringUtils.getIP(request),joinPoint,log);
        return result;
    }

    /**
     * 配置异常通知
     * @param joinPoint join point for advice
     * @param e Throwable
     */
    @AfterThrowing(pointcut = "logPointcut()",throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e){
        Log log=new Log(LogType.ERROR.name(),System.currentTimeMillis());
        log.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
        HttpServletRequest request = HttpHolder.getHttpServletRequest();
        logService.save(getUsername(), StringUtils.getBrowser(request), StringUtils.getIP(request),(ProceedingJoinPoint)joinPoint,log);
    }

    public String getUsername() {
        try {
            return SecurityUtils.getUsername();
        }catch (Exception e){
            return "";
        }
    }
}
