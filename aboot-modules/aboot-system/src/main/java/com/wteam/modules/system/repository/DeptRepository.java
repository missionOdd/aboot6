package com.wteam.modules.system.repository;


import com.wteam.base.BaseRepository;
import com.wteam.modules.system.domain.Dept;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * 部门 存储层
 * @author mission
 * @since 2019/07/08 20:12
 */
public interface DeptRepository extends BaseRepository<Dept, Long> {

    /**
     * findByPid
     * @param id
     * @return
     */
    List<Dept> findByParentId(Long id);

    @Query(value = "select name from sys_dept where id = ?1",nativeQuery = true)
    String findNameById(Long id);

    Set<Dept> findByRoles_Id(Long roleId);
}