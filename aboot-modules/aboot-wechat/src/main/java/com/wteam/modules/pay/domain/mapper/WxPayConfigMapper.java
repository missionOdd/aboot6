/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.pay.domain.mapper;


import com.wteam.base.BaseMapper;
import com.wteam.modules.pay.domain.WxPayConfig;
import com.wteam.modules.pay.domain.dto.WxPayConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* 微信支付配置 领域转换层.
* @author aboot-wechat
* @since 2020-02-07
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WxPayConfigMapper extends BaseMapper<WxPayConfigDTO, WxPayConfig> {

}