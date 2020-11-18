package com.wteam.modules.system.domain;

import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 岗位 持久类
 * @author mission
 * @since 2019/07/08 16:48
 */
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name = "sys_job")
public class Job extends BaseEntity {

    public static final String ENTITY_NAME = "岗位";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    @Column(name = "id")
    private Long id;

    /**
     * 岗位名
     */
    private String name;


    @NotNull
    private Long sort;

    /**
     * 状态
     */
    @Column(nullable = false)
    private Boolean enabled;


    /**
     * 所属部门
     */
    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Dept dept;


}
