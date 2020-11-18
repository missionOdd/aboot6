package com.wteam.modules.system.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;


/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class RoleDTO implements Serializable {

    private Long id;

    private String name;

    private String authority;

    private String dataScope;

    private Integer level;

    private String remark;

    private Set<PermissionDTO> permissions;

    private Set<MenuDTO> menus;

    private Set<DeptDTO> depts;

    private Timestamp createdAt;
}
