package com.wteam.modules.system.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author mission
 * @since 2019/07/08 19:57
 */
@Data
public class PermissionDTO implements Serializable{

	private Long id;

	private String name;

	private Long parentId;

	private String alias;

	private Timestamp createdAt;

	private List<PermissionDTO>  children;

}
