/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.service;


import com.wteam.domain.Log;
import com.wteam.domain.criteria.LogQueryCriteria;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 日志 业务层
 * @author mission
 * @since 2019/07/08 9:44
 */
public interface LogService{


    /**
     * 分页查询
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(LogQueryCriteria criteria, Pageable pageable);



    /**
     * 查询全部数据
     * @param criteria 查询条件
     * @return /
     */
    List<Log> queryAll(LogQueryCriteria criteria);
    /**
     * queryAllByUser
     * @param criteria
     * @param pageable
     * @return
     */
    Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable);

    /**
     * 新增日志
     * @param username
     * @param ip
     * @param joinPoint
     * @param log
     */
    @Async
    void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, Log log);

    /**
     * 查询异常详情
     * @param id
     * @return
     */
    Object findByErrDetail(Long id);

    /**
     * 导出日志
     * @param queryAll
     * @param response
     * @throws IOException
     */
    void download(List<Log> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 清空异常日志
     */
    void delAllByError();

    /**
     * 清空操作日志
     */
    void delAllByInfo();
}
