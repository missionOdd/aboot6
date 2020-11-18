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
 * 部门 持久类
 * @author mission
 * @since 2019/07/08 16:39
 */
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name = "sys_dept")
public class Dept extends BaseEntity {


    public static final String ENTITY_NAME = "部门";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    @Column(name = "id")
    private Long id;
    /**
     * 名称
     */
    @Column(nullable =false)
    @NotBlank(message = "部门名称不能为空")
    private String name;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled;

    /**
     * 上级部门
     */
    @Column(name = "parent_id",nullable = false)
    @NotNull(message = "请设置父类")
    private Long parentId;

    @JsonIgnore
    @ManyToMany(mappedBy = "depts")
    private Set<Role> roles;

}
