/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.pay.repository;

import com.wteam.base.BaseRepository;
import com.wteam.modules.pay.domain.PayLog;

/**
* 交易日志表 存储层.
* @author mission
* @since 2020-03-18
*/
public interface PayLogRepository extends BaseRepository<PayLog, Long> {
}