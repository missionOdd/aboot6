package com.wteam.modules.common.service;

import com.wteam.modules.common.domain.WxConfig;
import com.wteam.modules.common.domain.criteria.WxConfigQueryCriteria;
import com.wteam.modules.common.domain.dto.WxConfigDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* 微信配置 业务层.
* @author aboot-wechat
* @since 2020-02-06
*/
public interface WxConfigService{

   /**
    * 查询数据分页
    * @param criteria
    * @param pageable
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(WxConfigQueryCriteria criteria, Pageable pageable);

   /**
    * 查询所有数据不分页
    * @param criteria
    * @return
    */
    List<WxConfigDTO> queryAll(WxConfigQueryCriteria criteria);

    /**
    * 根据ID查询
    * @param id ID
    * @return WxConfigDTO
    */
    WxConfigDTO findDTOById(Long id);

    /**
    * 创建
    * @param resources /
    * @return WxConfigDTO
    */
    WxConfigDTO create(WxConfig resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(WxConfig resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

   /**
   * 导出数据
   * @param queryAll 待导出的数据
   * @param response /
   * @throws IOException /
   */
   void download(List<WxConfigDTO> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 根据ID查询
     * @param appid appid
     * @return WxConfigDTO
     */
    WxConfigDTO getConfig(String appid);
}