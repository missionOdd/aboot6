/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wteam.base.BaseCons;
import com.wteam.domain.GenConfig;
import com.wteam.domain.GenTemplate;
import com.wteam.repository.GenConfigRepository;
import com.wteam.repository.GenTemplateRepository;
import com.wteam.service.GenConfigService;
import com.wteam.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.util.List;

/**
 * @author mission
 * @since 2019/07/16 8:52
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GenConfigServiceImpl implements GenConfigService {

    @PersistenceContext
    private EntityManager em;

    private final GenConfigRepository genConfigRepository;

    private final GenTemplateRepository genTemplateRepository;


    @Override
    public GenConfig find(String tableName) {
        GenConfig genConfig = genConfigRepository.findByTableName(tableName);
        if(genConfig == null){

            GenConfig genConfigLast = genConfigRepository.findFirstOrderBy(Sort.by(Sort.Order.desc(BaseCons.Field.updatedAt.name())));
            genConfig = new GenConfig(tableName,StringUtils.toCamelCase(tableName));
            // 使用预编译防止sql注入
            String sql = "select table_comment from information_schema.tables " +
                    "where table_schema = (select database()) and table_name = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1,tableName);
            Object tableComment = query.getSingleResult();
            if (ObjectUtil.isNotEmpty(tableComment)) {
                genConfig.setTableComment(tableComment.toString());
            }
            if (genConfigLast ==null) {
                return genConfig;
            }
            BeanUtil.copyProperties(genConfigLast,genConfig, "id","tableName","tableComment","apiAlias");

        }
        List<GenTemplate> genTemplateList = genTemplateRepository.findAll();
        genConfig.setGenTemplates(genTemplateList);
        return genConfig;
    }

    @Override
    @Transactional( rollbackFor = Exception.class)
    public GenConfig update(GenConfig genConfig) {
        // 如果 api 路径为空，则自动生成路径
        if(StringUtils.isBlank(genConfig.getApiPath())) {
            //自动设置APi路径,注释掉前需要同步取消前端的注释
            String separator = File.separator;
            String[] paths = null;
            if (separator.equals("\\")) {
                paths = genConfig.getPath().split("\\\\");
            } else {
                paths = genConfig.getPath().split(File.separator);
            }

            StringBuffer api = new StringBuffer();
            for (String path : paths) {
                api.append(path);
                api.append(separator);
                if ("src".equals(path)) {
                    api.append("api");
                    break;
                }
            }
            genConfig.setApiPath(api.toString());
        }
        GenConfig config = genConfigRepository.findByTableName(genConfig.getTableName());
        if (config!=null) {
            genConfig.setId(config.getId());
        }
        if (genConfig.getGenTemplates()!=null) {
            genTemplateRepository.saveAll(genConfig.getGenTemplates());
        }
        return genConfigRepository.save(genConfig);

    }
}
