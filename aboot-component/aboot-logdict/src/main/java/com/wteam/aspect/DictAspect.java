/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.aspect;

import com.google.common.collect.Lists;
import com.wteam.annotation.Dict;
import com.wteam.domain.vo.R;
import com.wteam.domain.criteria.DictQueryCriteria;
import com.wteam.service.DictService;
import com.wteam.utils.HttpHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 字典 AOP
 * @author mission
 * @since 2019/12/30 17:23
 */
@Slf4j
@Aspect
@Component
public class DictAspect {

    private final DictService dictService;

    public DictAspect(DictService dictService) {
        this.dictService = dictService;
    }

    @Pointcut("@annotation(com.wteam.annotation.Dict)")
    public void pointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    @AfterReturning(value = "pointcut()",returning = "result")
    public Object afterReturn(JoinPoint joinPoint, Object result){
        if (result instanceof R) {
            try {
                R<?> msg = (R<?>) result;
                //获取目标对象
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                //获取目标对象上正在执行的方法
                Method signatureMethod = signature.getMethod();
                Dict dict = signatureMethod.getAnnotation(Dict.class);
                String showDict = null;
                switch (dict.showType()){
                    case ALWAYS:
                        //通过注解上的值查询
                        msg.setDict(dictService.queryMap(new DictQueryCriteria(Lists.newArrayList(dict.value()))));
                        break;
                    case PARAMETER:
                        showDict =getShowDictParameter();
                        if (StringUtils.hasText(showDict)&&showDict.equals("1")) {
                            //通过注解上的值查询
                            msg.setDict(dictService.queryMap(new DictQueryCriteria(Lists.newArrayList(dict.value()))));
                        }
                        break;
                    case CUSTOMER:
                        showDict =getShowDictParameter();
                        if (StringUtils.hasText(showDict)) {
                            if (showDict.equals("1")) {
                                //通过注解上的值查询
                                msg.setDict(dictService.queryMap(new DictQueryCriteria(Lists.newArrayList(dict.value()))));

                            }else {
                                //通过参数上的值查询
                                msg.setDict(dictService.queryMap(new DictQueryCriteria(Lists.newArrayList(showDict.split(",")))));

                            }
                        }
                        break;
                }
                return msg;
            } catch (Exception e) {
                log.error("获取字典失败");
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取showDict参数
     */
    private String getShowDictParameter(){
        HttpServletRequest request = HttpHolder.getHttpServletRequest();
        return request.getParameter("showDict");
    }

}
