package com.wteam.modules.system.domain.mapper;


import com.wteam.base.BaseMapper;
import com.wteam.modules.system.domain.Dept;
import com.wteam.modules.system.domain.dto.DeptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author mission
 * @since 2019/07/08 20:07
 */
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptMapper extends BaseMapper<DeptDTO, Dept> {

}