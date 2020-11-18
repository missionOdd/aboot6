/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.pay.service.impl;


import com.wteam.modules.pay.domain.PayLog;
import com.wteam.modules.pay.domain.criteria.PayLogQueryCriteria;
import com.wteam.modules.pay.domain.dto.PayLogDTO;
import com.wteam.modules.pay.domain.mapper.PayLogMapper;
import com.wteam.modules.pay.repository.PayLogRepository;
import com.wteam.modules.pay.service.PayLogService;
import com.wteam.utils.FileUtil;
import com.wteam.utils.PageUtil;
import com.wteam.utils.QueryHelper;
import com.wteam.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* 交易日志表 业务实现层.
* @author mission
* @since 2020-03-18
*/
@Service
@RequiredArgsConstructor
public class PayLogServiceImpl implements PayLogService {

    private final PayLogRepository payLogRepository;

    private final PayLogMapper payLogMapper;


    @Override
    public Map<String,Object> queryAll(PayLogQueryCriteria criteria, Pageable pageable){
        Page<PayLog> page = payLogRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(payLogMapper::toDto));
    }

    @Override
    public List<PayLogDTO> queryAll(PayLogQueryCriteria criteria){
        return payLogMapper.toDto(payLogRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public PayLogDTO findDTOById(Long id) {
        PayLog payLog = payLogRepository.findById(id).orElse(null);
        ValidUtil.notNull(payLog,PayLog.ENTITY_NAME,"id",id);
        return payLogMapper.toDto(payLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayLogDTO create(PayLog resources) {
    return payLogMapper.toDto(payLogRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PayLog resources) {
        PayLog payLog = payLogRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull( payLog,PayLog.ENTITY_NAME,"id",resources.getId());

        payLog.copy(resources);
        payLogRepository.save(payLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            payLogRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<PayLogDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PayLogDTO payLog : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("应用id", payLog.getAppId());
            map.put("应用方订单号", payLog.getAppOrderId());
            map.put("本次交易唯一id，整个支付系统唯一，生成他的原因主要是 order_id对于其它应用来说可能重复", payLog.getTransactionId());
            map.put("异常详细", payLog.getExceptionDetail());
            map.put("操作用户", payLog.getUsername());
            map.put("请求耗时", payLog.getTime());
            map.put("方法名", payLog.getMethod());
            map.put("支付的请求参数", payLog.getParams());
            map.put("日志类型，payment:支付; refund:退款; notify:异步通知; return:同步通知; query:查询", payLog.getLogType());
            map.put("请求ip", payLog.getRequestIp());
            map.put("请求地址", payLog.getAddress());
            map.put("浏览器", payLog.getBrowser());
            map.put("创建时间", payLog.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}