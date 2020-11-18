package com.wteam.modules.system.repository;


import com.wteam.base.BaseRepository;
import com.wteam.modules.system.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * 用户 存储层
 * @author mission
 * @since 2019/07/08 20:31
 */
public interface UserRepository extends BaseRepository<User, Long> {


    /**
     * findByUsername
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * findByEmail
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 根据电话登录
     * @param phone
     * @return
     */
    User findByPhone(String phone);


    /**
     * 修改密码
     * @param username
     * @param pass
     */
    @Modifying
    @Query(value = "update sys_user set password = ?2 , last_password_reset_time = ?3 where username = ?1",nativeQuery = true)
    void updatePass(String username, String pass, Timestamp lastPasswordResetTime);

    /**
     * 修改头像
     * @param username
     * @param url
     */
    @Modifying
    @Query(value = "update sys_user set avatar = ?2 where username = ?1",nativeQuery = true)
    void updateAvatar(String username, String url);

    /**
     * 修改邮箱
     * @param username
     * @param email
     */
    @Modifying
    @Query(value = "update sys_user set email = ?2 where username = ?1",nativeQuery = true)
    void updateEmail(String username, String email);


    /**
     * 登录,更新登录时间
     * @param username
     * @param loginTime
     */
    @Modifying
    @Query(value = "update sys_user set last_login_time = ?2 where username = ?1",nativeQuery = true)
    void updateLoginTime(String username, Timestamp loginTime);


    /**
     * 根据角色查询用户
     * @param roleId /
     * @return /
     */
    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles_map r WHERE" +
            " u.id = r.user_id AND r.role_id = ?1", nativeQuery = true)
    List<User> findByRoleId(Long roleId);

    /**
     * 根据角色中的部门查询
     * @param id /
     * @return /
     */
    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles_map r, sys_roles_depts_map d WHERE " +
            "u.id = r.user_id AND r.role_id = d.role_id AND r.user_id = ?1 group by u.id", nativeQuery = true)
    List<User> findByDeptRoleId(Long id);

    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles_map ur, sys_roles_permissions_map rm WHERE " +
            "u.id = ur.user_id AND ur.role_id = rm.role_id AND rm.permission_id = ?1 group by u.id", nativeQuery = true)
    List<User> findByPermissionId(Long id);
    /**
     * 根据菜单查询
     * @param id 菜单ID
     * @return /
     */
    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles_map ur, sys_roles_menus_map rm WHERE " +
            "u.id = ur.user_id AND ur.role_id = rm.role_id AND rm.menu_id = ?1 group by u.id", nativeQuery = true)
    List<User> findByMenuId(Long id);


    Long countByLoginTypeAndSex(Integer loginType, Integer sex);

    Long countByLoginType(Integer loginType);

    /**
     * 根据岗位查询
     * @param ids /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u, sys_job j WHERE u.job_id = j.id AND j.id IN ?1", nativeQuery = true)
    int countByJobs(Set<Long> ids);

    /**
     * 根据部门查询
     * @param deptIds /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u WHERE u.dept_id IN ?1", nativeQuery = true)
    int countByDepts(Set<Long> deptIds);


    /**
     * 根据角色查询
     * @param ids /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM  sys_user u, sys_users_roles_map r WHERE " +
            "u.id = r.user_id AND r.role_id in ?1", nativeQuery = true)
    int countByRoles(Set<Long> ids);


}
