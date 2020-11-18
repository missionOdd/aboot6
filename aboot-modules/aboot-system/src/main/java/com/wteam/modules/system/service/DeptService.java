package com.wteam.modules.system.service;

import com.wteam.modules.system.domain.Dept;
import com.wteam.modules.system.domain.criteria.DeptQueryCriteria;
import com.wteam.modules.system.domain.dto.DeptDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 部门 业务层
 * @author mission
 * @since 2019/07/13 9:43
 */
public interface DeptService{


    /**
     * queryAll
     * @param criteria
     * @return
     */
    List<DeptDTO> queryAll(DeptQueryCriteria criteria);

    /**
     * findById
     * @param id
     * @return
     */
    DeptDTO findDTOById(Long id);

    /**
     * create
     * @param resources
     * @return
     */

    DeptDTO create(Dept resources);

    /**
     * update
     * @param resources
     */
    void update(Dept resources);

    /**
     * delete
     * @param ids /
     */
    void delete(Long[] ids);



    /**
     * buildTree
     * @param deptDTOS
     * @return
     */
    Object buildTree(List<DeptDTO> deptDTOS);

    /**
     * findByPid
     * @param pid
     * @return
     */
    List<Dept> findByPid(long pid);

    Set<Dept> findByRoleIds(Long id);

    /**
     * 获取待删除的企业
     * @param deptList
     * @param deptSet
     * @return
     */
    Set<Dept> getDeleteDepts(List<Dept> deptList, Set<Dept> deptSet);


    void download(List<DeptDTO> queryAll, HttpServletResponse response) throws IOException;
}
