package com.wteam.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wteam.base.BaseCons;
import com.wteam.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 用户 持久类
 * @author mission
 * @since 2019/07/08 16:15
 */
@Entity
@Getter
@Setter
@Where(clause = BaseCons.SOFT_DELETE)
@Table(name = "sys_user")
public class User extends BaseEntity {


    public final static String ENTITY_NAME ="账号";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    @Column(name = "id")
    private Long id;


    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Column(nullable = false)
    private String username;


    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Column
    private String nickname;

    /**
     * 密码
     */
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    /**
     * 头像
     */
    @Column
    private String avatar;

    /**
     * 性别
     */
    @Column(columnDefinition = "tinyint(1)  default 0 comment \'性别 0未知,1男,2女\'")
    private Integer sex;

    /**
     * 登录类型
     */
    @Column(name = "login_type",columnDefinition = "tinyint(1) default 0 comment \'用户登录类型'",nullable = false)
    private Integer loginType;

    /**
     * 邮箱
     */
    @Pattern(regexp = "([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}",message = "格式错误")
    private String email;

    /**
     * 电话
     */
    private String phone;


    /**
     * 状态
     */
    private Boolean enabled;

    /**
     * 最后登录时间
     */
    private Timestamp lastLoginTime;

    /**
     * 最后修改密码时间
     */
    private Timestamp lastPasswordResetTime;

    /**
     * 拥有角色
     */
    @NotEmpty
    @ManyToMany
    @JoinTable(name = "sys_users_roles_map",joinColumns ={
        @JoinColumn(name = "user_id",referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")})
    private Set<Role> roles;


    /**
     * 所在岗位
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "job_id",foreignKey = @ForeignKey(name = "none"))
    private Job job;

    /**
     * 所在部门
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "dept_id",foreignKey = @ForeignKey(name = "none"))
    private Dept dept;


    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", nickname='" + nickname + '\'' +
            ", avatar='" + avatar + '\'' +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", enabled=" + enabled +
            ", lastLoginTime=" + lastLoginTime +
            ", lastPasswordResetTime=" + lastPasswordResetTime +
            ", roles=" + roles +
            ", job=" + job +
            ", dept=" + dept +
            '}';
    }
}
