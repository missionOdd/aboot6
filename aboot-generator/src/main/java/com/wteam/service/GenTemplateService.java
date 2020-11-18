/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.service;

import com.wteam.domain.dto.GenTemplateDTO;
import com.wteam.domain.GenTemplate;
import com.wteam.domain.criteria.GenTemplateQueryCriteria;
import org.springframework.data.domain.Pageable;

/**
* 生成模板信息 业务层.
* @author mission
* @since 2019-09-29
*/

public interface GenTemplateService{

   /**
    * queryAll 分页
    * @param criteria
    * @param pageable
    * @return
    */
    Object queryAll(GenTemplateQueryCriteria criteria, Pageable pageable);

   /**
    * queryAll 不分页
    * @param criteria
    * @return
    */
    Object queryAll(GenTemplateQueryCriteria criteria);

   /**
    * findById
    * @param id
    * @return
    */
    GenTemplateDTO findDTOById(Long id);

   /**
    * create
    * @param resources
    * @return
    */
    GenTemplateDTO create(GenTemplate resources);

   /**
    * update
    * @param resources
    */
    void update(GenTemplate resources);

   /**
    * delete
    * @param ids
    */
    void delete(Long[] ids);
}