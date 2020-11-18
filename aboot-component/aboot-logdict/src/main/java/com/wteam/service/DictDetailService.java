/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.service;


import com.wteam.domain.DictDetail;
import com.wteam.domain.criteria.DictDetailQueryCriteria;
import com.wteam.domain.dto.DictDetailDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 字典详情 业务层
 * @author mission
 * @since 2019/07/13 11:16
 */
public interface DictDetailService {


    /**
     * findById
     * @param id /
     * @return /
     */
    DictDetailDTO findById(Long id);

    /**
     * create
     * @param resources /
     * @return /
     */
    DictDetailDTO create(DictDetail resources);

    /**
     * update
     * @param resources  /
     */
    void update(DictDetail resources);

    /**
     * delete
     * @param ids /
     */
    void delete(Long[] ids);

    Map queryAll(DictDetailQueryCriteria criteria, Pageable pageable);
}
