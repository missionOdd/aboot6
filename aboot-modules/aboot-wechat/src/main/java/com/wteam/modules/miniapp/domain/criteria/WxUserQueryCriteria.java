/*
 * copyleft © 2019-2021
 */

package com.wteam.modules.miniapp.domain.criteria;

import com.wteam.annotation.Query;
import lombok.Data;

/**
* 微信用户 搜索类.
* @author aboot-system
* @since 2020-02-06
*/
@Data
public class WxUserQueryCriteria{
    /** 精确 */
    @Query
    private String openid;
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String nickname;
    /** 精确 */
    @Query
    private Integer gender;
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String province;
    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String city;
    /** 模糊 */
    @Query(propName = "phoneNumber",type = Query.Type.INNER_LIKE)
    private String phone;
}