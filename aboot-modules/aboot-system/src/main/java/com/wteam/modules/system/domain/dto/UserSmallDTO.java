package com.wteam.modules.system.domain.dto;

import lombok.Data;

/**
 * @author mission
 * @since 2020/02/11 14:43
 */
@Data
public class UserSmallDTO {


    /**
     * 昵称
     */
    private String nickname;


    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机
     */
    private String phone;
}
