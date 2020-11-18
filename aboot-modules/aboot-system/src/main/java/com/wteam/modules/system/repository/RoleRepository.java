package com.wteam.modules.system.repository;


import com.wteam.base.BaseRepository;
import com.wteam.modules.system.domain.Role;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 角色 存储层
 * @author mission
 * @since 2019/07/08 20:31
 */
public interface RoleRepository extends BaseRepository<Role, Long> {

    /**
     * findByName
     * @param name
     * @return
     */
    Role findByName(String name);

    Set<Role> findByUsers_Id(Long users_id);

    Set<Role> findByMenus_Id(Long menus_id);

    /**
     * 根据部门查询
     * @param deptIds /
     * @return /
     */
    @Query(value = "select count(1) from sys_role r, sys_roles_depts_map d where " +
            "r.id = d.role_id and d.dept_id in ?1",nativeQuery = true)
    int countByDepts(Set<Long> deptIds);

    /**
     * 根据菜单Id查询
     * @param menuIds /
     * @return /
     */
    @Query(value = "SELECT r.* FROM sys_role r, sys_roles_menus_map m WHERE " +
            "r.id = m.role_id AND m.menu_id in ?1",nativeQuery = true)
    List<Role> findInMenuId(List<Long> menuIds);

    @Query(value = "SELECT r.* FROM sys_role r, sys_roles_permissions_map m WHERE " +
            "r.id = m.role_id AND m.permission_id in ?1",nativeQuery = true)
    List<Role> findInPermissionId(ArrayList<Long> permissionIds);

}
