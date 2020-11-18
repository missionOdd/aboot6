/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.wteam.domain.Dict;
import com.wteam.domain.DictDetail;
import com.wteam.domain.criteria.DictQueryCriteria;
import com.wteam.domain.dto.DictDTO;
import com.wteam.domain.mapper.DictDetailMapper;
import com.wteam.domain.mapper.DictMapper;
import com.wteam.exception.EntityExistException;
import com.wteam.repository.DictDetailRepository;
import com.wteam.repository.DictRepository;
import com.wteam.service.DictService;
import com.wteam.utils.FileUtil;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 字典 业务实现层
 * @author mission
 * @since 2019/07/13 11:03
 */
@Service
@CacheConfig(cacheNames = "dict")
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl implements DictService {

    private final DictRepository dictRepository;

    private final DictDetailRepository dictDetailRepository;

    private final DictMapper dictMapper;

    private final DictDetailMapper dictDetailMapper;

    public DictServiceImpl(DictRepository dictRepository, DictDetailRepository dictDetailRepository, DictMapper dictMapper, DictDetailMapper dictDetailMapper) {
        this.dictRepository = dictRepository;
        this.dictDetailRepository = dictDetailRepository;
        this.dictMapper = dictMapper;
        this.dictDetailMapper = dictDetailMapper;
    }

    @Override
    @Cacheable
    public Object queryAll(DictDTO dict, Pageable pageable) {
        Page<Dict> page = dictRepository.findAll((root, cq, cb) -> QueryHelper.andPredicate(root, dict, cb), pageable);
        return PageUtil.toPage(page.map(dictMapper::toDto));
    }

    @Override
    @Cacheable
    public Map<String,Object> queryMap(DictQueryCriteria criteria) {
        List<Dict> list = dictRepository.findAll((root, cq, cb) -> QueryHelper.andPredicate(root, criteria, cb));
        Map<String,Object> map =new HashMap<>();
        for (Dict dict : list) {
            map.put(dict.getName(),dictDetailMapper.toDto(dict.getDictDetails()));
        }
        return map;
    }

    @Override
    @Cacheable
    public List<DictDTO> queryAll(DictQueryCriteria criteria) {
        return dictMapper.toDto(dictRepository.findAll((root, cq, cb) -> QueryHelper.andPredicate(root, criteria, cb)));
    }

    @Override
    @Cacheable(key = "#p0")
    public DictDTO findDTOById(Long id) {
        Dict dict = dictRepository.findById(id).orElse(null);
        ValidUtil.notNull(dict, Dict.ENTITY_NAME,"id",id);
        return dictMapper.toDto(dict);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DictDTO create(Dict resources) {
        if (dictRepository.findByName(resources.getName())!=null){
            throw  new EntityExistException(Dict.ENTITY_NAME,"name",resources.getName());
        }
        return dictMapper.toDto(dictRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Dict resources) {
        Dict dict = dictRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull( dict,Dict.ENTITY_NAME,"id",resources.getId());
        Dict dict1 = dictRepository.findByName(resources.getName());
        if (dict1!=null&&!dict1.getId().equals(dict.getId())){
            throw  new EntityExistException(Dict.ENTITY_NAME,"name",resources.getName());
        }
        resources.setId(dict.getId());
        dictRepository.save(resources);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            dictRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<DictDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DictDTO dictDTO : queryAll) {
            List<DictDetail> dictDetails = dictDetailRepository.findByDict_Name(dictDTO.getName());
            if(CollectionUtil.isNotEmpty(dictDetails)){
                for (DictDetail dictDetail : dictDetails) {
                    Map<String,Object> map = new LinkedHashMap<>();
                    map.put("字典名称", dictDTO.getName());
                    map.put("字典描述", dictDTO.getRemark());
                    map.put("字典标签", dictDetail.getLabel());
                    map.put("字典值", dictDetail.getValue());
                    map.put("创建日期", dictDetail.getCreatedAt());
                    list.add(map);
                }
            } else {
                Map<String,Object> map = new LinkedHashMap<>();
                map.put("字典名称", dictDTO.getName());
                map.put("字典描述", dictDTO.getRemark());
                map.put("字典标签", null);
                map.put("字典值", null);
                map.put("创建日期", dictDTO.getCreatedAt());
                list.add(map);
            }
        }
        FileUtil.downloadExcel(list, response);
    }
}
