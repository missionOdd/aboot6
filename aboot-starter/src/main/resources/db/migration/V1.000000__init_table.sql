/* 2020/02/02 000000 mission */
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `parent_id` bigint(20) NOT NULL COMMENT '上级部门',
  `enabled` bit(1) NOT NULL COMMENT '状态',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '部门';

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '字典名称',
  `remark` varchar(255) NULL DEFAULT NULL COMMENT '描述',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '字典';

-- ----------------------------
-- Table structure for dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_detail`;
CREATE TABLE `sys_dict_detail`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `label` varchar(255) NOT NULL COMMENT '字典标签',
  `value` varchar(255) NOT NULL COMMENT '字典值',
  `sort` varchar(255) NULL DEFAULT NULL COMMENT '排序',
  `dict_id` bigint(11) NULL DEFAULT NULL COMMENT '字典id',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `detail_dict_id_index`(`dict_id`) USING BTREE,
  CONSTRAINT `FK5tpkputc6d9nboxojdbgnpmyb` FOREIGN KEY (`dict_id`) REFERENCES `sys_dict` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '字典详情';

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '岗位名',
  `sort` bigint(20)  NULL COMMENT '排序字段',
  `enabled` bit(1) NOT NULL COMMENT '是否启用 1启用 0不启用',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门编号',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `job_dept_id_index`(`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '岗位';

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `description` varchar(255) NULL DEFAULT NULL COMMENT '描述',
  `exception_detail` text NULL COMMENT '异常详细',
  `username` varchar(255) NULL DEFAULT NULL COMMENT '操作用户',
  `time` bigint(20) NULL DEFAULT NULL COMMENT '请求耗时',
  `method` varchar(255) NULL DEFAULT NULL COMMENT '方法名',
  `params` text NULL COMMENT '参数',
  `log_type` varchar(255) NULL DEFAULT NULL COMMENT '日志类型',
  `request_ip` varchar(255) NULL DEFAULT NULL COMMENT '请求ip',
  `address` varchar(255) NULL DEFAULT NULL COMMENT '请求地址',
  `browser` varchar(255) NULL DEFAULT NULL COMMENT '浏览器',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '日志';

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
 `name` varchar(255) NULL DEFAULT NULL COMMENT '菜单名称',
 `authorities` varchar(255) DEFAULT NULL COMMENT '权限标识集合',
 `i_frame` bit(1) NULL DEFAULT NULL COMMENT '是否外链',
 `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否显示',
 `cache` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否前端缓存',
 `component` varchar(255) NULL DEFAULT NULL COMMENT '组件',
 `parent_id` bigint(20) NOT NULL COMMENT '上级菜单ID',
 `sort` bigint(20) NOT NULL COMMENT '排序',
 `icon` varchar(255) NULL DEFAULT NULL COMMENT '图标',
 `path` varchar(255) NULL DEFAULT NULL COMMENT '链接地址',
 `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
 `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
 `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE utf8mb4_general_ci COMMENT = '菜单';

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `alias` varchar(255) NULL DEFAULT NULL COMMENT '别名',
  `name` varchar(255) NULL DEFAULT NULL COMMENT '名称',
  `parent_id` int(11) NOT NULL COMMENT '上级权限',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '权限';

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `authority` varchar(255) DEFAULT NULL COMMENT '权限标识',
  `data_scope` varchar(255) DEFAULT NULL COMMENT '数据范围',
  `level` int(255) DEFAULT NULL COMMENT '角色级别(越小越高)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
)  ENGINE = InnoDB COLLATE = utf8mb4_general_ci COMMENT='角色';

-- ----------------------------
-- Table structure for roles_depts_map
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles_depts_map`;
CREATE TABLE `sys_roles_depts_map`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE,
  INDEX `roles_dept_id_index`(`dept_id`) USING BTREE,
  CONSTRAINT `FK7qg6itn5ajdoa9h9o78v9ksur` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKrg1ci4cxxfbja0sb0pddju7k` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB COLLATE = utf8mb4_general_ci COMMENT = '角色可用部门';

-- ----------------------------
-- Table structure for roles_menus_map
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles_menus_map`;
CREATE TABLE `sys_roles_menus_map`  (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`menu_id`, `role_id`) USING BTREE,
  INDEX `menus_role_id_index`(`role_id`) USING BTREE,
  CONSTRAINT `FKcngg2qadojhi3a651a5adkvbq` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKq1knxf8ykt26we8k331naabjx` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB COLLATE = utf8mb4_general_ci COMMENT = '角色拥有菜单';

-- ----------------------------
-- Table structure for roles_permissions_map
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles_permissions_map`;
CREATE TABLE `sys_roles_permissions_map`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE,
  INDEX `roles_permission_id_index`(`permission_id`) USING BTREE,
  CONSTRAINT `FK4hrolwj4ned5i7qe8kyiaak6m` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKboeuhl31go7wer3bpy6so7exi` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB COLLATE = utf8mb4_general_ci COMMENT = '角色拥有权限';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`username` VARCHAR(64) NOT NULL COMMENT '用户名',
	`nickname` VARCHAR(64) NULL DEFAULT NULL COMMENT '昵称',
	`password` VARCHAR(128) NOT NULL COMMENT '密码',
	`login_type` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '用户登录类型',
	`phone` VARCHAR(64) NULL DEFAULT NULL COMMENT '手机',
	`email` VARCHAR(64) NULL DEFAULT NULL COMMENT '邮箱',
	`avatar` VARCHAR(255) NULL DEFAULT NULL COMMENT '头像地址',
	`sex` TINYINT(1) NULL DEFAULT '0' COMMENT '性别 0未知,1男,2女',
	`dept_id` BIGINT(20) NULL DEFAULT NULL COMMENT '部门编号',
	`job_id` BIGINT(20) NULL DEFAULT NULL COMMENT '岗位编号',
	`enabled` BIT(1) NOT NULL DEFAULT b'1' COMMENT '状态：1启用、0禁用',
	`last_password_reset_time` TIMESTAMP NULL DEFAULT NULL COMMENT '最后修改密码的日期',
	`last_login_time` TIMESTAMP NULL DEFAULT NULL COMMENT '最后登录时间',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
    `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE,
	INDEX `user_dept_id_index` (`dept_id`) USING BTREE,
	INDEX `user_job_id_index` (`job_id`) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT='用户';

-- ----------------------------
-- Table structure for users_roles_map
-- ----------------------------
DROP TABLE IF EXISTS `sys_users_roles_map`;
CREATE TABLE `sys_users_roles_map`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `role_id_index`(`role_id`) USING BTREE,
  CONSTRAINT `users_roles_map_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `users_roles_map_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB COLLATE = utf8mb4_general_ci COMMENT = '用户拥有角色';

-- ----------------------------
-- Table structure for visits
-- ----------------------------
DROP TABLE IF EXISTS `sys_visits`;
CREATE TABLE `sys_visits`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `date` varchar(64) NOT NULL COMMENT '日期',
  `ip_counts` bigint(20) NULL DEFAULT NULL COMMENT '访问量',
  `pv_counts` bigint(20) NULL DEFAULT NULL COMMENT 'ip量',
  `week_day` varchar(255) NULL DEFAULT NULL COMMENT '星期',
  `created_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `visits_date_index`(`date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '访问记录';

-- ----------------------------
-- Table structure for show_config
-- ----------------------------
DROP TABLE IF EXISTS `tool_show_config`;
CREATE TABLE `tool_show_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NULL DEFAULT NULL COMMENT '键',
  `value` varchar(255) NULL DEFAULT NULL COMMENT '值',
  `enabled` bit(1) NULL DEFAULT NULL  COMMENT '是否启用',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '前端配置';

-- ----------------------------
-- Table structure for local_storage
-- ----------------------------
DROP TABLE IF EXISTS `tool_local_storage`;
CREATE TABLE `tool_local_storage`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NULL DEFAULT NULL COMMENT '文件名',
  `operate` varchar(255) NULL DEFAULT NULL COMMENT '上传人',
  `path` varchar(255) NULL DEFAULT NULL COMMENT '磁盘路径',
  `real_name` varchar(255) NULL DEFAULT NULL COMMENT '文件真实名',
  `size` varchar(255) NULL DEFAULT NULL COMMENT '文件大小',
  `suffix` varchar(255) NULL DEFAULT NULL COMMENT '文件后缀',
  `type` varchar(255) NULL DEFAULT NULL COMMENT '文件类型',
  `md5` varchar(255) DEFAULT NULL COMMENT '文件MD5',
  `url` varchar(255) NULL DEFAULT NULL COMMENT '文件相对路径',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '存储';

DROP TABLE IF EXISTS `tool_group_data`;
CREATE TABLE `tool_group_data` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`group_name` VARCHAR(100) NOT NULL COMMENT '对应的数据名称',
	`value` TEXT NOT NULL COMMENT '数据组对应的数据值（json数据）',
	`sort` INT(11) NULL DEFAULT NULL COMMENT '排序字段',
	`enabled` BIT(1) NOT NULL COMMENT '状态（1：开启；2：关闭；）',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB COLLATE = utf8mb4_general_ci COMMENT = '数据组';

SET FOREIGN_KEY_CHECKS = 1;