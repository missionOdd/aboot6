package com.wteam.modules.system.repository;

import com.wteam.base.BaseRepository;
import com.wteam.modules.system.domain.Permission;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 权限 存储层
 * @author mission
 * @since 2019/07/08 20:12
 */
public interface PermissionRepository extends BaseRepository<Permission, Long> {

    /**
     * findByName
     * @param name
     * @return
     */
    Permission findByName(String name);


    /**
     * findIdByName
     * @param name
     * @return
     */
    @Query(value = "select id from sys_permission where name = ?1",nativeQuery = true)
    Long findIdByName(String name);

    /**
     * findByPid
     * @param pid
     * @return
     */
    List<Permission> findByParentId(long pid);
}
