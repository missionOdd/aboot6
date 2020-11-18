package com.wteam.modules.system.repository;

import com.wteam.base.BaseRepository;
import com.wteam.modules.system.domain.Job;

/**
 * 岗位 存储层
 * @author mission
 * @since 2019/07/08 20:12
 */
public interface JobRepository extends BaseRepository<Job, Long> {

    Job findBySort(long sort);
}