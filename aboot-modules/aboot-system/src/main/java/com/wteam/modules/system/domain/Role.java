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
 * 角色 持久类
 * @author mission
 * @since 2019/07/08 16:20
 */
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name = "sys_role")
public class Role extends BaseEntity {

    public static final String ENTITY_NAME = "角色";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    @Column(name = "id")
    private Long id;
    /**
     * 角色名
     */
    @Column(nullable = false)
    @NotBlank
    private String name;

    /**
     * 权限标识
     */
    @Column
    private String authority;

    // 数据权限类型 全部 、 本级 、 自定义
    @Column(nullable = false)
    private String dataScope="本级";

    //数值越小,级别越大
    @Column
    private Integer level=3;

    /**
     * 备注
     */
    @Column
    private String remark;

    /**
     * 所属用户
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    /**
     * 拥有权限
     */
    @ManyToMany
    @JoinTable(name = "sys_roles_permissions_map",
        joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "permission_id",referencedColumnName = "id")})
    private Set<Permission> permissions;

    /**
     * 拥有菜单
     */
    @ManyToMany
    @JoinTable(name = "sys_roles_menus_map",
        joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "menu_id",referencedColumnName = "id")})
    private Set<Menu> menus;

    /**
     * 管理部门
     */
    @ManyToMany
    @JoinTable(name = "sys_roles_depts_map",
        joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "dept_id",referencedColumnName = "id")})
    private Set<Dept> depts;

    public Role() {
    }

    public Role(Long id) {
        this.id = id;
    }
}
