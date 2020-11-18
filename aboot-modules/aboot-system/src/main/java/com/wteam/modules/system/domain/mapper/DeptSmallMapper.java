package com.wteam.modules.system.domain.mapper;


import com.wteam.base.BaseMapper;
import com.wteam.modules.system.domain.Dept;
import com.wteam.modules.system.domain.dto.DeptSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
* @since 2019-03-25
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptSmallMapper extends BaseMapper<DeptSmallDTO, Dept> {

}