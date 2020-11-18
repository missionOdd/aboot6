package com.wteam.modules.pay.service;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mission
 * @since 2020/03/24 21:14
 */
public interface PayService {

    /**
     * 预支付
     * @param request
     * @param httpRequest
     * @return
     */
     WxPayMpOrderResult createOrder(WxPayUnifiedOrderRequest request, HttpServletRequest httpRequest);


    /**
     * 查询订单
     * @param appid /
     * @param tradeNo /
     * @return /
     */
     WxPayOrderQueryResult queryOrder(String appid, String tradeNo);
}
