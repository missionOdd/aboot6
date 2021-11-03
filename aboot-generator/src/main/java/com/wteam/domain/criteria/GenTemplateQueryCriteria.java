/*
 * copyleft © 2019-2021
 */

package com.wteam.domain.criteria;


import com.wteam.annotation.Query;
import lombok.Data;


/**
* 生成模板信息 搜索类.
* @author mission
* @since 2019-09-29
*/
@Data
public class GenTemplateQueryCriteria{

        // 模糊
        @Query(type = Query.Type.INNER_LIKE)
        private String name;

        // 精确
        @Query
        private Integer type;

        // 精确
        @Query
        private Boolean enabled;
}