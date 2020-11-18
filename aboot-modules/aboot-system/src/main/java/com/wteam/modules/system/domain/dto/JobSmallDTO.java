package com.wteam.modules.system.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
@NoArgsConstructor
public class JobSmallDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;
}