/*
 * copyleft © 2019-2021
 */

package com.wteam.modules.system.domain.criteria;

import com.wteam.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mission
 * @since 2020/08/07 14:17
 */
@Data
public class RoleQueryCriteria {

	@ApiModelProperty(value = "角色名")
	@Query(type = Query.Type.INNER_LIKE)
	private String name;
}
