package com.wteam.modules.pay.service.impl;


import com.wteam.exception.BadRequestException;
import com.wteam.exception.EntityExistException;
import com.wteam.modules.pay.domain.WxPayConfig;
import com.wteam.modules.pay.domain.criteria.WxPayConfigQueryCriteria;
import com.wteam.modules.pay.domain.dto.WxPayConfigDTO;
import com.wteam.modules.pay.domain.mapper.WxPayConfigMapper;
import com.wteam.modules.pay.repository.WxPayConfigRepository;
import com.wteam.modules.pay.service.WxPayConfigService;
import com.wteam.utils.FileUtil;
import com.wteam.utils.PageUtil;
import com.wteam.utils.QueryHelper;
import com.wteam.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.util.Asserts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.wteam.utils.PathUtil.*;

/**
* 微信支付配置 业务实现层.
* @author aboot-wechat
* @since 2020-02-07
*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class WxPayConfigServiceImpl implements WxPayConfigService {

    private final WxPayConfigRepository wxPayConfigRepository;

    private final WxPayConfigMapper wxPayConfigMapper;

    @Override
    public Map<String,Object> queryAll(WxPayConfigQueryCriteria criteria, Pageable pageable){
        Page<WxPayConfig> page = wxPayConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(wxPayConfigMapper::toDto));
    }

    @Override
    public List<WxPayConfigDTO> queryAll(WxPayConfigQueryCriteria criteria){
        return wxPayConfigMapper.toDto(wxPayConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public WxPayConfigDTO findDTOById(Long id) {
        WxPayConfig wxPayConfig = wxPayConfigRepository.findById(id).orElse(null);
        ValidUtil.notNull(wxPayConfig, WxPayConfig.ENTITY_NAME,"id",id);
        return wxPayConfigMapper.toDto(wxPayConfig);
    }

    @Override
    public WxPayConfigDTO findDTOByAppid(String appid) {
        WxPayConfig wxPayConfig = wxPayConfigRepository.findByAppid(appid);
        ValidUtil.notNull(wxPayConfig,WxPayConfig.ENTITY_NAME,"appid",appid);
        return wxPayConfigMapper.toDto(wxPayConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxPayConfigDTO create(WxPayConfig resources) {

        if (wxPayConfigRepository.findByAppid(resources.getAppid())!=null) {
            throw new EntityExistException("微信支付AppId");
        }
        if (StringUtils.hasText(resources.getMchPath())) {
            String realPath = basePath() + toFilepath(resources.getMchPath().replaceFirst(fileUrlPrefix(), ""));
            resources.setMchPath(realPath);
        }
    return wxPayConfigMapper.toDto(wxPayConfigRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WxPayConfig resources) {
        WxPayConfig wxPayConfig = wxPayConfigRepository.findById(resources.getId()).orElse(null);
        ValidUtil.notNull( wxPayConfig,WxPayConfig.ENTITY_NAME,"id",resources.getId());
        WxPayConfig wxPayConfig1 = wxPayConfigRepository.findByAppid(resources.getAppid());
        if (wxPayConfig1!=null&&!wxPayConfig1.getId().equals(resources.getId())) {
            throw new EntityExistException("微信支付AppId");
        }
        if (StringUtils.hasText(resources.getMchPath())&& !resources.getMchPath().equals(wxPayConfig.getMchPath())) {
            String realPath = basePath() + toFilepath(resources.getMchPath().replaceFirst(fileUrlPrefix(), ""));
            resources.setMchPath(realPath);
        }
        wxPayConfig.copy(resources);
        wxPayConfigRepository.save(wxPayConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            if (id==1L){
                throw new BadRequestException("系统不允许删除");
            }
            wxPayConfigRepository.deleteById(id);
        }

    }

    @Override
    public void download(List<WxPayConfigDTO> queryAll, HttpServletResponse response) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        List<Map<String, Object>> list = new ArrayList<>();
        for (WxPayConfigDTO wxPayConfig : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("设置微信公众号或者小程序等的appid", wxPayConfig.getAppid());
            map.put("微信支付商户号", wxPayConfig.getMchId());
            map.put("微信支付商户密钥", wxPayConfig.getMchKey());
            map.put("apiclient_cert.p12文件的绝对路径", wxPayConfig.getMchPath());
            map.put("注备", wxPayConfig.getRemark());
            map.put("创建时间", wxPayConfig.getCreatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void downloadCredentials(String mchPath, HttpServletRequest request, HttpServletResponse response) {
        Asserts.notBlank(mchPath,"无上传证书");
        File file = new File(mchPath);
        if (!file.exists()) {
           throw new BadRequestException("证书已经被删除,请重新上传");
        }
        FileUtil.downloadFile(request,response,file,false);

    }
}