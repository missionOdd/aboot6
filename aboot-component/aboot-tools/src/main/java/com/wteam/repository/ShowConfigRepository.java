/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.repository;


import com.wteam.base.BaseRepository;
import com.wteam.domain.ShowConfig;
import java.util.Optional;

/**
* 前端配置 存储层.
* @author mission
* @since 2019-10-15
*/
public interface ShowConfigRepository extends BaseRepository<ShowConfig, Long> {

  Optional<ShowConfig> findFirstByName(String name);
}