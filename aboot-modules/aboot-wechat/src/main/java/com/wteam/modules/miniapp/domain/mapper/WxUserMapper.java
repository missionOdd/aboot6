/*
 * copyleft © 2019-2021
 */

package com.wteam.modules.miniapp.domain.mapper;

import com.wteam.base.BaseMapper;
import com.wteam.modules.miniapp.domain.WxUser;
import com.wteam.modules.miniapp.domain.dto.WxUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
* 微信用户 领域转换层.
* @author aboot-system
* @since 2020-02-06
*/
@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WxUserMapper extends BaseMapper<WxUserDTO, WxUser> {

	@Mappings({
		@Mapping(target = "nickName", source = "nickname"),
		@Mapping(target = "phoneNumber", source = "phone"),
		@Mapping(target = "avatarUrl", source = "avatar")
	})
	@Override
	WxUser toEntity(WxUserDTO dto);

	@Mappings({
			@Mapping(target = "nickname", source = "nickName"),
			@Mapping(target = "phone", source = "phoneNumber"),
			@Mapping(target = "avatar", source = "avatarUrl")
	})
	@Override
	WxUserDTO toDto(WxUser entity);


}