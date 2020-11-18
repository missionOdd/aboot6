package com.wteam.service;


import com.wteam.domain.ShowConfig;
import com.wteam.domain.criteria.ShowConfigQueryCriteria;
import com.wteam.domain.dto.ShowConfigDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
* 前端配置 业务层.
* @author mission
* @since 2019-10-15
*/
public interface ShowConfigService{

   /**
    * queryAll 分页
    * @param criteria /
    * @param pageable /
    * @return /
    */
    Object queryAll(ShowConfigQueryCriteria criteria, Pageable pageable);

   /**
    * queryAll 不分页
    * @param criteria
    * @return
    */
    Object queryAll(ShowConfigQueryCriteria criteria);

   /**
    * findById
    * @param id
    * @return
    */

    ShowConfigDTO findDTOById(Long id);


  /**
   * findById
   * @param name
   * @return
   */
  ShowConfigDTO findByName(String name);
   /**
    * create
    * @param resources
    * @return
    */

    ShowConfigDTO create(ShowConfig resources);

   /**
    * update
    * @param resources
    */
    void update(ShowConfig resources);

   /**
    * delete
    * @param ids
    */
    void delete(Long[] ids);

  /**
   * upload
   * @param file
   */
   Object upload(MultipartFile file);

    /**
     * upload
     * @param file
     */
    Object upload(MultipartFile file, String filename, String configname);
}