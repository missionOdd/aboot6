/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.domain.mapper;


import com.wteam.domain.GenTemplate;
import com.wteam.domain.dto.GenTemplateDTO;
import com.wteam.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* 生成模板信息 领域转换层.
* @author mission
* @since 2019-09-29
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenTemplateMapper extends BaseMapper<GenTemplateDTO, GenTemplate> {

}