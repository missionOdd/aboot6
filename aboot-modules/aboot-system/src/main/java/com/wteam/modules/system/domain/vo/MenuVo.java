package com.wteam.modules.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建前端路由时用到
 * @author Zheng Jie
 * @since 2018-12-20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVo implements Serializable {

    private Long id;

    private String name;

    private String authorities;

    private Boolean cache;

    private Boolean iFrame;

    private String path;

    private Long parentId;

    private String redirect;

    private String component;

    private Boolean enabled;

    private Boolean alwaysShow;

    private MenuMetaVo meta;

    private List<MenuVo> children;
}
