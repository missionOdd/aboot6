/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.service.impl;


import com.alibaba.fastjson.JSON;
import com.wteam.domain.GroupData;
import com.wteam.domain.criteria.GroupDataQueryCriteria;
import com.wteam.domain.dto.GroupDataDTO;
import com.wteam.domain.mapper.GroupDataMapper;
import com.wteam.repository.GroupDataRepository;
import com.wteam.service.GroupDataService;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* 数据组 业务实现层.
* @author mission
* @since 2020-03-23
*/
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "groupData")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class GroupDataServiceImpl implements GroupDataService {

    private final GroupDataRepository groupDataRepository;

    private final GroupDataMapper groupDataMapper;


    @Override
    @Cacheable
    public Map<String,Object> queryAll(GroupDataQueryCriteria criteria, Pageable pageable){
        Page<GroupData> page = groupDataRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder),pageable);
        Page<GroupDataDTO> map = page.map(groupDataMapper::toDto);
        map.forEach(dto->dto.setMap(JSON.parse(dto.getValue())));
        return PageUtil.toPage(map);
    }

    @Override
    @Cacheable
    public List<GroupDataDTO> queryAll(GroupDataQueryCriteria criteria){
        return groupDataMapper.toDto(groupDataRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public GroupDataDTO findDTOById(Long id) {
        GroupData groupData = groupDataRepository.findById(id).orElse(null);
        ValidUtil.notNull(groupData,GroupData.ENTITY_NAME,"id",id);
        GroupDataDTO groupDataDTO = groupDataMapper.toDto(groupData);
        groupDataDTO.setMap(JSON.parse(groupDataDTO.getValue()));
        return groupDataDTO;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public GroupDataDTO create(GroupData resources) {
        return groupDataMapper.toDto(groupDataRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(GroupData resources) {
        GroupData groupData = null;
        if (resources.getId()!=null) {
           groupData = groupDataRepository.findById(resources.getId()).orElse(null);
        }else {
            groupData = groupDataRepository.findFirstByGroupName(resources.getGroupName());
        }
        ValidUtil.notNull( groupData,GroupData.ENTITY_NAME,"id",resources.getId());

        assert groupData != null;
        groupData.copy(resources);
        groupDataRepository.save(groupData);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            groupDataRepository.logicDelete(id);
        }
    }

    @Override
    public void download(List<GroupDataDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (GroupDataDTO groupData : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("对应的数据名称", groupData.getGroupName());
            map.put("数据组对应的数据值（json数据）", groupData.getValue());
            map.put("排序字段", groupData.getSort());
            map.put("状态（1：开启；2：关闭；）", groupData.getEnabled());
            map.put("创建时间", groupData.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Cacheable
    public GroupDataDTO findDTOByName(String name) {
        GroupData groupData = groupDataRepository.findFirstByGroupName(name);
        ValidUtil.notNull(groupData,GroupData.ENTITY_NAME,"name",name);
        GroupDataDTO groupDataDTO = groupDataMapper.toDto(groupData);
        groupDataDTO.setMap(JSON.parse(groupDataDTO.getValue()));
        return groupDataDTO;
    }


}