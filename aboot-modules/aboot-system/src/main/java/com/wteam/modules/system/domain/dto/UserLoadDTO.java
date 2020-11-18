package com.wteam.modules.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class UserLoadDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    private String username;

    private String nickname;

    private String password;

    private String phone;

    private Integer sex;

    private Boolean enabled;

    private Integer loginType;

    private Long merId;

}
