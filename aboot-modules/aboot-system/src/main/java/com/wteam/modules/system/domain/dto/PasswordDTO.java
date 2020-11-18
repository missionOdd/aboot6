package com.wteam.modules.system.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author mission
 * @since 2019/07/27 16:35
 */
@Data
public class PasswordDTO{

    @NotBlank
    private String oldPass;

    @NotBlank
    private String newPass;

}
