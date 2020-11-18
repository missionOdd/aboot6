package com.wteam.modules.common.domain.criteria;


import com.wteam.annotation.Query;
import lombok.Data;

/**
* 微信配置 搜索类.
* @author aboot-wechat
* @since 2020-02-06
*/
@Data
public class WxConfigQueryCriteria{
    /** 精确 */
    @Query
    private Integer type;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String appid;
}