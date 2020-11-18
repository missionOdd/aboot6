package com.wteam.modules.system.domain.mapper;


import com.wteam.base.BaseMapper;
import com.wteam.modules.system.domain.Role;
import com.wteam.modules.system.domain.dto.RoleSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author mission
 * @since 2019/07/08 20:07
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleSmallMapper extends BaseMapper<RoleSmallDTO, Role> {

}
