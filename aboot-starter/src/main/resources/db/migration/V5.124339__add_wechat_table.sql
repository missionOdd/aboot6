/* 2020/03/11 121830 mission */
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user` (
	`uid` BIGINT(20) NOT NULL COMMENT '用户ID',
	`openid` VARCHAR(64) NOT NULL COMMENT '平台ID',
  `appid` VARCHAR(255) NOT NULL COMMENT '应用ID',
	`nickname` VARCHAR(64) NULL DEFAULT NULL COMMENT '昵称',
	`avatar` VARCHAR(255) NULL DEFAULT NULL COMMENT '头像',
	`gender` INT(11) NULL DEFAULT NULL COMMENT '性别',
	`province` VARCHAR(32) NULL DEFAULT NULL COMMENT '省份',
	`city` VARCHAR(32) NULL DEFAULT NULL COMMENT '城市',
	`phone` VARCHAR(32) NULL DEFAULT NULL COMMENT '电话',
	`country_code` VARCHAR(32) NULL DEFAULT NULL COMMENT '区号',
	`language` VARCHAR(32) NULL DEFAULT NULL COMMENT '语言',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
	PRIMARY KEY (`uid`)
)COMMENT='微信用户' COLLATE = utf8mb4_general_ci ENGINE=InnoDB;

DROP TABLE IF EXISTS `wx_config`;
CREATE TABLE `wx_config` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`type` INT(11) NOT NULL COMMENT '配置类型 公众号0 小程序1',
	`appid` VARCHAR(255) NOT NULL COMMENT '应用ID',
	`secret` VARCHAR(255) NOT NULL COMMENT '应用密钥',
	`token` VARCHAR(255) NULL DEFAULT NULL COMMENT '应用Token',
	`aes_key` VARCHAR(255) NULL DEFAULT NULL COMMENT '解密密钥',
	`msg_data_format` VARCHAR(32) NULL DEFAULT NULL COMMENT '返回格式',
	`logo` VARCHAR(255) NULL DEFAULT NULL COMMENT 'logo',
	`remark` VARCHAR(255) NULL DEFAULT NULL COMMENT '备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
	PRIMARY KEY (`id`)
)COMMENT='微信配置' COLLATE = utf8mb4_general_ci ENGINE=InnoDB;

DROP TABLE IF EXISTS `wx_pay_config`;
CREATE TABLE `wx_pay_config` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`appid` VARCHAR(255) NOT NULL COMMENT '设置微信公众号或者小程序等的appid',
	`mch_id` VARCHAR(128) NOT NULL COMMENT '微信支付商户号',
	`mch_key` VARCHAR(255) NOT NULL COMMENT '微信支付商户密钥',
	`mch_path` VARCHAR(255) NULL DEFAULT NULL COMMENT 'apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定',
	`remark` VARCHAR(255) NULL DEFAULT NULL COMMENT '备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '逻辑删除时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '修改人',
	PRIMARY KEY (`id`)
)COMMENT='微信支付配置' COLLATE = utf8mb4_general_ci ENGINE=InnoDB;

DROP TABLE IF EXISTS `pay_log`;
CREATE TABLE `pay_log` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`app_id` VARCHAR(255) NOT NULL COMMENT '应用id' COLLATE 'utf8mb4_general_ci',
	`app_order_id` VARCHAR(255) NOT NULL COMMENT '应用方订单号' COLLATE 'utf8mb4_general_ci',
	`transaction_id` VARCHAR(255) NOT NULL COMMENT '本次交易唯一id，整个支付系统唯一，生成他的原因主要是 order_id对于其它应用来说可能重复' COLLATE 'utf8mb4_general_ci',
	`exception_detail` VARCHAR(255) NULL DEFAULT NULL COMMENT '异常详细' COLLATE 'utf8mb4_general_ci',
	`username` VARCHAR(255) NULL DEFAULT NULL COMMENT '操作用户' COLLATE 'utf8mb4_general_ci',
	`time` BIGINT(20) NULL DEFAULT NULL COMMENT '请求耗时',
	`method` VARCHAR(255) NULL DEFAULT NULL COMMENT '方法名' COLLATE 'utf8mb4_general_ci',
	`params` TEXT NULL DEFAULT NULL COMMENT '支付的请求参数' COLLATE 'utf8mb4_general_ci',
	`log_type` VARCHAR(32) NOT NULL COMMENT '日志类型，payment:支付; refund:退款; notify:异步通知; return:同步通知; query:查询' COLLATE 'utf8mb4_general_ci',
	`request_ip` VARCHAR(32) NULL DEFAULT NULL COMMENT '请求ip' COLLATE 'utf8mb4_general_ci',
	`address` VARCHAR(255) NULL DEFAULT NULL COMMENT '请求地址' COLLATE 'utf8mb4_general_ci',
	`browser` VARCHAR(255) NULL DEFAULT NULL COMMENT '浏览器' COLLATE 'utf8mb4_general_ci',
	`created_at` TIMESTAMP NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
	`created_by` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '创建人',
	`deleted_at` TIMESTAMP NULL DEFAULT NULL COMMENT '逻辑删除时间',
	`updated_at` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '修改时间',
	`updated_by` BIGINT(20) NULL DEFAULT NULL COMMENT '修改人',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='交易日志表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

INSERT INTO `wx_config`(`id`, `type`, `appid`, `secret`, `token`, `aes_key`, `msg_data_format`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `updated_by`) VALUES (1, 1, '', '', NULL, NULL, 'JSON', '2020-08-10 10:55:13', 0, NULL, '2020-08-10 10:55:13', NULL);
INSERT INTO `wx_pay_config`(`id`, `appid`, `mch_id`, `mch_key`, `mch_path`, `remark`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `updated_by`) VALUES (1, '', '', '', 'classpath:apiclient_cert.p12', NULL, '2020-08-10 11:20:05', 0, NULL, '2020-08-10 17:33:50', NULL);
