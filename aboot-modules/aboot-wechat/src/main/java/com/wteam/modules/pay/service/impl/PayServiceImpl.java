package com.wteam.modules.pay.service.impl;


import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.wteam.exception.BadRequestException;
import com.wteam.modules.pay.config.WxPayConfiguration;
import com.wteam.modules.pay.domain.PayLog;
import com.wteam.modules.pay.service.PayLogService;
import com.wteam.modules.pay.service.PayService;
import com.wteam.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mission
 * @since 2020/03/24 21:18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    private final PayLogService payLogService;

    @Override
    public WxPayMpOrderResult createOrder(WxPayUnifiedOrderRequest request, HttpServletRequest httpRequest) {
        PayLog payLog =new PayLog();
        long startTime = System.currentTimeMillis();
        WxPayService wxPayService = WxPayConfiguration.getPayService(request.getAppid());
        String ip = StringUtils.getIP(httpRequest);
        WxPayMpOrderResult order = null;
        try {
            order = wxPayService.createOrder(request);
        } catch (WxPayException e) {
            e.printStackTrace();
            long time =System.currentTimeMillis()-startTime;
            payLog.setTime(time);
            payLog.setAppId(request.getAppid());
            payLog.setAppOrderId(request.getOutTradeNo());
            payLog.setExceptionDetail(e.getCustomErrorMsg());
            payLog.setUsername(request.getOpenid());
            payLog.setMethod(httpRequest.getMethod());
            payLog.setParams(request.toString());
            payLog.setLogType("unifiedorder");
            payLog.setRequestIp(ip);
            payLog.setAddress(StringUtils.getCityInfo(ip));
            payLog.setBrowser(StringUtils.getBrowser(httpRequest));
            payLog.setExceptionDetail(e.getCustomErrorMsg());
            payLogService.create(payLog);
            throw new BadRequestException(e.getCustomErrorMsg());
        }
        return order;
    }

    @Override
    public WxPayOrderQueryResult queryOrder(String appid, String tradeNo) {
        PayLog payLog =new PayLog();
        long startTime = System.currentTimeMillis();
        WxPayService wxPayService = WxPayConfiguration.getPayService(appid);
        WxPayOrderQueryResult queryResult = null;
        try {
             queryResult = wxPayService.queryOrder(null, tradeNo);
            if (queryResult.getTradeState().equals(WxPayConstants.WxpayTradeStatus.SUCCESS)) {
                log.debug("支付成功--{}",tradeNo);
            }
        } catch (WxPayException e) {
            e.printStackTrace();
            long time =System.currentTimeMillis()-startTime;
            payLog.setTime(time);
            payLog.setAppId(appid);
            payLog.setAppOrderId(tradeNo);
            payLog.setExceptionDetail(e.getCustomErrorMsg());
            payLog.setUsername("系统");
            payLog.setMethod("post");
            payLog.setParams(tradeNo);
            payLog.setLogType("orderquery");
            payLog.setExceptionDetail(e.getCustomErrorMsg());
            payLogService.create(payLog);
            throw new BadRequestException(e.getCustomErrorMsg());
        }
        return queryResult;
    }
}
