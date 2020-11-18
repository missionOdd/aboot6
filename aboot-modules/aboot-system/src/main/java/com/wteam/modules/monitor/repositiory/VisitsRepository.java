/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.modules.monitor.repositiory;

import com.wteam.base.BaseRepository;
import com.wteam.modules.monitor.domain.Visits;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mission
 * @since 2019/07/11 21:17
 */
@Repository
public interface VisitsRepository extends BaseRepository<Visits,Long> {

    /**
     * findByDate
     * @param date
     * @return
     */
    Visits findByDate(String date);

    /**
     * 获得一个时间段的记录
     * @param date1
     * @param date2
     * @return
     */
    @Query(value = "select * FROM sys_visits where " +
        "created_at between ?1 and ?2",nativeQuery = true)
    List<Visits> findAllVisits(String date1, String date2);

}
