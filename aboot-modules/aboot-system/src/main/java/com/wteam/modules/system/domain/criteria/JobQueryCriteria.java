package com.wteam.modules.system.domain.criteria;

import com.wteam.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import java.util.Set;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
@NoArgsConstructor
public class JobQueryCriteria {

    @ApiModelProperty(value = "岗位名")
    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @ApiModelProperty(value = "岗位状态 true正常/false停用")
    @Query
    private Boolean enabled;

    @ApiModelProperty(value = "部门编号, 按选择的部门查询, 好像也不用传")
    @Query(propName = "id", joinName = "dept")
    private Long deptId;

    @ApiModelProperty(value = "部门, 无需传入")
    @Query(propName = "id", joinName = "dept", type = Query.Type.IN)
    private Set<Long> deptIds;


    @ApiModelProperty("操作时间 >大于")
    @Query(propName = "createdAt",type = Query.Type.GREATER_THAN)
    private Timestamp greatTime;

    @ApiModelProperty("操作时间 <小于")
    @Query(propName = "createdAt",type = Query.Type.LESS_THAN)
    private Timestamp lessTime;
}