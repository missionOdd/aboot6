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
 * 权限 持久类
 * @author mission
 * @since 2019/07/08 16:27
 */
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name = "sys_permission")
public class Permission extends BaseEntity {

    public static final String ENTITY_NAME = "权限";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    @Column(name = "id")
    private Long id;

    @NotNull
    private String name;


    @NotBlank
    private String alias;

    /**
     * 上级目录
     */
    @NotNull
    @Column(nullable = false)
    private Long parentId;

    /**
     * 所属角色
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    public Permission() {
    }

    public Permission(@NotNull String name, @NotBlank String alias) {
        this.name = name;
        this.alias = alias;
    }
}
