package com.wteam.modules.system.service;


import com.wteam.modules.system.domain.Job;
import com.wteam.modules.system.domain.criteria.JobQueryCriteria;
import com.wteam.modules.system.domain.dto.JobDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 岗位 业务层
 * @author mission
 * @since 2019/07/13 10:45
 */
public interface JobService{


    /**
     * findById
     * @param id
     * @return
     */

    JobDTO findDTOById(Long id);

    /**
     * create
     * @param resources
     * @return
     */
    JobDTO create(Job resources);

    /**
     * update
     * @param resources
     */
    void update(Job resources);

    /**
     * delete
     * @param ids
     */
    void delete(Set<Long> ids);

    /**
     * queryAll
     * @param criteria
     * @param pageable
     * @return
     */
    Map<String,Object> queryAll(JobQueryCriteria criteria, Pageable pageable);

    /**
     * queryAll
     * @param criteria /
     * @return
     */
    List<JobDTO> queryAll(JobQueryCriteria criteria);

    /**
     * 导出数据
     * @param queryAll
     * @param response
     */
    void download(List<JobDTO> queryAll, HttpServletResponse response) throws IOException;
}
