/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.base;

/**
 * @author mission
 * @since 2020/07/28 15:57
 */
public interface BaseCons {

	public String SOFT_DELETE=" deleted_at is null ";


	/***
	 * 默认字段名定义
	 */
	public enum Field{
		/**
		 * 主键属性名
		 */
		id,
		/**
		 * 默认的上级ID属性名
		 */
		parentId,

		/**
		 * 创建人
		 */
		createdBy,

		/**
		 * 创建人
		 */
		updatedBy,

		/**
		 * 子节点属性名
		 */
		children,
		/**
		 * 逻辑删除标记字段
		 */
		deletedAt,
		/**
		 * 创建时间
		 */
		createdAt,
		/**
		 * 更新时间
		 */
		updatedAt,

	}
}
