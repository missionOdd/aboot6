package com.wteam.modules.system.domain.mapper;

import com.wteam.base.BaseMapper;
import com.wteam.modules.system.domain.Permission;
import com.wteam.modules.system.domain.dto.PermissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author mission
 * @since 2019/07/08 20:07
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper extends BaseMapper<PermissionDTO, Permission> {

}
