package com.wteam.modules.system.domain.mapper;

import com.wteam.base.BaseMapper;
import com.wteam.modules.system.domain.User;
import com.wteam.modules.system.domain.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author mission
 * @since 2019/07/08 20:07
 */
@Mapper(componentModel = "spring",uses = {RoleMapper.class, DeptMapper.class, JobMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDTO, User> {

}
