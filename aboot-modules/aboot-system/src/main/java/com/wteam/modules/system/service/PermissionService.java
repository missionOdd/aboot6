package com.wteam.modules.system.service;


import com.wteam.modules.system.domain.Permission;
import com.wteam.modules.system.domain.criteria.PermissionQueryCriteria;
import com.wteam.modules.system.domain.dto.PermissionDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 权限 业务层
 * @author mission
 * @since 2019/07/13 8:36
 */
public interface PermissionService{


    /**
     * get
     * @param id
     * @return
     */
    PermissionDTO findDTOById(long id);

    /**
     * create
     * @param resources
     * @return
     */
    PermissionDTO create(Permission resources);

    /**
     * update
     * @param resources
     */
    void update(Permission resources);

    /**
     * delete
     * @param ids
     */
    void delete(Long[] ids);

    Set<Permission> getDeletePermissions(List<Permission> permissionList, Set<Permission> permissionSet);
    /**
     * permission tree
     * @return
     */
    Object getPermissionTree(List<Permission> permissions);

    /**
     * findByPid
     * @param pid
     * @return
     */
    List<Permission> findByPid(long pid);

    /**
     * build Tree
     * @param permissionDTOS
     * @return
     */
    Object buildTree(List<PermissionDTO> permissionDTOS);

    /**
     * queryAll
     * @param criteria
     * @return
     */
    List<PermissionDTO> queryAll(PermissionQueryCriteria criteria);

    void download(List<PermissionDTO> queryAll, HttpServletResponse response) throws IOException;

}
