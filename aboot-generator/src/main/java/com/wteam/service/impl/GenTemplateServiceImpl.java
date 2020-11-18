/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.service.impl;


import com.wteam.domain.GenTemplate;
import com.wteam.domain.criteria.GenTemplateQueryCriteria;
import com.wteam.domain.dto.GenTemplateDTO;
import com.wteam.domain.mapper.GenTemplateMapper;
import com.wteam.repository.GenTemplateRepository;
import com.wteam.service.GenTemplateService;
import com.wteam.utils.PageUtil;
import com.wteam.utils.QueryHelper;
import com.wteam.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
* 生成模板信息 业务实现层.
* @author mission
* @since 2019-09-29
*/
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GenTemplateServiceImpl implements GenTemplateService {

    private final GenTemplateRepository genTemplateRepository;

    private final GenTemplateMapper genTemplateMapper;


    @Override
    public Object queryAll(GenTemplateQueryCriteria criteria, Pageable pageable){
        Page<GenTemplate> page = genTemplateRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(genTemplateMapper::toDto));
    }

    @Override
    public Object queryAll(GenTemplateQueryCriteria criteria){
        return genTemplateMapper.toDto(genTemplateRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public GenTemplateDTO findDTOById(Long id) {
        GenTemplate genTemplate = genTemplateRepository.findById(id).orElse(null);
        ValidUtil.notNull(genTemplate,GenTemplate.ENTITY_NAME,"id",id);
        return genTemplateMapper.toDto(genTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenTemplateDTO create(GenTemplate resources) {
        return genTemplateMapper.toDto(genTemplateRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GenTemplate resources) {
        GenTemplate genTemplate = genTemplateRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull( genTemplate,GenTemplate.ENTITY_NAME,"id",resources.getId());
        assert genTemplate != null;
        genTemplate.copy(resources);
        genTemplateRepository.save(genTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            genTemplateRepository.deleteById(id);
        }
    }
}