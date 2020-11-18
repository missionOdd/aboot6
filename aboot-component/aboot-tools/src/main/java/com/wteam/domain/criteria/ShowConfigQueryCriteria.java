/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */
package com.wteam.domain.criteria;


import com.wteam.annotation.Query;
import lombok.Data;


/**
* 前端配置 搜索类.
* @author mission
* @since 2019-10-15
*/
@Data
public class ShowConfigQueryCriteria{

        // 精确
        @Query
        private Boolean enabled;

        // 模糊
        @Query(type = Query.Type.INNER_LIKE)
        private String name;
}