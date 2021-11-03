/*
 * copyleft © 2019-2021
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