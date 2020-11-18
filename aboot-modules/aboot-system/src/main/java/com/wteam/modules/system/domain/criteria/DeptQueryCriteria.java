package com.wteam.modules.system.domain.criteria;

import com.wteam.annotation.Query;
import lombok.Data;

import java.util.Set;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class DeptQueryCriteria{

    @Query(type = Query.Type.IN, propName="id")
    private Set<Long> ids;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    private Boolean enabled;

    @Query
    private Long parentId;
}