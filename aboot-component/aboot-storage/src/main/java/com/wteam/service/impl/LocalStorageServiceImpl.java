/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.wteam.domain.LocalStorage;
import com.wteam.domain.criteria.LocalStorageQueryCriteria;
import com.wteam.domain.dto.LocalStorageDTO;
import com.wteam.domain.mapper.LocalStorageMapper;
import com.wteam.exception.BadRequestException;
import com.wteam.repository.LocalStorageRepository;
import com.wteam.service.LocalStorageService;
import com.wteam.utils.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.wteam.utils.PathUtil.basePath;
import static com.wteam.utils.PathUtil.fileUrlPrefix;

/**
* 存储 业务实现层.
* @author mission
* @since 2019-11-03
*/
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LocalStorageServiceImpl implements LocalStorageService {

    private final LocalStorageRepository localStorageRepository;

    private final LocalStorageMapper localStorageMapper;


    @Value("${file.maxSize}")
    private final long maxSize=200L;


    @Override
    public Object queryAll(LocalStorageQueryCriteria criteria, Pageable pageable){
        Page<LocalStorage> page = localStorageRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(localStorageMapper::toDto));
    }

    @Override
    public List<LocalStorageDTO> queryAll(LocalStorageQueryCriteria criteria){
        return localStorageMapper.toDto(localStorageRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public LocalStorageDTO findDTOById(Long id) {
        LocalStorage localStorage = localStorageRepository.findById(id).orElse(null);
        ValidUtil.notNull(localStorage,LocalStorage.ENTITY_NAME,"id",id);
        return localStorageMapper.toDto(localStorage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocalStorageDTO create(String name, MultipartFile multipartFile) {
        //校验上传大小
        FileUtil.checkSize(maxSize, multipartFile.getSize());
        //校验重复文件
        String md5 = FileUtil.getMd5(multipartFile);
        LocalStorage storage = localStorageRepository.findFirstByMd5(md5);
        if (storage!=null) {
            return localStorageMapper.toDto(storage);
        }

        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        //上传文件到本地
        File file = FileUtil.upload(multipartFile, basePath() + type +  File.separator);
        if(ObjectUtil.isNull(file)){
            throw new BadRequestException("上传失败");
        }

        String username = null;
        try{
            username = SecurityUtils.getUsername();
        }catch (Exception e){
            username = "游客";
        }
        try {
            //文件名不超过35个字
            name = StringUtils.isBlank(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            if (name.length()>35) {
                name=name.substring(0,35);
            }

            //保存文件实体
            LocalStorage localStorage = new LocalStorage(
                    name,
                    username,
                    file.getName(),
                    file.getPath(),
                    FileUtil.getSize(multipartFile.getSize()),
                    suffix,
                    type,
                    md5,
                    StringUtils.join(fileUrlPrefix(), type, "/", file.getName())
            );
            return localStorageMapper.toDto(localStorageRepository.save(localStorage));
        }catch (Exception e){
            FileUtil.del(file);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocalStorageDTO uploadImage(String name, MultipartFile multipartFile) {
        if(!"image".contains(FileUtil.getFileTypeByMimeType(multipartFile.getContentType()))){
            throw new BadRequestException("上传图片格式不正确");
        }
        return create(name,multipartFile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LocalStorage resources) {
        LocalStorage localStorage = localStorageRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull( localStorage,LocalStorage.ENTITY_NAME,"id",resources.getId());

        localStorage.copy(resources);
        localStorageRepository.save(localStorage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        LocalStorage storage = localStorageRepository.findById(id).orElseGet(LocalStorage::new);
        if (storage.getPath()!=null) {
            FileUtil.del(storage.getPath());
        }
        localStorageRepository.logicDelete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            LocalStorage storage = localStorageRepository.findById(id).orElseGet(LocalStorage::new);
            FileUtil.del(storage.getPath());
            localStorageRepository.logicDelete(id);
        }
    }

    @Override
    public void download(List<LocalStorageDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LocalStorageDTO localStorageDTO : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("文件名", localStorageDTO.getRealName());
            map.put("备注名", localStorageDTO.getName());
            map.put("文件类型", localStorageDTO.getType());
            map.put("文件大小", localStorageDTO.getSize());
            map.put("操作人", localStorageDTO.getOperate());
            map.put("创建日期", localStorageDTO.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}