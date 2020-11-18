package com.wteam.modules.system.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class DeptDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    @NotNull
    private Boolean enabled;

    /**
     * 上级部门
     */
    private Long parentId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptDTO> children;

    private Timestamp createdAt;

    public String getLabel() {
        return name;
    }
}