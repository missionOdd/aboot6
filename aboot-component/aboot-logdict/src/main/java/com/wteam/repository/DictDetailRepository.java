/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.repository;


import com.wteam.base.BaseRepository;
import com.wteam.domain.DictDetail;

import java.util.List;

/**
 * 字典详情 存储层
 * @author mission
 * @since 2019/07/08 20:12
 */
public interface DictDetailRepository extends BaseRepository<DictDetail, Long> {

    List<DictDetail> findByDict_Name(String name);
}