/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 *  Notice: Whale Cloud Inc copyrights this specification.
 *  No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 */

package com.wteam.domain.dto;

import com.wteam.annotation.Query;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class DictDTO implements Serializable {

    private Long id;

    /**
     * 字典名称
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    /**
     * 描述
     */
    @Query(type = Query.Type.INNER_LIKE)
    private String remark;



    private Timestamp createdAt;
}
