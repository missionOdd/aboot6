/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.monitor.service.impl;


import com.wteam.modules.monitor.domain.Visits;
import com.wteam.modules.monitor.repositiory.VisitsRepository;
import com.wteam.modules.monitor.service.VisitsService;
import com.wteam.repository.LogRepository;
import com.wteam.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 访问 实现类
 * @author mission
 * @since 2019/07/11 21:07
 */
@Service
@RequiredArgsConstructor
public class VisitsServiceImpl implements VisitsService {


    private final VisitsRepository visitsRepository;

    private final LogRepository logRepository;


    @Override
    public void save() {
        LocalDate localDate=LocalDate.now();
        Visits visits=visitsRepository.findByDate(localDate.toString());
        if (visits == null) {
            visits =new Visits();
            visits.setWeekDay(StringUtils.getWeekDay());
            visits.setPvCounts(1L);
            visits.setIpCounts(1L);
            visits.setDate(localDate.toString());
            visitsRepository.save(visits);
        }
    }

    @Override
    public void count(HttpServletRequest request) {
        LocalDate localDate=LocalDate.now();
        Visits visits=visitsRepository.findByDate(localDate.toString());
        if (visits==null){
            save();
            return;
        }
        visits.setPvCounts(visits.getPvCounts()+1L);
        long ipCounts=logRepository.findIp(Timestamp.valueOf(localDate.atStartOfDay()),
                Timestamp.valueOf(localDate.plusDays(1).atStartOfDay()));
        visits.setIpCounts(ipCounts);
        visitsRepository.save(visits);
    }

    @Override
    public Object get() {
        Map<String,Object> map = new HashMap<>(4);
        LocalDate localDate = LocalDate.now();
        Visits visits=visitsRepository.findByDate(localDate.toString());
        if (visits==null){
            return map;
        }
        List<Visits> list=visitsRepository.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());

        long recentVisits =0, recentIp =0;
        for (Visits data : list) {
            recentVisits += data.getPvCounts();
            recentIp += data.getIpCounts();
        }
        map.put("newVisits",visits.getPvCounts());
        map.put("newIp",visits.getIpCounts());
        map.put("recentVisits",recentVisits);
        map.put("recentIp",recentIp);

        return map;
    }

    @Override
    public Object getChartData() {
        Map<String,Object> map = new HashMap<>();
        LocalDate localDate = LocalDate.now();
        List<Visits> list = visitsRepository.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());
        if (CollectionUtils.isEmpty(list)){
            return map;
        }
        map.put("weekDays",list.stream().map(Visits::getWeekDay).collect(Collectors.toList()));
        map.put("visitsData",list.stream().map(Visits::getPvCounts).collect(Collectors.toList()));
        map.put("ipData",list.stream().map(Visits::getIpCounts).collect(Collectors.toList()));
        return map;
    }
}
