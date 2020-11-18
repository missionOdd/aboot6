package com.wteam.modules.system.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
@NoArgsConstructor
public class JobDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Boolean enabled;

    /**
     * 部门
     */
    private DeptDTO dept;

    /**
     * 如果分公司存在相同部门，则显示上级部门名称
     */
    private String deptSuperiorName;

    /**
     * 创建日期
     */
    private Timestamp createdAt;

    public JobDTO(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}