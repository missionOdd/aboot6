package com.wteam.modules.system.domain.mapper;


import com.wteam.base.BaseMapper;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.dto.UserSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author mission
 * @since 2020/02/11 14:50
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserSmallMapper extends BaseMapper<UserSmallDTO, User> {
}
