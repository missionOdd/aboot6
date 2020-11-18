/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.service;


import com.wteam.domain.GenConfig;

/**
 * 代码生成器配置 业务层.
 * @author mission
 * @since 2019/07/16 8:39
 */
public interface GenConfigService {

    /**
     * 查询表配置
     * @return
     */
    GenConfig find(String tableName);

    /**
     * update
     * @param genConfig /
     * @return /
     */
    GenConfig update(GenConfig genConfig);
}
