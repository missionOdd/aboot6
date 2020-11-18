/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Context工具
 * @author mission
 * @since 2019/07/14 15:38
 */
@Slf4j
public class SpringContextUtil implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext =null;
    private static final List<CallBack> CALL_BACKS = new ArrayList<>();
    private static boolean addCallback = true;

    /**
     * 针对 某些初始化方法，在SpringContextHolder 未初始化时 提交回调方法。
     * 在SpringContextUtil 初始化后，进行回调使用
     *
     * @param callBack 回调函数
     */
    public synchronized static void addCallBacks(CallBack callBack) {
        if (addCallback) {
            SpringContextUtil.CALL_BACKS.add(callBack);
        } else {
            log.warn("CallBack：{} 已无法添加！立即执行", callBack.getCallBackName());
            callBack.executor();
        }
    }

    /**
     * 取得存储在静态变量中的applicationContext
     */
    public static ApplicationContext getApplicationContext(){
        assertContextInjected();
        return applicationContext;
    }

    /**
     * 从静态变量applicationContext中取得bean,自动转型为所赋值对象的类型
     * @param <T> /
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name){
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量中applicationContext
     * @param requiredType 返回类型
     * @param <T> /
     * @return
     */
    public static <T> T getBean(Class<T> requiredType){
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property     属性key
     * @param defaultValue 默认值
     * @param requiredType 返回类型
     * @return /
     */
    public static <T> T getProperties(String property, T defaultValue, Class<T> requiredType) {
        T result = defaultValue;
        try {
            result = getBean(Environment.class).getProperty(property, requiredType);
        } catch (Exception ignored) {}
        return result;
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property     属性key
     * @param requiredType 返回类型
     * @return /
     */
    public static <T> T getProperties(String property, Class<T> requiredType) {
        return getProperties(property, null, requiredType);
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property 属性key
     * @return /
     */
    public static String getProperties(String property) {
        return getProperties(property, null, String.class);
    }


    /**
     * 清除ApplicationContext
     */
    public static void clearHolder(){
        log.debug("清除SpringContextHolder 中的ApplicationContext:"+applicationContext);
        applicationContext=null;
    }

    /**
     * 销毁ApplicationContext /
     * @throws Exception /
     */
    @Override
    public void destroy() throws Exception {
        SpringContextUtil.clearHolder();
    }

    /**
     * 设置applicationContext
     * @param applicationContext /
     * @throws BeansException  /
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext!=null){
            log.warn("SpringContextHolder中adApplicationContext被覆盖,原有ApplicationContext为:"+ SpringContextUtil.applicationContext);
        }
        SpringContextUtil.applicationContext=applicationContext;
        if (addCallback) {
            for (CallBack callBack : SpringContextUtil.CALL_BACKS) {
                callBack.executor();
            }
            CALL_BACKS.clear();
        }
        SpringContextUtil.addCallback = false;

    }


    /**
     * 检查ApplicationContext不为空
     */
    private static void assertContextInjected(){
        if (applicationContext == null) {
            throw new IllegalArgumentException("applicationContext属性未注入, 请在applicationContext"+
                ".xml中定义springContextHolder或在springboot启动类中注册springContextHolder");
        }
    }
}
