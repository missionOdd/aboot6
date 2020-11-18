package com.wteam.modules.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class UserDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    private String username;

    private String nickname;

    @JsonIgnore
    private String password;

    private String avatar;

    private String email;

    private String phone;

    private Integer sex;

    private Boolean enabled;

    private Integer loginType;


    private Timestamp createdAt;

    private Timestamp lastPasswordResetTime;

    @ApiModelProperty(hidden = true)
    private Set<RoleSmallDTO> roles;

    @ApiModelProperty(hidden = true)
    private JobSmallDTO job;

    private DeptSmallDTO dept;

}
