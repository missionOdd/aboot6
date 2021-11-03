/*
 * copyleft © 2019-2021
 */

package com.wteam.modules.miniapp.domain.dto;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 小程序登录DTO
 * @author mission
 * @since 2020/02/04 3:30
 */
@Data
public class WxLoginDTO {

    @NotBlank(message = "应用appid不能为空")
    private String appid;

    @NotBlank(message = "登录code不能为空")
    private String code;

    private RawData userInfo;

    private RawData phoneInfo;

    private WxMaUserInfo wxUser;

    @Data
    public class RawData {

        private String encryptedData;

        private String iv;
    }
}
