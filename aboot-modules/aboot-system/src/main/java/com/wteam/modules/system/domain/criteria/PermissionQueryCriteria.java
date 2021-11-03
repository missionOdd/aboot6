/*
 * copyleft © 2019-2021
 */

package com.wteam.modules.system.domain.criteria;

import com.wteam.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mission
 * @since 2020/08/07 14:49
 */
@Data
public class PermissionQueryCriteria {


	@ApiModelProperty(value = "权限名")
	@Query(type = Query.Type.INNER_LIKE)
	private String name;
}
