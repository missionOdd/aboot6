package com.wteam.modules.system.domain.mapper;

import com.wteam.base.BaseMapper;
import com.wteam.modules.system.domain.Role;
import com.wteam.modules.system.domain.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author mission
 * @since 2019/07/08 20:07
 */
@Mapper(componentModel = "spring", uses = {PermissionMapper.class, MenuMapper.class, DeptMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends BaseMapper<RoleDTO, Role> {

}
