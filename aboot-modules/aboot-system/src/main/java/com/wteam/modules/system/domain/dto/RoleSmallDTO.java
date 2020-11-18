package com.wteam.modules.system.domain.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class RoleSmallDTO implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;
}
