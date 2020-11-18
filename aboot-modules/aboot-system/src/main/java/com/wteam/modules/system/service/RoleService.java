package com.wteam.modules.system.service;


import com.wteam.modules.system.domain.Menu;
import com.wteam.modules.system.domain.Permission;
import com.wteam.modules.system.domain.Role;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.criteria.RoleQueryCriteria;
import com.wteam.modules.system.domain.dto.RoleDTO;
import com.wteam.modules.system.domain.dto.RoleSmallDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 角色 业务层
 * @author mission
 * @since 2019/07/09 15:30
 */
public interface RoleService{

    /**
     * get
     * @param id
     * @return
     */
    RoleDTO findDTOById(long id);

    /**
     * create
     * @param resources /
     * @return /
     */
    RoleDTO create(Role resources);

    /**
     * update
     * @param resources /
     */
    void update(Role resources);

    /**
     * delete
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * key的名称如有修改，请同步修改 UserServiceImpl 中的 update 方法
     * findByUsersId
     * @param id /
     * @return /
     */
    List<RoleSmallDTO> findByUsersId(Long id);

    /**
     * 根据用户查询角色级别
     * @param user /
     * @return
     */
    Integer findLevelByUser(User user);

    /**
     * 获取绑定菜单角色
     * @param menuIds /
     * @return
     */
    List<RoleSmallDTO> findInMenuId(ArrayList<Long> menuIds);

    /**
     * 获取绑定权限角色
     * @param permissionIds /
     * @return
     */
    List<RoleSmallDTO> findInPermissionId(ArrayList<Long> permissionIds);
    /**
     * updatePermission
     * @param resources
     */
    void updatePermission(Role resources);

    /**
     * updateMenu
     * @param resources
     */
    void updateMenu(Role resources);

    /**
     * 解绑菜单
     * @param menu /
     */
    void untiedMenu(Menu menu);


    /**
     * 解绑权限
     * @param permission /
     */
    void untiedPermission(Permission permission);

    /**
     * queryAll
     * @param pageable
     * @return
     */
    Object queryAll(Pageable pageable);

    /**
     * queryAll
     * @param pageable
     * @param criteria
     * @return
     */
    Map<String, Object> queryAll(RoleQueryCriteria criteria, Pageable pageable);


    /**
     * queryAll
     * @param criteria
     * @return
     */
    List<RoleDTO> queryAll(RoleQueryCriteria criteria);

    /**
     * 导出数据
     * @param list 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<RoleDTO> list, HttpServletResponse response) throws IOException;



}
