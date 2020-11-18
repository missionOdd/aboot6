/* 2020/02/02 000001 mission */

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Records of dept
-- ----------------------------
REPLACE INTO `sys_dept` VALUES (1, '总部', 0, b'1', '2019-03-25 09:14:05',1, NULL, '2019-09-04 20:25:16', NULL);

-- ----------------------------
-- Records of dict
-- ----------------------------
REPLACE INTO `sys_dict` VALUES (1, 'user_status', '用户状态', '2019-07-14 10:51:35', 0, NULL, '2019-07-14 10:51:35', NULL);
REPLACE INTO `sys_dict` VALUES (2, 'dept_status', '部门状态', '2019-07-14 10:51:35', 0, NULL, '2019-09-04 20:04:48', NULL);
REPLACE INTO `sys_dict` VALUES (3, 'job_status', '岗位状态', '2019-07-14 10:51:35', 0, NULL, '2019-09-04 20:04:51', NULL);

-- ----------------------------
-- Records of dict_detail
-- ----------------------------
REPLACE INTO `sys_dict_detail` VALUES (1, '激活', 'true', '1', 1, '2019-07-14 10:51:35', 0, NULL, '2019-07-14 10:51:36', NULL);
REPLACE INTO `sys_dict_detail` VALUES (2, '锁定', 'false', '2', 1, '2019-07-14 10:51:35', 0, NULL, '2019-07-14 10:51:36', NULL);
REPLACE INTO `sys_dict_detail` VALUES (3, '正常', 'true', '1', 2, '2019-07-14 10:51:35', 0, NULL, '2019-09-04 20:05:11', NULL);
REPLACE INTO `sys_dict_detail` VALUES (4, '停用', 'false', '2', 2, '2019-07-14 10:51:35', 0, NULL, '2019-09-04 20:05:11', NULL);
REPLACE INTO `sys_dict_detail` VALUES (5, '正常', 'true', '1', 3, '2019-07-14 10:51:35', 0, NULL, '2019-09-04 20:05:15', NULL);
REPLACE INTO `sys_dict_detail` VALUES (6, '停用', 'false', '2', 3, '2019-07-14 10:51:35', 0, NULL, '2019-09-04 20:05:15', NULL);

-- ----------------------------
-- Records of job
-- ----------------------------
REPLACE INTO `sys_job` VALUES (1, '执行董事', 1, b'1', 1, '2019-08-20 16:18:14',1,  NULL, '2020-01-11 09:17:22',NULL);

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
REPLACE INTO `sys_menu` VALUES (1, '系统管理', NULL, b'0', b'1', b'0', '', 0, 990, '系统管理', '', '2019-07-26 17:39:58', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (2, '用户管理', NULL, b'0', b'1', b'0', '/system/user/index', 1, 0, '用户', '/home/user_manage', '2019-07-26 17:40:30', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (3, '角色管理', NULL, b'0', b'1', b'0', '/system/role/index', 1, 0, '角色管理', '/home/role_manage', '2019-07-26 17:40:59', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (4, '权限管理', NULL, b'0', b'1', b'0', '/system/authority/index', 1, 0, '权限管理', '/home/authority_manage', '2019-07-26 17:41:34', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (5, '菜单管理', NULL, b'0', b'1', b'0', '/system/menu/index', 1, 0, '菜单', '/home/menu_manage', '2019-07-26 17:41:52', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (6, '字典管理', NULL, b'0', b'1', b'0', '/system/dictionary/index', 1, 0, '字典管理', '/home/dictionary_manage', '2019-07-26 17:42:12', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (7, '部门管理', NULL, b'0', b'1', b'0', '/system/department/index', 1, 0, '部门管理', '/home/department_manage', '2019-07-26 17:42:33', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (8, '岗位管理', NULL, b'0', b'1', b'0', '/system/station/index', 1, 0, '岗位图标', '/home/station_manage', '2019-07-26 17:43:04', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (9, '日志管理', NULL, b'0', b'1', b'0', '', 0, 991, '日志管理', '', '2019-07-26 17:43:27', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (10, '操作日志', NULL, b'0', b'1', b'0', '/log/operation_log/index', 9, 0, '操作_设置', '/home/operation_log', '2019-07-26 17:43:53', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (11, '异常日志', NULL, b'0', b'1', b'0', '/log/exception_log/index', 9, 0, '异常', '/home/exception_log', '2019-07-26 17:44:24', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (12, '系统监控', NULL, b'0', b'1', b'0', '', 0, 992, '监控', '', '2019-07-26 17:55:38', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (13, '系统缓存', NULL, b'0', b'1', b'0', '/monitor/redis_manage/index', 14, 1, '清除缓存', '/home/redis_manage', '2019-07-26 17:56:34', 0, NULL, '2020-09-12 10:49:58', NULL);
REPLACE INTO `sys_menu` VALUES (14, '系统工具', NULL, b'0', b'1', b'0', '', 0, 993, '工具', '', '2019-07-26 20:53:33', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (15, '平台介绍', NULL, b'0', b'0', b'0', '/introduction/index', 0, 999, '平台介绍', '/home/introduction', '2019-07-27 14:26:42', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (16, '多级菜单', NULL, b'0', b'1', b'0', '', 0, 926, '菜单', '', '2019-07-31 12:06:26', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (17, '二级子菜单', NULL, b'0', b'1', b'0', '/menu_1/index', 16, 0, '菜单', '/home/menu_1', '2019-07-31 12:07:14', 0, NULL, '2020-09-12 10:42:24', NULL);
REPLACE INTO `sys_menu` VALUES (18, '三级菜单', NULL, b'0', b'1', b'0', '', 16, 1, '菜单', '', '2019-07-31 12:07:46', 0, NULL, '2020-09-12 10:42:31', NULL);
REPLACE INTO `sys_menu` VALUES (19, '三级子菜单', NULL, b'0', b'1', b'0', '/menu_2/index', 18, 0, '菜单', '/home/menu_2', '2019-07-31 12:08:35', 0, NULL, '2020-09-12 10:48:12', NULL);
REPLACE INTO `sys_menu` VALUES (20, '四级菜单', NULL, b'0', b'1', b'0', '', 18, 1, '菜单', '', '2019-07-31 12:09:16', 0, NULL, '2020-09-12 10:48:15', NULL);
REPLACE INTO `sys_menu` VALUES (21, '四级子菜单', NULL, b'0', b'1', b'0', '/menu_3/index', 20, 0, '菜单', '/home/menu_3', '2019-07-31 12:09:45', 0, NULL, '2020-09-12 10:43:01', NULL);
REPLACE INTO `sys_menu` VALUES (22, '文件预览', NULL, b'0', b'1', b'0', '/common/file/index.vue', 24, 4, '文章分类', '/home/preview_file', '2019-08-02 11:06:24', 0, NULL, '2020-09-12 10:43:45', NULL);
REPLACE INTO `sys_menu` VALUES (23, 'markdown', NULL, b'0', b'1', b'1', '/common/markdown/index', 24, 3, 'markdown', '/home/markdown', '2019-08-02 16:59:56', 0, NULL, '2020-09-12 10:43:45', NULL);
REPLACE INTO `sys_menu` VALUES (24, '常用工具', NULL, b'0', b'1', b'0', '', 0, 925, 'running', '', '2019-08-02 18:48:46', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (25, '富文本', NULL, b'0', b'1', b'0', '/common/editor/index', 24, 2, '文章管理', '/home/editor', '2019-08-02 18:50:47', 0, NULL, '2020-09-12 10:43:49', NULL);
REPLACE INTO `sys_menu` VALUES (26, '图标库', NULL, b'0', b'1', b'1', '/common/icon/index', 24, 1, 'iconfont', '/home/icon', '2019-08-02 18:54:09', 0, NULL, '2020-09-12 10:43:49', NULL);
REPLACE INTO `sys_menu` VALUES (27, '个人中心', NULL, b'0', b'0', b'0', '/person/index', 0, 1, '个人中心', '/home/person', '2019-08-03 11:29:17', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (28, '图表展示', NULL, b'0', b'1', b'0', '/dashboard/index', 0, 0, '图表', '/home/dashboard', '2019-08-04 20:10:01', 0, NULL, '2020-08-28 18:04:04', NULL);
REPLACE INTO `sys_menu` VALUES (29, '定时任务', NULL, b'0', b'1', b'0', '/tools/mission/index', 14, 1, '任务', '/home/mission_manage', '2019-08-10 16:35:41', 0, NULL, '2020-09-12 10:44:16', NULL);
REPLACE INTO `sys_menu` VALUES (30, '代码生成', NULL, b'0', b'1', b'0', '/tools/generator/index', 14, 3, 'dev', '/home/generator_code', '2019-09-26 19:22:41', 0, NULL, '2020-09-12 10:44:16', NULL);
REPLACE INTO `sys_menu` VALUES (31, '存储管理', NULL, b'0', b'1', b'0', '/tools/file/index', 14, 4, '文件', '/home/file_manage', '2019-09-29 20:22:32', 0, NULL, '2020-09-12 10:44:16', NULL);
REPLACE INTO `sys_menu` VALUES (32, '在线用户', NULL, b'0', b'1', b'0', '/monitor/online_user/index.vue', 12, 0, 'peoples', '/home/online_user', '2019-11-03 11:27:37', 0, NULL, '2020-09-12 10:44:24', NULL);
REPLACE INTO `sys_menu` VALUES (33, '生成配置', NULL, b'0', b'0', b'0', '/tools/generator/views/config', 30, 0, '', '/home/generator_config', '2019-09-29 20:22:32', 0, NULL, '2020-09-12 10:44:29', NULL);

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
REPLACE INTO `sys_permission` VALUES (1, '超级管理员', 'ADMIN', 0, '2018-12-03 12:27:48', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (2, '用户管理', 'USER:all', 0, '2018-12-03 12:28:19', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (3, '用户查询', 'USER:list', 2, '2018-12-03 12:31:35', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (4, '用户创建', 'USER:add', 2, '2018-12-03 12:31:35', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (5, '用户编辑', 'USER:edit', 2,  '2018-12-03 12:31:35', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (6, '用户删除', 'USER:del', 2, '2018-12-03 12:31:35', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (7, '角色管理', 'ROLES:all', 0, '2018-12-03 12:28:19', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (8, '角色查询', 'ROLES:list', 7, '2018-12-03 12:31:35', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (10, '角色创建', 'ROLES:add', 7, '2018-12-09 20:10:16', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (11, '角色编辑', 'ROLES:edit', 7, '2018-12-09 20:10:42', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (12, '角色删除', 'ROLES:del', 7, '2018-12-09 20:11:07', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (13, '权限管理', 'PERMISSION:all', 0, '2018-12-09 20:11:37', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (14, '权限查询', 'PERMISSION:list', 13, '2018-12-09 20:11:55', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (15, '权限创建', 'PERMISSION:add', 13, '2018-12-09 20:14:10', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (16, '权限编辑', 'PERMISSION:edit', 13, '2018-12-09 20:15:44', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (17, '权限删除', 'PERMISSION:del', 13, '2018-12-09 20:15:59', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (18, '缓存管理', 'REDIS:all', 0, '2018-12-17 13:53:25', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (20, '缓存查询', 'REDIS:list', 18, '2018-12-17 13:54:07', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (22, '缓存删除', 'REDIS:del', 18, '2018-12-17 13:55:04', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (23, '图片管理', 'PICTURE:all', 0, '2018-12-27 20:31:49', 0, NULL, '2019-07-23 20:20:26',NULL);
REPLACE INTO `sys_permission` VALUES (24, '图片查询', 'PICTURE:list', 23, '2018-12-27 20:32:04', 0, NULL, '2019-07-23 20:20:27',NULL);
REPLACE INTO `sys_permission` VALUES (25, '上传图片', 'PICTURE_UPLOAD', 23, '2018-12-27 20:32:24', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (26, '图片删除', 'PICTURE:del', 23, '2018-12-27 20:32:45', 0, NULL, '2019-07-23 20:20:27',NULL);
REPLACE INTO `sys_permission` VALUES (29, '菜单管理', 'MENU:all', 0, '2018-12-28 17:34:31', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (30, '菜单查询', 'MENU:list', 29, '2018-12-28 17:34:41', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (31, '菜单创建', 'MENU:add', 29, '2018-12-28 17:34:52', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (32, '菜单编辑', 'MENU:edit', 29, '2018-12-28 17:35:20', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (33, '菜单删除', 'MENU:del', 29, '2018-12-28 17:35:29', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (35, '岗位管理', 'JOB:all', 0, '2019-01-08 14:59:57', 0,NULL, '2019-07-22 21:11:41',NULL);
REPLACE INTO `sys_permission` VALUES (36, '岗位查询', 'JOB:list', 35, '2019-01-08 15:00:09', 0, NULL, '2019-07-22 21:11:41',NULL);
REPLACE INTO `sys_permission` VALUES (37, '岗位创建', 'JOB:add', 35, '2019-01-08 15:00:20', 0, NULL, '2019-07-22 21:11:41',NULL);
REPLACE INTO `sys_permission` VALUES (38, '岗位编辑', 'JOB:edit', 35, '2019-01-08 15:00:33', 0, NULL, '2019-07-22 21:11:41',NULL);
REPLACE INTO `sys_permission` VALUES (39, '岗位删除', 'JOB:del', 35, '2019-01-08 15:01:13', 0, NULL, '2019-07-22 21:11:41',NULL);
REPLACE INTO `sys_permission` VALUES (40, '部门管理', 'DEPT:all', 0, '2019-03-29 17:06:55', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (41, '部门查询', 'DEPT:list', 40, '2019-03-29 17:07:09', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (42, '部门创建', 'DEPT:add', 40, '2019-03-29 17:07:29', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (43, '部门编辑', 'DEPT:edit', 40, '2019-03-29 17:07:52', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (44, '部门删除', 'DEPT:del', 40, '2019-03-29 17:08:14', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (45, '岗位管理', 'USERJOB:all', 0, '2019-03-29 17:08:52', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (46, '岗位查询', 'USERJOB:list', 45, '2019-03-29 17:10:27', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (47, '岗位创建', 'USERJOB:add', 45, '2019-03-29 17:10:55', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (48, '岗位编辑', 'USERJOB:edit', 45, '2019-03-29 17:11:08', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (49, '岗位删除', 'USERJOB:del', 45, '2019-03-29 17:11:19', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (50, '字典管理', 'DICT:all', 0, '2019-04-10 16:24:51', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (51, '字典查询', 'DICT:list', 50, '2019-04-10 16:25:16', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (52, '字典创建', 'DICT:add', 50, '2019-04-10 16:25:29', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (53, '字典编辑', 'DICT:edit', 50, '2019-04-10 16:27:19', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (54, '字典删除', 'DICT:del', 50, '2019-04-10 16:27:30', 0, NULL, '2019-07-14 10:51:37',NULL);
REPLACE INTO `sys_permission` VALUES (56, '权限测试', 'PERMISSION_TEST', 13, '2019-07-22 20:22:39', 0, NULL, '2019-07-22 20:22:39',NULL);
REPLACE INTO `sys_permission` VALUES (67, '权限日志管理', 'AUTHLOG:all', 0, '2019-07-22 21:17:32', 0, NULL, '2019-07-22 21:17:32',NULL);
REPLACE INTO `sys_permission` VALUES (68, '权限日志创建', 'AUTHLOG:add', 67, '2019-07-22 21:17:32', 0, NULL, '2019-07-22 21:17:32',NULL);
REPLACE INTO `sys_permission` VALUES (69, '权限日志编辑', 'AUTHLOG:edit', 67, '2019-07-22 21:17:32', 0, NULL, '2019-07-22 21:17:32',NULL);
REPLACE INTO `sys_permission` VALUES (70, '权限日志查询', 'AUTHLOG:list', 67, '2019-07-22 21:17:32', 0, NULL, '2019-07-22 21:17:32',NULL);
REPLACE INTO `sys_permission` VALUES (71, '权限日志删除', 'AUTHLOG:del', 67, '2019-07-22 21:17:32', 0, NULL, '2019-07-22 21:17:32',NULL);
REPLACE INTO `sys_permission` VALUES (72, '测试', 'ALLP_CC', 25, '2019-07-23 08:06:45', 0, NULL, '2019-07-23 08:07:18',NULL);
REPLACE INTO `sys_permission` VALUES (73, '图片创建', 'PICTURE:add', 23, '2019-07-23 20:20:27', 0, NULL, '2019-07-23 20:20:27',NULL);
REPLACE INTO `sys_permission` VALUES (74, '图片编辑', 'PICTURE:edit', 23, '2019-07-23 20:20:27', 0, NULL, '2019-07-23 20:20:27',NULL);

-- ----------------------------
-- Records of role
-- ----------------------------
REPLACE INTO `sys_role` VALUES (1, '超级管理员','ADMIN', '全部', 1,'系统所有权', '2018-11-23 11:04:37', 1, NULL,'2019-08-21 10:14:20', NULL);
REPLACE INTO `sys_role` VALUES (2, '普通用户',NULL, '本级', 3,'用于测试菜单与权限',  '2018-11-23 13:09:06', 1, NULL, '2019-08-11 18:29:07', NULL);
REPLACE INTO `sys_role` VALUES (3, '普通管理员',NULL,  '全部', 2,'普通管理员级别为2，使用该角色新增用户时只能赋予比普通管理员级别低的角色', '2019-05-13 14:16:15', 1,  NULL, '2019-09-04 20:18:37', NULL);

-- ----------------------------
-- Records of sys_roles_menus_map
-- ----------------------------
REPLACE INTO `sys_roles_menus_map` VALUES (1, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (2, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (3, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (4, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (5, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (6, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (7, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (8, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (9, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (10, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (11, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (12, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (13, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (14, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (15, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (16, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (17, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (18, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (19, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (20, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (21, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (22, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (23, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (24, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (25, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (26, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (27, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (28, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (29, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (30, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (31, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (32, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (33, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (34, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (36, 1);
REPLACE INTO `sys_roles_menus_map` VALUES (37, 1);

-- ----------------------------
-- Records of roles_permissions_map
-- ----------------------------
REPLACE INTO `sys_roles_permissions_map` VALUES (1, 1);

-- ----------------------------
-- Records of user
-- ----------------------------
REPLACE INTO `sys_user` VALUES (1, 'admin', 'SuperLike', '$2a$10$tX2FSbajRt4xkUo4Sg4TF.V54m/QvoUFpiP9skWwzMqsTEKT44dbW', 0, '18888888888', '123456@qq.com', NULL, 0, 1, 1, b'1', '2019-07-27 19:32:05', '2019-08-29 09:16:54', '2018-08-23 09:11:56',0, NULL, '2020-01-19 13:22:43',NULL);

-- ----------------------------
-- Records of users_roles_map
-- ----------------------------
REPLACE INTO `sys_users_roles_map` VALUES (1, 1);


SET FOREIGN_KEY_CHECKS = 1;