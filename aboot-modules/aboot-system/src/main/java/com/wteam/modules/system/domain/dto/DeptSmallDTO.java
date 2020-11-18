package com.wteam.modules.system.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class DeptSmallDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;
}