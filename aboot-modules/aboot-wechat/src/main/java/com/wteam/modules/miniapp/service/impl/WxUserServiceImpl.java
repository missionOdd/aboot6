/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.miniapp.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import com.google.common.collect.Sets;
import com.wteam.exception.BadRequestException;
import com.wteam.exception.EntityExistException;
import com.wteam.modules.miniapp.config.WxMaConfiguration;
import com.wteam.modules.miniapp.domain.WxUser;
import com.wteam.modules.miniapp.domain.criteria.WxUserQueryCriteria;
import com.wteam.modules.miniapp.domain.dto.WxLoginDTO;
import com.wteam.modules.miniapp.domain.dto.WxUserDTO;
import com.wteam.modules.miniapp.domain.mapper.WxUserMapper;
import com.wteam.modules.miniapp.repository.WxUserRepository;
import com.wteam.modules.miniapp.service.WxUserService;
import com.wteam.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wteam.utils.PathUtil.basePath;
import static com.wteam.utils.PathUtil.fileUrlPrefix;

/**
 * 微信用户
 * @author mission
 * @since 2020/02/04 3:58
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "wxUser")
@RequiredArgsConstructor
@Transactional( readOnly = true, rollbackFor = Exception.class)
public class WxUserServiceImpl implements WxUserService {

    private final WxUserRepository wxUserRepository;

    private final WxUserMapper wxUserMapper;

    private final RedisUtils redisUtils;

    @Override
    public Map<String,Object> queryAll(WxUserQueryCriteria criteria, Pageable pageable){
        Page<WxUser> page = wxUserRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(wxUserMapper::toDto));
    }

    @Override
    public List<WxUserDTO> queryAll(WxUserQueryCriteria criteria){
        return wxUserMapper.toDto(wxUserRepository.findAll((root, criteriaQuery, criteriaBuilder) ->  QueryHelper.andPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "'id:'+#p0")
    public WxUserDTO findDTOById(Long uid) {
        WxUser wxUser = wxUserRepository.findById(uid).orElse(null);
        ValidUtil.notNull(wxUser,WxUser.ENTITY_NAME,"uid",uid);
        return wxUserMapper.toDto(wxUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxUserDTO create(WxUser resources) {
        if (wxUserRepository.findByOpenId(resources.getOpenId()).isPresent()) {
            throw new EntityExistException(WxUser.ENTITY_NAME);
        }
        return wxUserMapper.toDto(wxUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WxUser resources) {
        if (!resources.getUid().equals(findByOpenId(resources.getOpenId()).getUid())) {
            throw new BadRequestException(WxUser.ENTITY_NAME+"已存在");
        }
        WxUser wxUser = wxUserRepository.findById(resources.getUid()).orElse(null);
        ValidUtil.notNull( wxUser,WxUser.ENTITY_NAME,"id",resources.getUid());

        redisUtils.del("wxUser::id:"+resources.getUid());
        redisUtils.del("wxUser::openId:",wxUser.getOpenId());
        wxUser.copy(resources);
        wxUserRepository.save(wxUser);
    }

    @Override
    @CacheEvict(allEntries=true)
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        redisUtils.delByKeys("wxUser::id:", Sets.newHashSet(ids));
        List<WxUser> wxUsers = wxUserRepository.findAllByUidIn(Sets.newHashSet(ids));
        redisUtils.del("wxUser::openId:",wxUsers.stream().map(WxUser::getOpenId).collect(Collectors.joining()));
        for (Long id : ids) {
             wxUserRepository.findById(id);
            wxUserRepository.logicDelete(id);
        }
    }

    @Override
    public void download(List<WxUserDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WxUserDTO wxUser : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("平台ID", wxUser.getOpenId());
            map.put("昵称", wxUser.getNickname());
            map.put("头像", wxUser.getAvatar());
            map.put("性别", wxUser.getGender());
            map.put("省份", wxUser.getProvince());
            map.put("城市", wxUser.getCity());
            map.put("电话", wxUser.getPhone());
            map.put("区号", wxUser.getCountryCode());
            map.put("语言", wxUser.getLanguage());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public WxUser load(WxLoginDTO wxLoginDTO) {
        WxUser wxUser = null;
        try {
            WxMaService maService = WxMaConfiguration
                    .getMaService(wxLoginDTO.getAppid());
            WxMaUserService wxMaUserService =maService.getUserService();
            WxMaJscode2SessionResult sessionInfo = maService.jsCode2SessionInfo(wxLoginDTO.getCode());
            wxUser = findByOpenId(sessionInfo.getOpenid());
            String sessionKey = sessionInfo.getSessionKey();
            log.info(sessionKey);
            wxUser.setAppId(wxLoginDTO.getAppid());
            wxUser.setNickName("用户"+ RandomUtil.randomNumbers(6));

            WxLoginDTO.RawData userInfoRaw = wxLoginDTO.getUserInfo();
            WxLoginDTO.RawData phoneInfoRaw = wxLoginDTO.getPhoneInfo();

            WxMaUserInfo userInfo =wxLoginDTO.getWxUser();
            if (userInfo == null&&userInfoRaw!=null) {
                userInfo = wxMaUserService.getUserInfo(sessionKey, userInfoRaw.getEncryptedData(), userInfoRaw.getIv());
            }
            if (userInfo != null) {
                //表情过滤
                userInfo.setNickName(StringUtils.filterEmoji(userInfo.getNickName()));
                //头像保存到本地
                URL url = new URL(userInfo.getAvatarUrl());
                InputStream stream = URLUtil.getStream(url);
                String fileName = IdUtil.simpleUUID() + ".jpg";
                String dest = basePath() + "avatar" + File.separator + fileName;
                FileUtil.writeFromStream(stream,dest);
                userInfo.setAvatarUrl(StringUtils.join( fileUrlPrefix(),"avatar", "/",fileName));
                wxUser.copy(userInfo);
            }

            wxUser.setOpenId(sessionInfo.getOpenid());
            if (phoneInfoRaw != null) {
                WxMaPhoneNumberInfo phoneInfo = wxMaUserService.getPhoneNoInfo(sessionKey, phoneInfoRaw.getEncryptedData(), phoneInfoRaw.getIv());
                wxUser.copy(phoneInfo);
            }
        } catch (WxErrorException | MalformedURLException e) {
            e.printStackTrace();
            throw new BadRequestException("登录失败");
        }

        return wxUser;
    }

    @Override
    public WxUser checkWxReg(WxLoginDTO wxLoginDTO) {
        WxMaUserService wxMaUserService = WxMaConfiguration
                .getMaService(wxLoginDTO.getAppid()).getUserService();
        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxMaUserService.getSessionInfo(wxLoginDTO.getCode());
        } catch (WxErrorException e) {
            throw new BadRequestException(e.getError().getErrorMsg());
        }

        return findByOpenId(sessionInfo.getOpenid());
    }

    @Override
    @Cacheable(key = "'openId:'+#p0")
    public WxUser findByOpenId(String openid) {
        return wxUserRepository.findByOpenId(openid).orElseGet(WxUser::new);
    }
}