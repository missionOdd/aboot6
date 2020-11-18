package com.wteam.modules.common.domain.mapper;

import com.wteam.base.BaseMapper;
import com.wteam.modules.common.domain.WxConfig;
import com.wteam.modules.common.domain.dto.WxConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
* 微信配置 领域转换层.
* @author aboot-wechat
* @since 2020-02-06
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WxConfigMapper extends BaseMapper<WxConfigDTO, WxConfig> {

}