/* 2020/02/02 000000 mission */
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for column_info
-- ----------------------------
DROP TABLE IF EXISTS `gen_column_info`;
CREATE TABLE `gen_column_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `table_name` varchar(64) NOT NULL COMMENT '数据库名称',
  `column_name` varchar(64) NOT NULL COMMENT '数据库字段名称',
  `column_type` varchar(64) NOT NULL COMMENT '数据库字段类型',
  `key_type` varchar(64) NULL DEFAULT NULL COMMENT '数据库字段键类型',
  `extra` varchar(64) NULL DEFAULT NULL COMMENT '字段额外的参数',
  `remark` text  NULL DEFAULT NULL COMMENT '数据库字段描述',
  `not_null` bit(1) NULL DEFAULT NULL COMMENT '必填',
  `list_show` bit(1) NULL DEFAULT NULL COMMENT '是否在列表显示',
  `form_show` bit(1) NULL DEFAULT NULL COMMENT '是否表单显示',
  `form_type` varchar(64) NULL DEFAULT NULL COMMENT '表单类型',
  `query_type` varchar(64) NULL DEFAULT NULL COMMENT '查询 1:模糊 2：精确',
  `dict_name` varchar(64) NULL DEFAULT NULL COMMENT '字典名称',
  `date_annotation` varchar(64) NULL DEFAULT NULL COMMENT '数据库字段键类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '数据库字段';

-- ----------------------------
-- Table structure for gen_config
-- ----------------------------
DROP TABLE IF EXISTS `gen_config`;
CREATE TABLE `gen_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `table_name` varchar(64) NOT NULL COMMENT '数据库表名',
  `table_comment` varchar(128) NULL DEFAULT NULL COMMENT '数据库表注备',
  `api_alias` varchar(64) NULL DEFAULT NULL COMMENT '接口名称',
  `pack` varchar(128) NULL DEFAULT NULL COMMENT '包路径',
  `module_name` varchar(128) NULL DEFAULT NULL COMMENT '模块名称',
  `path` varchar(255) NULL DEFAULT NULL COMMENT '前端代码生成的路径',
  `api_path` varchar(255) NULL DEFAULT NULL COMMENT '文件路径',
  `author` varchar(64) NULL DEFAULT NULL COMMENT '作者',
  `prefix` varchar(64) NULL DEFAULT NULL COMMENT '表前缀',
  `cover` bit(1) NOT NULL COMMENT '是否覆盖',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '生成配置';

-- ----------------------------
-- Table structure for gen_template
-- ----------------------------
DROP TABLE IF EXISTS `gen_template`;
CREATE TABLE `gen_template`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NULL DEFAULT NULL COMMENT '模板名字',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '类型',
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否生成该模板',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COLLATE = utf8mb4_general_ci COMMENT = '生成模板信息';

-- ----------------------------
-- Records of gen_template
-- ----------------------------
REPLACE INTO `gen_template` VALUES (1, 'Entity', 0, b'1', '2019-09-28 15:57:48', 0, NULL, '2019-10-15 09:46:50', 1);
REPLACE INTO `gen_template` VALUES (2, 'DTO', 0, b'1', '2019-09-28 15:57:48', 0, NULL, '2019-09-28 16:33:45', 1);
REPLACE INTO `gen_template` VALUES (3, 'Mapper', 0, b'1', '2019-09-28 15:57:48', 0, NULL, '2019-09-28 16:33:45', 1);
REPLACE INTO `gen_template` VALUES (4, 'Repository', 0, b'1', '2019-09-28 15:57:48', 0, NULL, '2019-09-28 16:33:45', 1);
REPLACE INTO `gen_template` VALUES (5, 'Service', 0, b'1', '2019-09-28 15:57:48', 0, NULL, '2019-09-28 16:33:45', 1);
REPLACE INTO `gen_template` VALUES (6, 'ServiceImpl', 0, b'1', '2019-09-28 15:57:48', 0, NULL, '2019-09-28 16:33:45', 1);
REPLACE INTO `gen_template` VALUES (7, 'QueryCriteria', 0, b'1', '2019-09-28 15:57:48', 0, NULL, '2019-09-28 16:33:45', 1);
REPLACE INTO `gen_template` VALUES (8, 'Controller', 0, b'1', '2019-09-28 15:57:48', 0, NULL, '2019-09-28 16:33:45', 1);
REPLACE INTO `gen_template` VALUES (9, 'api', 1, b'0', '2019-09-28 15:57:48', 0, NULL, '2019-10-15 09:46:50', 1);
REPLACE INTO `gen_template` VALUES (10, 'index', 1, b'0', '2019-09-28 15:57:48', 0, NULL, '2019-10-15 09:46:50', 1);
REPLACE INTO `gen_template` VALUES (11, 'eForm', 1, b'0', '2019-09-28 15:57:48', 0, NULL, '2019-10-15 09:46:50', 1);
