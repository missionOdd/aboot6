/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
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
