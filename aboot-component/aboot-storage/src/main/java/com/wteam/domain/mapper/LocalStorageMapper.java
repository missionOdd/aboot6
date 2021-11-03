/*
 * copyleft © 2019-2021
 */
package com.wteam.domain.mapper;

import com.wteam.base.BaseMapper;
import com.wteam.domain.LocalStorage;
import com.wteam.domain.dto.LocalStorageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* 存储 领域转换层.
* @author mission
* @since 2019-11-03
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalStorageMapper extends BaseMapper<LocalStorageDTO, LocalStorage> {

}