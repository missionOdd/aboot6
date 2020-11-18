package com.wteam.modules.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class UserPhoneDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    private String phone;


    private String vcode;

    private String uuid = "";

}
