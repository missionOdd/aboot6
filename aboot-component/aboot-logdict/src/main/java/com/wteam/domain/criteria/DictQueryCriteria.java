/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.domain.criteria;

import com.wteam.annotation.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 字典查询类
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictQueryCriteria {
    /*名字查询*/
    @Query(propName = "name",type = Query.Type.IN)
    private List<String> names;
}