/*
 * copyleft © 2019-2021
 */

package com.wteam.modules.pay.domain.mapper;

import com.wteam.base.BaseMapper;
import com.wteam.modules.pay.domain.PayLog;
import com.wteam.modules.pay.domain.dto.PayLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* 交易日志表 领域转换层.
* @author mission
* @since 2020-03-18
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayLogMapper extends BaseMapper<PayLogDTO, PayLog> {

}