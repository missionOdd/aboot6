package com.wteam.modules.common.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
* 微信配置 DTO类.
* @author aboot-wechat
* @since 2020-02-07
*/
@Data
public class WxConfigDTO implements Serializable {
    /** ID */
    private Long id;
    /** 配置类型 公众号0 小程序1 */
    private Integer type;
    /** 应用ID */
    private String appid;
    /** 应用密钥 */
    private String secret;
    /** 应用Token */
    private String token;
    /** 解密密钥 */
    private String aesKey;
    /** 返回格式 */
    private String msgDataFormat;
    /** 页面logo*/
    private String logo;
    /** 简介*/
    private String remark;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 修改时间 */
    private LocalDateTime editTime;

}