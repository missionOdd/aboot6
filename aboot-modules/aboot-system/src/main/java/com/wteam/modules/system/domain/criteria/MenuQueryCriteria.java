package com.wteam.modules.system.domain.criteria;

import com.wteam.annotation.Query;
import lombok.Data;

/**
 * @author mission
 * @since 2020/01/02 11:28
 */
@Data
public class MenuQueryCriteria {

    @Query(blurry = "name,path,component")
    private String name;

}
