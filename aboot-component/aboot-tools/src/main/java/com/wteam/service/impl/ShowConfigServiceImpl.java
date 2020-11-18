/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.service.impl;


import com.wteam.utils.PageUtil;
import com.wteam.utils.QueryHelper;
import com.wteam.utils.ValidUtil;
import com.wteam.domain.ShowConfig;
import com.wteam.domain.criteria.ShowConfigQueryCriteria;
import com.wteam.domain.dto.LocalStorageDTO;
import com.wteam.domain.dto.ShowConfigDTO;
import com.wteam.domain.mapper.ShowConfigMapper;
import com.wteam.repository.ShowConfigRepository;
import com.wteam.service.LocalStorageService;
import com.wteam.service.ShowConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
* 前端配置 业务实现层.
* @author mission
* @since 2019-10-15
*/
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "showConfig")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ShowConfigServiceImpl implements ShowConfigService {

    private final ShowConfigRepository showConfigRepository;

    private final ShowConfigMapper showConfigMapper;

    private final LocalStorageService localStorageService;


    @Override
    @Cacheable
    public Object queryAll(ShowConfigQueryCriteria criteria, Pageable pageable){
    Page<ShowConfig> page = showConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder),pageable);
    return PageUtil.toPage(page.map(showConfigMapper::toDto));
    }

    @Override
    @Cacheable
    public Object queryAll(ShowConfigQueryCriteria criteria){
    return showConfigMapper.toDto(showConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ShowConfigDTO findDTOById(Long id) {
    ShowConfig showConfig = showConfigRepository.findById(id).orElse(null);
    ValidUtil.notNull(showConfig,ShowConfig.ENTITY_NAME,"id",id);
    return showConfigMapper.toDto(showConfig);
    }

    @Override
    @Cacheable
    public ShowConfigDTO findByName(String name) {
        return showConfigMapper.toDto(showConfigRepository.findFirstByName(name).orElseGet(ShowConfig::new));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ShowConfigDTO create(ShowConfig resources) {
    return showConfigMapper.toDto(showConfigRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ShowConfig resources) {
        ShowConfig showConfig = showConfigRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull( showConfig,ShowConfig.ENTITY_NAME,"id",resources.getId());
        assert showConfig != null;
        showConfig.copy(resources);
        showConfigRepository.save(showConfig);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            showConfigRepository.deleteById(id);
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Object upload(MultipartFile file) {
        ShowConfig showConfig = showConfigRepository.findFirstByName("logo").orElse(new ShowConfig());
        LocalStorageDTO storage = localStorageService.uploadImage(file.getName(), file);
        showConfig.setName("logo");
        showConfig.setValue(storage.getUrl());
        showConfig.setEnabled(true);
        return  showConfigMapper.toDto(showConfigRepository.save(showConfig));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Object upload(MultipartFile file, String filename, String configname) {
        ShowConfig showConfig = showConfigRepository.findFirstByName(configname).orElse(new ShowConfig());
        filename=filename==null?file.getName():filename;
        LocalStorageDTO storage = localStorageService.create(filename, file);
        showConfig.setName(configname);
        showConfig.setValue(storage.getUrl());
        showConfig.setEnabled(true);
        return  showConfigMapper.toDto(showConfigRepository.save(showConfig));
    }
}