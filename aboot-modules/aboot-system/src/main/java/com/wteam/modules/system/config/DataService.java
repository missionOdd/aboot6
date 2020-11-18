/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.system.config;

import com.wteam.modules.system.domain.Dept;
import com.wteam.modules.system.domain.dto.RoleSmallDTO;
import com.wteam.modules.system.domain.dto.UserDTO;
import com.wteam.modules.system.service.DeptService;
import com.wteam.modules.system.service.RoleService;
import com.wteam.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 数据权限服务实现
 * @author mission
 * @since 2020/08/07 18:07
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "data")
public class DataService {

	private final RoleService roleService;
	private final DeptService deptService;
	private final UserService userService;

	@Cacheable(key = "'user:' + #p0")
	public Set<Long> getDeptIds(Long userId){

		UserDTO userDTO=userService.findDTOById(userId);
		//用于存储部门id
		Set<Long> deptIds =new HashSet<>();
		//查询用户角色
		List<RoleSmallDTO> roleSet =roleService.findByUsersId(userDTO.getId());
		for (RoleSmallDTO role : roleSet) {

			DataScopeEnum scopeType = DataScopeEnum.find(role.getDataScope());
			switch (Objects.requireNonNull(scopeType)) {
				case THIS_LEVEL:
					deptIds.add(userDTO.getDept().getId());
					break;
				case CUSTOMIZE:
					Set<Dept> depts =deptService.findByRoleIds(role.getId());
					for (Dept dept : depts) {
						if (dept!=null && dept.getEnabled()){
							deptIds.add(dept.getId());
							List<Dept> deptChildren =deptService.findByPid(dept.getId());
							if (deptChildren!=null && deptChildren.size() != 0){
								deptIds.addAll(getDeptChildren(deptChildren));
							}
						}
					}
					break;
				default:
					return deptIds;
			}
		}
		return deptIds;
	}

	public List<Long> getDeptChildren(List<Dept> deptList) {
		List<Long> list = new ArrayList<>();
		deptList.forEach(dept -> {
					if (dept!=null && dept.getEnabled()){
						List<Dept> depts = deptService.findByPid(dept.getId());
						if(deptList.size() != 0){
							list.addAll(getDeptChildren(depts));
						}
						list.add(dept.getId());
					}
				}
		);
		return list;
	}

}
