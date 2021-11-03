/*
 * copyleft © 2019-2021
 */

package com.wteam.modules.pay.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
* 微信支付配置 DTO类.
* @author aboot-wechat
* @since 2020-02-07
*/
@Data
public class WxPayConfigDTO implements Serializable {
    /** ID */
    private Long id;
    /** 设置微信公众号或者小程序等的appid */
    private String appid;
    /** 微信支付商户号 */
    private String mchId;
    /** 微信支付商户密钥 */
    private String mchKey;
    /** apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定 */
    private String mchPath;
    /** 注备 */
    private String remark;
    /** 创建时间 */
    private Timestamp createdAt;

}