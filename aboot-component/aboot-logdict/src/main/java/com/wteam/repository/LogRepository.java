/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.repository;

import com.wteam.base.BaseRepository;
import com.wteam.domain.Log;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * @author mission
 * @since 2019/07/08 9:41
 */
@Repository
public interface LogRepository extends BaseRepository<Log,Long> {

    /**
     * 获取一个时间段的IP记录
     * @param date1
     * @param date2
     * @return
     */
    @Query(value = "select count(*) FROM (select request_ip FROM sys_log where created_at between ?1 and ?2 GROUP BY request_ip) as s",nativeQuery = true)
    Long findIp(Timestamp date1, Timestamp date2);

    /**
     * findExceptionById
     * @param id
     * @return
     */
    @Query(value = "select exception_detail FROM sys_log where id = ?1",nativeQuery = true)
    String findExceptionById(Long id);


    /**
     * 根据日志类型删除信息
     * @param logType 日志类型
     */
    @Modifying
    @Query(value = "delete from sys_log where log_type = ?1",nativeQuery = true)
    void deleteByLogType(String logType);
}
