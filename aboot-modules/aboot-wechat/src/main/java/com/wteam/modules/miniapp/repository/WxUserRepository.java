/*
 * copyleft © 2019-2021
 */

package com.wteam.modules.miniapp.repository;

import com.wteam.base.BaseRepository;
import com.wteam.modules.miniapp.domain.WxUser;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 图片 存储层
 * @author mission
 * @since 2019/07/12 15:55
 */
public interface WxUserRepository extends BaseRepository<WxUser,Long> {

    Optional<WxUser> findByOpenId(String openId);

    List<WxUser> findAllByUidIn(Set<Long> ids);
}
