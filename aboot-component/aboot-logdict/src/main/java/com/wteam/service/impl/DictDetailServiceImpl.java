/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.service.impl;


import com.wteam.domain.Dict;
import com.wteam.domain.DictDetail;
import com.wteam.domain.criteria.DictDetailQueryCriteria;
import com.wteam.domain.dto.DictDetailDTO;
import com.wteam.domain.mapper.DictDetailMapper;
import com.wteam.repository.DictDetailRepository;
import com.wteam.repository.DictRepository;
import com.wteam.service.DictDetailService;
import com.wteam.utils.PageUtil;
import com.wteam.utils.QueryHelper;
import com.wteam.utils.ValidUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


/**
 *  字典详情 业务实现层
 * @author mission
 * @since 2019/07/13 11:17
 */
@Service
@CacheConfig(cacheNames = "dictDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictDetailServiceImpl implements DictDetailService {

    private final DictRepository dictRepository;

    private final DictDetailRepository dictDetailRepository;

    private final DictDetailMapper dictDetailMapper;



    public DictDetailServiceImpl(DictRepository dictRepository, DictDetailRepository dictDetailRepository, DictDetailMapper dictDetailMapper) {
        this.dictRepository = dictRepository;
        this.dictDetailRepository = dictDetailRepository;
        this.dictDetailMapper = dictDetailMapper;
    }

    @Override
    @Cacheable(key = "'id:'+#p0")
    public DictDetailDTO findById(Long id) {
        DictDetail dictDetail = dictDetailRepository.findById(id).orElse(null);
        ValidUtil.notNull(dictDetail,DictDetail.ENTITY_NAME,"id",id);
        return dictDetailMapper.toDto(dictDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DictDetailDTO create(DictDetail resources) {
        Dict dict = dictRepository.getOne(resources.getDict().getId());
        resources.setDict(dict);
        return dictDetailMapper.toDto(dictDetailRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetail resources) {
        DictDetail dictDetail = dictDetailRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull( dictDetail,DictDetail.ENTITY_NAME,"id",resources.getId());
        Dict dict = dictRepository.findById(dictDetail.getDict().getId()).orElse(null);
        ValidUtil.notNull( dict, Dict.ENTITY_NAME,"id",resources.getId());
        resources.setDict(dict);
        dictDetailRepository.save(resources);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            dictDetailRepository.deleteById(id);
        }
    }

    @Override
    @Cacheable(keyGenerator = "keyGenerator")
    public Map queryAll(DictDetailQueryCriteria criteria, Pageable pageable) {
        Page<DictDetail> page = dictDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelper.andPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dictDetailMapper::toDto));
    }
}
