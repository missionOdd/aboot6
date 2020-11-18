package com.wteam.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 菜单 存储持久类
 * @author mission
 * @since 2019/07/08 16:31
 */
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name = "sys_menu")
public class Menu extends BaseEntity {

    public static final String ENTITY_NAME = "菜单";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    @Column(name = "id")
    private Long id;

    /**
     * 菜单名
     */
    @NotBlank
    private String name;

    /**
     * 权限标识
     */
    @Column
    private String authorities;

    /**
     * 排序
     */
    @NotNull
    private Long sort;

    /**
     * 链接地址
     */
    @Column
    private String path;

    /**
     * 路由组件
     */
    @Column
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 上级菜单
     */
    @NotNull
    @Column(name = "parent_id",nullable = false)
    private Long parentId;

    /**
     * 是否为外链 true/false
     */
    @Column(name = "i_frame")
    private Boolean iFrame;

    /**
     * 是否缓存
     */
    @Column(name = "cache")
    private Boolean cache;
    /**
     * 是否可见
     */
    @Column(name = "enabled")
    private Boolean enabled;


    /**
     * 所属角色
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "menus")
    private Set<Role> roles;


}
