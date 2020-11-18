/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.wteam.annotation.Log;
import com.wteam.aspect.LogType;
import com.wteam.domain.criteria.LogQueryCriteria;
import com.wteam.domain.mapper.LogErrorMapper;
import com.wteam.domain.mapper.LogSmallMapper;
import com.wteam.repository.LogRepository;
import com.wteam.service.LogService;
import com.wteam.utils.FileUtil;
import com.wteam.utils.PageUtil;
import com.wteam.utils.QueryHelper;
import com.wteam.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志 业务实现层
 * @author mission
 * @since 2019/07/08 11:00
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;


    @Override
    public Object queryAll(LogQueryCriteria criteria, Pageable pageable) {
        Page<com.wteam.domain.Log> page=logRepository.findAll(((root, criteriaQuery, cb)-> QueryHelper.andPredicate(root,criteria,cb)), pageable);
        if (LogType.ERROR.name().equals(criteria.getLogType())){
            return PageUtil.toPage(page.map(LogErrorMapper.INSTANCE::toDto));
        }
        return PageUtil.toPage(page);
    }

    @Override
    public List<com.wteam.domain.Log> queryAll(LogQueryCriteria criteria) {
        return logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelper.andPredicate(root, criteria, cb)));
    }

    @Override
    public Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable) {
        Page<com.wteam.domain.Log> page=logRepository.findAll(
            (root,criteriaQuery,cb)->QueryHelper.andPredicate(root,criteria,cb),
            pageable);
        return PageUtil.toPage(page.map(LogSmallMapper.INSTANCE::toDto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, com.wteam.domain.Log logEntity) {

        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        Method method=signature.getMethod();
        Log aopLog =method.getAnnotation(Log.class);
        //描述
        if (logEntity!=null){
            logEntity.setDescription(aopLog.value());
        }

        //方法路径
        String methodName=joinPoint.getTarget().getClass().getName()
            +"."+signature.getName()+"()";
        StringBuilder params = new StringBuilder("{");

        //参数值
        Object[] argValues=joinPoint.getArgs();
        //参数名称
        String[] argNames=((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (argValues!=null){
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }

        //获取IP地址
        assert logEntity != null;
        logEntity.setRequestIp(ip);
        String loginPath = "login";
        if (loginPath.equals(signature.getName())){
            try {
                assert argValues != null;
                username = new JSONObject(argValues[0]).getStr("username");
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }
        logEntity.setAddress(StringUtils.getCityInfo(logEntity.getRequestIp()));
        logEntity.setMethod(methodName);
        logEntity.setUsername(username);
        logEntity.setParams(params.toString() + " }");
        logEntity.setBrowser(browser);
        logRepository.save(logEntity);

    }

    @Override
    public Object findByErrDetail(Long id) {
        return Dict.create().set("exception",logRepository.findExceptionById(id));
    }



    @Override
    public void download(List<com.wteam.domain.Log> logs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (com.wteam.domain.Log log : logs) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户名", log.getUsername());
            map.put("IP", log.getRequestIp());
            map.put("IP来源", log.getAddress());
            map.put("描述", log.getDescription());
            map.put("浏览器", log.getBrowser());
            map.put("请求耗时/毫秒", log.getTime());
            map.put("异常详情", new String(ObjectUtil.isNotNull(log.getExceptionDetail()) ? log.getExceptionDetail() : "".getBytes()));
            map.put("创建日期", log.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByError() {
        logRepository.deleteByLogType(LogType.ERROR.name());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByInfo() {
        logRepository.deleteByLogType(LogType.INFO.name());
    }
}
