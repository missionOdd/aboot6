/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 * without the prior written consent of Whale Cloud Inc.
 *
 */
package ${package}.domain.mapper;

import com.wteam.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ${package}.domain.dto.${className}DTO;
import ${package}.domain.${className};

/**
 * ${tableComment} 领域转换层.
 *
 * @author ${author}
 * @since ${date}
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${className}Mapper extends BaseMapper<${className}DTO, ${className}> {

}