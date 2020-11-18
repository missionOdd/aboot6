/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.monitor.service;

import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问 业务层
 * @author mission
 * @since 2019/07/11 21:04
 */
public interface VisitsService {

    /**
     * 提供给定时任务，每天0点执行
     */
    void save();

    /**
     * 新增记录
     * @param request /
     */
    @Async
    void count(HttpServletRequest request);

    /**
     * 获取数据
     * @return
     */
    Object get();

    /**
     * getChartData
     * @return /
     */
    Object getChartData();
}
