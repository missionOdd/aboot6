/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
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
