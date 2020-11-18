/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.domain.mapper;

import com.wteam.base.BaseMapper;
import com.wteam.domain.GroupData;
import com.wteam.domain.dto.GroupDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* 数据组 领域转换层.
* @author mission
* @since 2020-03-23
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupDataMapper extends BaseMapper<GroupDataDTO, GroupData> {

}