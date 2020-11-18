/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.service;


import com.wteam.domain.LocalStorage;
import com.wteam.domain.criteria.LocalStorageQueryCriteria;
import com.wteam.domain.dto.LocalStorageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* 存储 业务层.
* @author mission
* @since 2019-11-03
*/
public interface LocalStorageService{

   /**
    * queryAll 分页
    * @param criteria /
    * @param pageable /
    * @return /
    */
    Object queryAll(LocalStorageQueryCriteria criteria, Pageable pageable);

   /**
    * queryAll 不分页
    * @param criteria /
    * @return /
    */
    List<LocalStorageDTO> queryAll(LocalStorageQueryCriteria criteria);

   /**
    * findById
    * @param id /
    * @return
    */
    LocalStorageDTO findDTOById(Long id);


    /**
     * create
     * @param name /
     * @param multipartFile /
     * @return
     */
    LocalStorageDTO create(String name, MultipartFile multipartFile);

    /**
     * createImage
     * @param name /
     * @param multipartFile /
     * @return
     */
    LocalStorageDTO uploadImage(String name, MultipartFile multipartFile);
   /**
    * update
    * @param resources /
    */
    void update(LocalStorage resources);

   /**
    * delete
    * @param id /
    */
    void delete(Long id);


    /**
     * deleteAll
     * @param ids /
     */
    void deleteAll(Long[] ids);

    /**
     * 导出
     * @param queryAll /
     * @param response /
     * @throws IOException /
     */
    void download(List<LocalStorageDTO> queryAll, HttpServletResponse response) throws IOException;


}