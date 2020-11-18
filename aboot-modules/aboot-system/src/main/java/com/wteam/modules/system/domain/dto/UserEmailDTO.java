package com.wteam.modules.system.domain.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * 修改邮箱
 * @author mission
 * @since 2019/07/27 19:44
 */
@Data
public class UserEmailDTO {

    /**
     * 密码
     */
    @NotNull
    private String password;

    /**
     * 邮箱
     */
    @Email
    private String email;
}
