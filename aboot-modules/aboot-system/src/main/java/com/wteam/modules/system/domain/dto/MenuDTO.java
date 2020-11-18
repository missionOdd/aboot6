package com.wteam.modules.system.domain.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class MenuDTO {

    private Long id;

    private String name;

    private String authorities;

    private Long sort;

    private String path;

    private String component;

    private Long parentId;

    private Boolean iFrame;

    private Boolean cache;

    private String icon;

    private Boolean enabled;

    private List<MenuDTO> children;

    private Timestamp createdAt;
}
