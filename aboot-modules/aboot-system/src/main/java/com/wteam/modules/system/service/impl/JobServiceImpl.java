package com.wteam.modules.system.service.impl;


import com.wteam.exception.BadRequestException;
import com.wteam.modules.system.domain.Job;
import com.wteam.modules.system.domain.criteria.JobQueryCriteria;
import com.wteam.modules.system.domain.dto.JobDTO;
import com.wteam.modules.system.domain.mapper.JobMapper;
import com.wteam.modules.system.repository.DeptRepository;
import com.wteam.modules.system.repository.JobRepository;
import com.wteam.modules.system.repository.UserRepository;
import com.wteam.modules.system.service.JobService;
import com.wteam.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 岗位 业务层实现类
 * @author mission
 * @since 2019/07/13 10:47
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "job")
@Transactional( readOnly = true, rollbackFor = Exception.class)
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    private final JobMapper jobMapper;

    private final RedisUtils redisUtils;

    private final DeptRepository deptRepository;

    private final UserRepository userRepository;


    @Override
    @Cacheable(key = "'id:' + #p0")
    public JobDTO findDTOById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        ValidUtil.notNull(job, Job.ENTITY_NAME,"id",id);
        return jobMapper.toDto(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JobDTO create(Job resources) {
        Job job1 = jobRepository.findBySort(resources.getSort());
        if (job1!=null){
            throw new BadRequestException(Job.ENTITY_NAME+"排序数不能与其他者相同");
        }
        Job job = jobRepository.save(resources);
        return jobMapper.toDto(job);
    }

    @Override
    @CacheEvict(key = "'id:' + #p0.id")
    @Transactional(rollbackFor = Exception.class)
    public void update(Job resources) {
        Job job = jobRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull(job,Job.ENTITY_NAME,"id", resources.getId());

        Job job1 = jobRepository.findBySort(resources.getSort());
        if (job1!=null&&!job1.getId().equals(job.getId())){
            throw new BadRequestException(Job.ENTITY_NAME+"排序数不能与其他者相同");
        }

        jobRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {

        verification(ids);
        // 删除缓存
        redisUtils.delByKeys("job::id:", ids);

        for (Long id : ids) {
            jobRepository.logicDelete(id);
        }

    }

    private void verification(Set<Long> ids) {
        if(userRepository.countByJobs(ids) > 0){
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }
    @Override
    public Map<String,Object> queryAll(JobQueryCriteria criteria, Pageable pageable) {
        Page<Job> page = jobRepository.findAll((root, cq, cb) -> QueryHelper.andPredicate(root, criteria, cb), pageable);
        List<JobDTO> jobs=new ArrayList<>();
        for (Job job : page.getContent()) {
            jobs.add(jobMapper.toDto(job,deptRepository.findNameById(job.getDept().getParentId())));
        }
        return PageUtil.toPage(jobs,page.getTotalElements());
    }

    @Override
    public List<JobDTO> queryAll(JobQueryCriteria criteria) {
        return  jobMapper.toDto(jobRepository.findAll((root, cq, cb) -> QueryHelper.andPredicate(root, criteria, cb)));
    }

    @Override
    public void download(List<JobDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (JobDTO job : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("岗位名称", job.getName());
            map.put("所属部门", job.getDept().getName());
            map.put("排序", job.getSort());
            map.put("岗位状态", job.getEnabled()?"正常":"停用");
            map.put("创建日期", job.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
