package com.wteam.modules.common.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.wteam.exception.BadRequestException;
import com.wteam.exception.EntityExistException;
import com.wteam.modules.common.domain.WxConfig;
import com.wteam.modules.common.domain.criteria.WxConfigQueryCriteria;
import com.wteam.modules.common.domain.dto.WxConfigDTO;
import com.wteam.modules.common.domain.mapper.WxConfigMapper;
import com.wteam.modules.common.repository.WxConfigRepository;
import com.wteam.modules.common.service.WxConfigService;
import com.wteam.modules.miniapp.config.WxMaConfiguration;
import com.wteam.utils.FileUtil;
import com.wteam.utils.PageUtil;
import com.wteam.utils.QueryHelper;
import com.wteam.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* 微信配置 业务实现层.
* @author aboot-wechat
* @since 2020-02-06
*/
@Service
@RequiredArgsConstructor
public class WxConfigServiceImpl implements WxConfigService {

    private final WxConfigRepository wxConfigRepository;

    private final WxConfigMapper wxConfigMapper;


    @Override
    public Map<String,Object> queryAll(WxConfigQueryCriteria criteria, Pageable pageable){
        Page<WxConfig> page = wxConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(wxConfigMapper::toDto));
    }

    @Override
    public List<WxConfigDTO> queryAll(WxConfigQueryCriteria criteria){
        return wxConfigMapper.toDto(wxConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public WxConfigDTO findDTOById(Long id) {
        WxConfig wxConfig = wxConfigRepository.findById(id).orElse(null);
        ValidUtil.notNull(wxConfig,WxConfig.ENTITY_NAME,"id",id);
        return wxConfigMapper.toDto(wxConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxConfigDTO create(WxConfig resources) {
        if (wxConfigRepository.findByAppid(resources.getAppid())!=null) {
            throw new EntityExistException("微信应用AppId");
        }
     WxMaConfiguration.cleanCache(resources.getAppid());
    return wxConfigMapper.toDto(wxConfigRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WxConfig resources) {
        WxConfig wxConfig = wxConfigRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull( wxConfig,WxConfig.ENTITY_NAME,"id",resources.getId());
        WxConfig wxConfig1 = wxConfigRepository.findByAppid(resources.getAppid());
        if (wxConfig1!=null&&!wxConfig1.getId().equals(resources.getId())) {
            throw new EntityExistException("微信应用AppId");
        }
        assert wxConfig != null;
        WxMaConfiguration.cleanCache(wxConfig.getAppid());
        WxMaConfiguration.cleanCache(resources.getAppid());
        wxConfig.copy(resources);
        wxConfigRepository.save(wxConfig);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            if (id==1){
                throw new BadRequestException("系统不允许删除");
            }
            wxConfigRepository.logicDelete(id);
        }
        WxMaConfiguration.cleanAllCache();
    }

    @Override
    public void download(List<WxConfigDTO> queryAll, HttpServletResponse response) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        List<Map<String, Object>> list = new ArrayList<>();
        for (WxConfigDTO wxConfig : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("配置类型 公众号0 小程序1", wxConfig.getType());
            map.put("应用ID", wxConfig.getAppid());
            map.put("应用密钥", wxConfig.getSecret());
            map.put("应用Token", wxConfig.getToken());
            map.put("解密密钥", wxConfig.getAesKey());
            map.put("返回格式", wxConfig.getMsgDataFormat());
            map.put("创建时间", wxConfig.getCreateTime().format(formatter));
            map.put("修改时间", wxConfig.getEditTime().format(formatter));
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public WxConfigDTO getConfig(String appid) {
        WxConfig config = wxConfigRepository.findByAppid(appid);
        if (ObjectUtil.isNull(config)) {
            throw new BadRequestException(String.format("未找到对应appid[%s]的配置，请核实！", appid));
        }
        return wxConfigMapper.toDto(config);
    }
}