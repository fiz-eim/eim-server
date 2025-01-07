/*
 Navicat Premium Data Transfer

 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Schema         : eim_base_db

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 22/08/2024 17:19:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

create database if not exists `eim_base_db` CHARACTER SET 'utf8mb4';

GRANT Alter, Alter Routine, Create, Create Routine, Create Temporary Tables, Create View, Delete, Drop, Event, Execute, Index, Insert, Lock Tables, References, Select, Show View, Trigger, Update ON `eim\_base\_db`.* TO `fizeim`@`%`;

use  `eim_base_db`;

-- ----------------------------
-- Table structure for alarm_notification_user
-- ----------------------------
DROP TABLE IF EXISTS `alarm_notification_user`;
CREATE TABLE `alarm_notification_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `alarm_type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报警类型id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_number` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of alarm_notification_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_auth_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_app`;
CREATE TABLE `sys_auth_app`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用名称',
  `login_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用登录地址',
  `secret_type` tinyint NULL DEFAULT NULL COMMENT '密钥类型：1-md5;2-rsa',
  `secret_pub` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公钥',
  `secret_private` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '私钥',
  `sso_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单点登录类型',
  `auth_protocol` tinyint NULL DEFAULT NULL COMMENT '认证协议：1- oauth2; 2-jwt;',
  `auth_mode` tinyint NULL DEFAULT NULL COMMENT '授权模式：1-code;2-password',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态：1-正常；2-停用',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `seq` int NULL DEFAULT NULL COMMENT '序号',
  `logout_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退出url',
  `logout_type` tinyint NULL DEFAULT NULL COMMENT '退出类型：1-无；2-前端；3-后端',
  `revision` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '乐观锁',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_auth_app
-- ----------------------------
INSERT INTO `sys_auth_app` VALUES (-1, 'industy-cloud', '基础服务平台', '127.0.0.1', 1, 'eeb95c26-4225-4846-9307-767db0354cd9\r\n', NULL, '1', 1, 2, 1, NULL, 1, NULL, 1, NULL, '1', NULL, NULL, '2022-08-22 11:40:44');
INSERT INTO `sys_auth_app` VALUES (36, 'eim-im', 'Eim-聊天服务', NULL, 1, '03de27c2-cee6-43a5-9489-b44efc5912cd', NULL, '1', 1, 2, 1, NULL, 12, NULL, NULL, NULL, '2', '2024-01-02 00:00:00', NULL, '2024-01-02 14:13:17');
INSERT INTO `sys_auth_app` VALUES (40, 'eim-task', 'EimTask', NULL, 1, 'c0c22d60-37b5-48ce-bbf3-f41f87c69b34', NULL, '1', 1, 2, 1, NULL, 10, NULL, NULL, NULL, '1', '2024-01-20 00:00:00', NULL, '2024-01-20 18:34:34');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-red', 'Y', '1', '2022-03-14 16:32:42', '1', '2022-05-23 09:40:12', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', '1', '2022-03-14 16:32:42', '1', '2023-05-29 15:36:07', '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', '1', '2022-03-14 16:32:42', NULL, NULL, '深色主题theme-dark，浅色主题theme-light');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '祖级列表',
  `dept_code` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '部门编码',
  `dept_name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `org_type` tinyint NULL DEFAULT NULL COMMENT '组织类型',
  `division_code` varchar(12) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '所属区划',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tanent_code` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '租户id',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 497 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', 'gszb', '公司总部', 0, '领导', '18653113130', 'test@test.com', '0', '0', 0, NULL, '1', '2022-03-14 16:32:41', '1', '2024-01-20 02:18:16', NULL, NULL);
INSERT INTO `sys_dept` VALUES (469, 495, '0,100,495', 'sofly-xz', '行政管理部', 0, '柳剑晨', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:20:56', '1', '2024-05-11 02:02:03', NULL, NULL);
INSERT INTO `sys_dept` VALUES (470, 495, '0,100,495', 'sofly-cw', '财务部', 0, '慕容雪', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:21:30', '1', '2024-05-11 02:01:47', NULL, NULL);
INSERT INTO `sys_dept` VALUES (471, 100, '0,100', 'sofly-sc', '售前市场部', 0, '白云飞', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:22:08', '', NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (472, 100, '0,100', 'sofly-lchz', '项目三部', 0, '秋水寒', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:22:58', '1', '2024-08-08 07:01:13', NULL, NULL);
INSERT INTO `sys_dept` VALUES (473, 100, '0,100', 'sofly-lrhz', '项目二部', 0, '风无痕', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:23:39', '1', '2024-08-08 07:02:19', NULL, NULL);
INSERT INTO `sys_dept` VALUES (475, 100, '0,100', 'sofly-jb', '项目一部', 0, '沈墨尘', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:25:55', '1', '2024-08-08 07:02:41', NULL, NULL);
INSERT INTO `sys_dept` VALUES (476, 100, '0,100', 'sofly-jf', '项目本部', 0, '顾盼生', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:28:00', '1', '2024-05-11 02:04:09', NULL, NULL);
INSERT INTO `sys_dept` VALUES (477, 100, '0,100', 'sofly-jls', '经理室', 0, '凌霜月', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:28:45', '', NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (478, 100, '0,100', 'sofly-co', '子公司集', 0, NULL, NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:31:17', '', NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (479, 478, '0,100,478', 'co-sofly', '创新维度子公司', 0, NULL, NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:31:40', '1', '2024-08-08 07:03:23', NULL, NULL);
INSERT INTO `sys_dept` VALUES (480, 478, '0,100,478', 'co-ds', '创想博远子公司', 0, NULL, NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:31:59', '1', '2024-08-08 07:03:07', NULL, NULL);
INSERT INTO `sys_dept` VALUES (481, 478, '0,100,478', 'co-xy', '智航云启子公司', 0, NULL, NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:32:26', '1', '2024-08-08 07:02:58', NULL, NULL);
INSERT INTO `sys_dept` VALUES (482, 475, '0,100,475', 'sofly-jb01', 'crm交付组', 0, NULL, NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:48:15', '', NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (483, 475, '0,100,475', 'sofly-jb02', 'OS交付一组', 0, NULL, NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:56:45', '', NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (484, 475, '0,100,475', 'sofly-jb03', 'OS交付二组', 0, NULL, NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:57:13', '', NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (485, 472, '0,100,472', 'sofly-lc01', '生产一组（采矿）', 0, '萧雨落', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:58:47', '1', '2024-08-08 07:01:59', NULL, NULL);
INSERT INTO `sys_dept` VALUES (486, 472, '0,100,472', 'sofly-lc02', '工业一组（园区）', 0, '段红尘', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:59:17', '1', '2024-08-08 07:02:06', NULL, NULL);
INSERT INTO `sys_dept` VALUES (487, 472, '0,100,472', 'sofly-lc03', '工业三组（装备）', 0, '燕归巢', NULL, NULL, '0', '0', 0, NULL, '1', '2024-01-20 02:59:57', '1', '2024-08-08 07:01:43', NULL, NULL);
INSERT INTO `sys_dept` VALUES (488, 473, '0,100,473', 'sjkfyy', '数据开发应用组', 0, '夏如歌', NULL, NULL, '0', '0', 0, NULL, '1', '2024-05-09 07:47:00', '1', '2024-05-10 08:16:09', NULL, NULL);
INSERT INTO `sys_dept` VALUES (489, 473, '0,100,473', 'zhbgz', '智慧办公组', 0, '欧阳烈', NULL, NULL, '0', '0', 0, NULL, '1', '2024-05-09 07:47:31', '1', '2024-05-09 07:47:47', NULL, NULL);
INSERT INTO `sys_dept` VALUES (490, 472, '0,100,472', 'sofly-lc04', '工业二组（行业）', 0, '苏倾城', NULL, NULL, '0', '0', 0, NULL, '1', '2024-05-09 09:21:31', '1', '2024-08-08 07:01:35', NULL, NULL);
INSERT INTO `sys_dept` VALUES (491, 100, '0,100', 'sofly-jszx', '技术中心', 0, '沈婉婷', NULL, NULL, '0', '0', 0, NULL, '21515', '2024-05-09 10:23:45', '', NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (493, 100, '0,100', 'sofly-pmo', '项目管理办公室', 0, '谢烟客', NULL, NULL, '0', '0', 0, NULL, '1', '2024-05-10 09:05:59', '', NULL, NULL, NULL);
INSERT INTO `sys_dept` VALUES (494, 495, '0,100,495', 'sofly-hr', '人力资源部', 0, '刘苏岳', NULL, NULL, '0', '0', 0, NULL, '1', '2024-05-10 10:02:48', '1', '2024-05-11 02:01:21', NULL, NULL);
INSERT INTO `sys_dept` VALUES (495, 100, '0,100', 'sofly-office', '职能部', 0, '温如言', NULL, NULL, '0', '0', 0, NULL, '1', '2024-05-11 02:01:10', '1', '2024-05-11 02:35:22', NULL, NULL);
INSERT INTO `sys_dept` VALUES (496, 100, '0,100', 'aQQQQ', 'CES', 0, NULL, NULL, NULL, '0', '2', 0, NULL, '1', '2024-08-16 06:42:17', '', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dept_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_post`;
CREATE TABLE `sys_dept_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门Id',
  `post_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位Id',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1606 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门岗位' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept_post
-- ----------------------------
INSERT INTO `sys_dept_post` VALUES (1, '100', '17', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (2, '101', '17', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (6, '228', '22', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (45, '100', '19', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (46, '223', '17', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (47, '100', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (78, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (79, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (80, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (81, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (82, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (83, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (84, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (85, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (86, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (87, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (88, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (89, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (90, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (91, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (92, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (93, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (94, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (95, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (96, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (97, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (98, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (99, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (100, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (101, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (102, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (103, NULL, '7', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (173, '101', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (174, '229', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (175, '103', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (176, '104', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (177, '105', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (178, '106', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (179, '107', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (180, '211', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (181, '212', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (183, '215', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (184, '220', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (185, '218', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (187, '108', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (188, '109', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (189, '216', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (190, '217', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (191, '219', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (192, '213', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (195, '223', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (196, '224', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (198, '227', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (199, '230', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (200, '228', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (201, '103', '3', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (202, '105', '3', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (203, '106', '3', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (204, '220', '3', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (205, '100', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (206, '101', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (207, '229', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (208, '103', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (209, '104', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (210, '105', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (211, '106', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (212, '107', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (213, '211', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (214, '212', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (215, '214', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (216, '215', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (219, '102', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (220, '108', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (221, '109', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (222, '216', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (223, '217', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (224, '219', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (225, '213', '1', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (226, '101', '4', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (227, '103', '4', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (228, '104', '4', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (229, '105', '4', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (230, '106', '4', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (232, '101', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (234, '215', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (236, '216', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (238, '223', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (239, '227', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (240, '101', '26', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (241, '229', '26', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (242, '103', '26', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (243, '104', '26', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (244, '105', '26', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (245, '106', '26', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (246, '107', '26', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (247, '211', '26', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (248, '212', '26', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (249, '105', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (250, '106', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (251, '101', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (252, '229', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (253, '103', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (254, '104', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (255, '107', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (256, '211', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (257, '212', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (258, '214', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (259, '215', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (260, '220', '27', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (261, '229', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (262, '103', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (263, '104', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (264, '105', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (265, '106', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (266, '107', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (267, '211', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (268, '212', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (269, '220', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (270, '218', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (271, '108', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (272, '109', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (273, '217', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (274, '219', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (275, '213', '28', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (276, '100', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (277, '101', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (278, '229', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (279, '103', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (280, '104', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (281, '105', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (282, '106', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (283, '107', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (284, '211', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (285, '212', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (286, '214', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (287, '215', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (288, '220', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (289, '218', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (290, '102', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (291, '108', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (292, '109', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (293, '216', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (294, '217', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (295, '219', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (296, '213', '29', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (297, '227', '30', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (298, '230', '30', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (299, '228', '30', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (300, '100', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (301, '214', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (302, '102', '2', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (305, '230', '22', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (310, '242', '31', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (311, '299', '31', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (312, '245', '32', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (313, '254', '32', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (314, '245', '32', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (315, '245', '32', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (316, '245', '33', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (317, '254', '33', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (318, '245', '32', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (319, '245', '32', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (449, '308', '36', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (450, '309', '37', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (451, '310', '37', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (452, '311', '37', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (453, '312', '37', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (454, '313', '37', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (480, '213', '38', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (481, '314', '39', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (483, '314', '39', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (488, '103', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (489, '101', '2', 'ry', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (490, '102', '2', 'ry', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (491, '214', '2', 'ry', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (492, '215', '2', 'ry', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (493, '216', '2', 'ry', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (494, '100', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (495, '101', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (496, '103', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (497, '104', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (498, '105', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (499, '106', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (500, '107', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (501, '211', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (502, '212', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (503, '242', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (504, '299', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (505, '218', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (506, '102', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (507, '108', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (508, '109', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (509, '214', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (510, '215', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (511, '220', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (512, '216', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (513, '217', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (514, '219', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (515, '213', '42', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (516, '100', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (517, '101', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (518, '103', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (519, '104', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (520, '105', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (521, '106', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (522, '107', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (523, '211', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (524, '212', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (525, '242', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (526, '299', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (528, '102', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (529, '108', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (530, '109', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (531, '214', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (532, '215', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (534, '216', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (535, '217', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (536, '219', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (537, '213', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (538, '227', '22', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (541, '100', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (542, '101', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (543, '103', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (544, '104', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (545, '105', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (546, '106', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (547, '107', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (548, '211', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (549, '212', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (550, '242', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (551, '299', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (552, '102', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (553, '108', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (554, '109', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (555, '214', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (556, '215', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (557, '216', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (558, '217', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (559, '219', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (560, '213', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (563, '101', '43', 'ry', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (564, '242', '43', 'ry', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (565, '102', '43', 'ry', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (566, '216', '43', 'ry', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (568, '100', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (569, '101', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (570, '104', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (571, '105', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (572, '106', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (573, '107', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (574, '211', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (575, '212', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (576, '242', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (577, '299', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (578, '102', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (579, '108', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (580, '109', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (581, '214', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (582, '215', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (583, '216', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (584, '217', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (585, '219', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (586, '213', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (587, '314', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (588, '315', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (589, '319', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (590, '320', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (591, '321', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (592, '331', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (593, '322', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (594, '330', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (595, '337', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (596, '338', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (597, '323', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (598, '345', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (599, '342', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (604, '410', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (605, '411', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (606, '412', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (607, '413', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (608, '414', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (609, '415', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (610, '416', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (611, '417', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (612, '418', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (613, '419', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (614, '420', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (615, '421', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (616, '422', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (617, '423', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (618, '424', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (619, '425', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (620, '426', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (621, '441', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (622, '442', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (623, '443', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (624, '444', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (625, '445', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (626, '446', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (627, '447', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (628, '448', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (629, '449', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (630, '450', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (631, '451', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (632, '452', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (633, '453', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (634, '454', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (635, '227', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (636, '308', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (637, '230', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (638, '228', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (639, '243', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (640, '245', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (641, '254', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (642, '246', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (643, '255', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (644, '256', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (645, '257', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (646, '258', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (647, '247', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (648, '259', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (649, '260', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (650, '261', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (651, '262', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (652, '248', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (653, '263', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (654, '264', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (655, '265', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (656, '266', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (657, '249', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (658, '267', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (659, '268', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (660, '269', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (661, '270', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (662, '250', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (663, '271', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (664, '272', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (665, '273', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (666, '274', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (667, '275', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (668, '276', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (669, '251', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (670, '277', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (671, '278', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (672, '279', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (673, '252', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (674, '280', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (675, '281', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (676, '282', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (677, '283', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (678, '253', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (679, '284', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (680, '285', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (681, '286', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (682, '287', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (683, '244', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (684, '289', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (685, '290', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (686, '291', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (687, '293', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (688, '295', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (689, '296', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (690, '297', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (691, '298', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (692, '309', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (693, '310', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (694, '461', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (695, '311', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (696, '312', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (697, '313', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (698, '100', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (699, '101', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (700, '242', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (701, '102', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (702, '214', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (703, '216', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (704, '101', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (705, '103', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (706, '104', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (707, '105', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (708, '106', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (709, '107', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (710, '211', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (711, '212', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (712, '242', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (713, '299', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (714, '102', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (715, '108', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (716, '109', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (717, '214', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (718, '215', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (719, '216', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (720, '217', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (721, '219', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (722, '213', '15', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (723, '100', '21', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (724, '101', '21', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (732, '242', '21', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (734, '102', '21', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (737, '214', '21', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (739, '216', '21', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (743, '100', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (744, '101', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (745, '103', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (746, '104', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (747, '105', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (748, '106', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (749, '107', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (750, '211', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (751, '212', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (752, '242', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (753, '299', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (754, '102', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (755, '108', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (756, '109', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (757, '214', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (758, '215', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (759, '216', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (760, '217', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (761, '219', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (762, '213', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (763, '100', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (764, '101', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (765, '242', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (766, '102', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (767, '214', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (768, '216', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (769, '100', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (770, '101', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (771, '242', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (772, '102', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (773, '214', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (774, '216', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (775, '314', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (776, '321', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (777, '322', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (778, '323', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (779, '410', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (780, '418', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (781, '441', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (782, '227', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (783, '243', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (784, '245', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (785, '246', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (786, '247', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (787, '248', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (788, '249', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (789, '250', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (790, '251', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (791, '252', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (792, '253', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (793, '244', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (794, '309', '41', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (795, '100', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (796, '101', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (797, '242', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (798, '102', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (799, '214', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (800, '216', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (801, '100', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (802, '101', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (803, '242', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (804, '102', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (805, '214', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (806, '216', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (807, '100', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (808, '101', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (809, '242', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (810, '102', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (811, '214', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (812, '216', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (813, '100', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (814, '101', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (815, '242', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (816, '102', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (817, '214', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (818, '216', '281', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (819, '100', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (820, '101', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (821, '242', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (822, '102', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (823, '214', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (824, '216', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (825, '100', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (826, '101', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (827, '242', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (828, '102', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (829, '214', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (830, '216', '43', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (831, '100', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (832, '101', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (833, '242', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (834, '102', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (835, '214', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (836, '216', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (837, '100', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (838, '101', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (839, '103', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (840, '104', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (841, '105', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (842, '106', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (843, '107', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (844, '211', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (845, '212', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (846, '242', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (847, '299', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (848, '102', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (849, '108', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (850, '109', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (851, '214', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (852, '215', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (853, '216', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (854, '217', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (855, '219', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (856, '213', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (857, '468', '283', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (858, '100', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (859, '101', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (860, '242', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (861, '102', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (862, '214', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (863, '216', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (864, '468', '45', 'admin', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1178, '100', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1179, '469', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1180, '478', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1181, '479', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1182, '480', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1183, '481', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1184, '477', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1185, '476', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1186, '475', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1187, '482', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1188, '483', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1189, '484', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1191, '473', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1192, '472', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1193, '485', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1194, '486', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1195, '487', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1196, '471', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1197, '470', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1198, '100', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1199, '469', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1200, '478', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1201, '479', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1202, '480', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1203, '481', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1204, '477', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1205, '476', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1206, '475', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1207, '482', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1208, '483', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1209, '484', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1211, '473', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1212, '472', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1213, '485', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1214, '486', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1215, '487', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1216, '471', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1217, '470', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1218, '100', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1219, '469', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1220, '478', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1221, '479', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1222, '480', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1223, '481', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1224, '477', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1225, '476', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1226, '475', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1227, '482', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1228, '483', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1229, '484', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1231, '473', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1232, '472', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1233, '485', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1234, '486', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1235, '487', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1236, '471', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1237, '470', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1238, '100', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1239, '469', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1240, '478', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1241, '479', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1242, '480', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1243, '481', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1244, '477', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1245, '476', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1246, '475', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1247, '482', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1248, '483', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1249, '484', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1251, '473', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1252, '472', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1253, '485', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1254, '486', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1255, '487', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1256, '471', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1257, '470', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1258, '100', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1259, '469', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1260, '478', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1266, '475', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1271, '473', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1272, '472', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1277, '470', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1379, '100', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1380, '478', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1381, '475', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1382, '472', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1383, '100', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1384, '478', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1385, '475', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1386, '472', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1387, '100', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1388, '478', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1389, '475', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1390, '472', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1391, '100', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1392, '478', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1393, '475', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1394, '472', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1395, '100', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1396, '478', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1397, '475', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1398, '472', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1399, '100', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1400, '478', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1401, '475', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1402, '472', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1403, '100', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1404, '478', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1405, '475', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1406, '472', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1407, '100', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1408, '478', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1409, '475', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1410, '472', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1411, '100', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1412, '478', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1413, '475', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1414, '472', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1415, '100', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1416, '478', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1417, '475', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1418, '472', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1419, '100', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1420, '478', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1421, '475', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1422, '472', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1423, '100', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1424, '478', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1425, '475', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1426, '472', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1427, '100', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1428, '478', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1429, '475', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1430, '472', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1431, '100', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1432, '478', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1433, '475', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1434, '472', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1435, '100', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1436, '478', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1437, '475', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1438, '472', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1488, '100', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1489, '495', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1490, '494', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1491, '491', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1492, '478', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1493, '475', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1494, '489', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1495, '488', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1496, '472', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1497, '490', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1498, '493', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1499, '100', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1500, '495', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1501, '478', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1502, '475', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1503, '473', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1504, '472', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1505, '100', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1506, '495', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1507, '494', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1508, '491', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1509, '478', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1510, '475', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1511, '489', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1512, '488', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1513, '472', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1514, '490', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1515, '493', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1516, '100', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1517, '495', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1518, '494', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1519, '491', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1520, '478', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1521, '475', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1522, '489', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1523, '488', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1524, '472', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1525, '490', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1526, '493', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1527, '478', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1528, '475', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1531, '495', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1532, '494', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1537, '491', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1538, '478', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1539, '475', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1540, '489', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1541, '488', '303', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1542, '100', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1543, '495', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1544, '478', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1545, '475', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1546, '473', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1547, '472', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1564, '495', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1565, '481', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1566, '480', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1567, '479', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1568, '482', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1569, '484', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1570, '483', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1571, '489', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1572, '488', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1573, '490', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1574, '487', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1575, '485', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1576, '486', '307', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1577, '100', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1578, '495', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1579, '478', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1580, '475', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1581, '473', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1582, '472', '306', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1583, '100', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1584, '495', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1585, '478', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1586, '475', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1587, '473', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1588, '472', '305', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1589, '100', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1590, '495', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1591, '478', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1592, '475', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1593, '473', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1594, '472', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1595, '100', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1596, '495', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1597, '478', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1598, '475', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1599, '473', '304', '1', NULL, NULL, NULL);
INSERT INTO `sys_dept_post` VALUES (1600, '472', '304', '1', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dept_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_user`;
CREATE TABLE `sys_dept_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6552 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept_user
-- ----------------------------
INSERT INTO `sys_dept_user` VALUES (80, 100, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (5895, 476, 21448, '1', '2024-01-22 10:32:20', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (5896, 479, 21448, '1', '2024-01-22 10:32:20', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6089, 477, 21453, '1', '2024-02-18 11:12:55', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6090, 479, 21453, '1', '2024-02-18 11:12:55', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6091, 100, 21453, '1', '2024-02-18 11:12:55', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6388, 479, 21450, '1', '2024-05-11 17:00:52', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6389, 491, 21450, '1', '2024-05-11 17:00:52', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6400, 473, 21456, '1', '2024-05-11 17:20:43', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6401, 480, 21456, '1', '2024-05-11 17:20:43', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6402, 488, 21456, '1', '2024-05-11 17:20:43', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6414, 469, 21454, '1', '2024-05-13 14:14:50', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6415, 479, 21454, '1', '2024-05-13 14:14:50', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6416, 495, 21454, '1', '2024-05-13 14:14:50', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6417, 470, 21455, '1', '2024-05-13 14:15:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6418, 479, 21455, '1', '2024-05-13 14:15:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6419, 495, 21455, '1', '2024-05-13 14:15:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6427, 490, 21445, '1', '2024-05-13 14:25:53', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6428, 480, 21445, '1', '2024-05-13 14:25:53', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6429, 479, 21452, '1', '2024-05-13 14:26:01', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6430, 490, 21452, '1', '2024-05-13 14:26:01', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6447, 472, 21439, '1', '2024-05-13 14:30:23', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6448, 479, 21439, '1', '2024-05-13 14:30:23', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6449, 485, 21439, '1', '2024-05-13 14:30:23', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6450, 472, 21446, '1', '2024-05-13 14:30:39', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6451, 485, 21446, '1', '2024-05-13 14:30:39', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6452, 480, 21446, '1', '2024-05-13 14:30:39', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6453, 485, 21449, '1', '2024-05-13 14:30:43', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6454, 480, 21449, '1', '2024-05-13 14:30:43', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6462, 480, 21451, '1', '2024-05-13 14:33:25', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6463, 473, 21451, '1', '2024-05-13 14:33:25', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6464, 488, 21451, '1', '2024-05-13 14:33:25', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6465, 473, 21444, '1', '2024-05-13 14:33:37', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6466, 479, 21444, '1', '2024-05-13 14:33:37', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6467, 488, 21444, '1', '2024-05-13 14:33:37', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6489, 479, 21447, '1', '2024-05-13 14:55:33', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6490, 491, 21447, '1', '2024-05-13 14:55:33', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6491, 473, 21440, '1', '2024-05-16 08:49:11', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6492, 488, 21440, '1', '2024-05-16 08:49:11', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6493, 480, 21440, '1', '2024-05-16 08:49:11', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6525, 479, 21438, '1', '2024-07-31 15:30:22', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6526, 491, 21438, '1', '2024-07-31 15:30:22', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6531, 473, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6532, 479, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6533, 488, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6534, 494, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6535, 481, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6536, 482, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6537, 484, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6538, 483, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6539, 487, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6540, 486, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6541, 471, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6542, 493, 21441, '1', '2024-08-08 16:18:05', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6543, 476, 21442, '1', '2024-08-08 16:18:58', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6544, 479, 21442, '1', '2024-08-08 16:18:58', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6545, 491, 21442, '1', '2024-08-08 16:18:58', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6546, 489, 21442, '1', '2024-08-08 16:18:58', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6547, 478, 21442, '1', '2024-08-08 16:18:58', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6548, 490, 21443, '1', '2024-08-08 16:20:12', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6549, 480, 21443, '1', '2024-08-08 16:20:12', NULL, NULL);
INSERT INTO `sys_dept_user` VALUES (6550, 475, 21443, '1', '2024-08-08 16:20:12', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '备注',
  `inher_dict` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2173 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', 'default', 'Y', '0', '1', '2022-03-14 16:32:42', '1', '2022-06-29 17:29:42', '性别男', NULL);
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', '1', '2022-03-14 16:32:42', '1', '2022-07-29 10:15:33', '性别女', NULL);
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', '1', '2022-03-14 16:32:42', '1', '2022-07-01 11:09:13', '性别未知', NULL);
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '显示菜单', NULL);
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '隐藏菜单', NULL);
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', 'color=red', 'default', 'Y', '0', '1', '2022-03-14 16:32:42', '1', '2022-07-29 09:59:50', '正常状态', NULL);
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'default', 'N', '0', '1', '2022-03-14 16:32:42', '1', '2022-07-29 09:59:53', '停用状态', NULL);
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', '1', '2022-03-14 16:32:42', '1', '2022-03-25 11:21:52', '正常状态', NULL);
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', '1', '2022-03-14 16:32:42', '1', '2022-03-25 11:21:54', '停用状态', NULL);
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', '1', '2022-03-14 16:32:42', '1', '2022-03-28 07:45:07', '默认分组', NULL);
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '系统分组', NULL);
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '系统默认是', NULL);
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '系统默认否', NULL);
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '通知', NULL);
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '1', '1', '2022-03-14 16:32:42', '1', '2022-11-10 10:35:47', '公告', NULL);
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '正常状态', NULL);
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '关闭状态', NULL);
INSERT INTO `sys_dict_data` VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '新增操作', NULL);
INSERT INTO `sys_dict_data` VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '修改操作', NULL);
INSERT INTO `sys_dict_data` VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '删除操作', NULL);
INSERT INTO `sys_dict_data` VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '授权操作', NULL);
INSERT INTO `sys_dict_data` VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '导出操作', NULL);
INSERT INTO `sys_dict_data` VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '导入操作', NULL);
INSERT INTO `sys_dict_data` VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '强退操作', NULL);
INSERT INTO `sys_dict_data` VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '生成操作', NULL);
INSERT INTO `sys_dict_data` VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '清空操作', NULL);
INSERT INTO `sys_dict_data` VALUES (27, 1, '成功', '0', 'sys_common_status', '', 'default', 'N', '0', '1', '2022-03-14 16:32:42', '1', '2022-07-29 15:38:24', '正常状态', NULL);
INSERT INTO `sys_dict_data` VALUES (28, 2, '失败', '1', 'sys_common_status', '', 'default', 'N', '0', '1', '2022-03-14 16:32:42', '1', '2022-07-29 15:38:27', '停用状态', NULL);
INSERT INTO `sys_dict_data` VALUES (116, 1, '出勤休假模型', '1', 'modelType', NULL, 'default', 'N', '0', '1', '2022-05-18 13:55:01', '1', '2024-01-22 08:19:16', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (117, 2, '人事模型', '2', 'modelType', NULL, 'default', 'N', '0', '1', '2022-05-19 15:59:47', '1', '2024-01-22 08:19:27', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (118, 3, '财务模型', '3', 'modelType', NULL, 'default', 'N', '0', '1', '2022-05-19 15:59:59', '1', '2024-01-22 08:19:37', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (119, 0, 'MD5', '1', 'secretType', NULL, 'default', 'N', '0', '1', '2022-05-19 20:08:59', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (120, 1, 'RSA', '2', 'secretType', NULL, 'default', 'N', '0', '1', '2022-05-19 20:09:11', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (121, 0, 'Oauth2', '1', 'authProtocol', NULL, 'default', 'N', '0', '1', '2022-05-19 20:15:01', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (122, 1, 'JWT', '2', 'authProtocol', NULL, 'default', 'N', '0', '1', '2022-05-19 20:15:18', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (123, 0, '授权码', '1', 'authMode', NULL, 'default', 'N', '0', '1', '2022-05-19 20:16:01', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (124, 1, '密码', '2', 'authMode', NULL, 'default', 'N', '0', '1', '2022-05-19 20:16:09', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (125, 0, 'Oauth2', '1', 'ssoType', NULL, 'default', 'N', '0', '1', '2022-05-19 20:16:30', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (126, 0, '正常', '1', 'appStatus', NULL, 'default', 'N', '0', '1', '2022-05-19 20:27:35', '1', '2022-07-29 09:53:37', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (127, 1, '停用', '2', 'appStatus', NULL, 'default', 'N', '0', '1', '2022-05-19 20:27:42', '1', '2022-07-29 09:53:40', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (128, 0, '测试部门1', 't1', 'department', NULL, 'default', 'N', '0', '1', '2022-05-23 16:39:21', '1', '2022-05-26 11:00:21', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (161, 0, '正常', '0', 'status', 'uglay', 'default', 'N', '0', '1', '2022-07-11 09:44:16', '1', '2022-07-29 09:56:14', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (162, 1, '停用', '1', 'status', NULL, 'default', 'N', '0', '1', '2022-07-11 09:44:30', '1', '2022-07-29 09:56:18', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (172, 3, '任务', '3', 'sys_notice_type', NULL, 'info', 'N', '1', '1', '2022-07-18 13:52:54', '1', '2022-11-10 10:35:50', '任务', NULL);
INSERT INTO `sys_dict_data` VALUES (173, 4, '提醒', '4', 'sys_notice_type', NULL, 'primary', 'N', '1', '1', '2022-07-18 13:53:08', '1', '2023-02-09 09:51:07', '提醒', NULL);
INSERT INTO `sys_dict_data` VALUES (174, 5, '警告', '5', 'sys_notice_type', NULL, 'danger', 'N', '1', '1', '2022-07-18 13:53:24', '1', '2023-02-09 09:37:34', '警告', NULL);
INSERT INTO `sys_dict_data` VALUES (393, 0, '汉族', 'Han', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:09:51', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (394, 0, '壮', 'Zhuang', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:10:10', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (395, 0, '满', 'Manchu', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:10:24', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (396, 0, '回', 'Hui', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:10:38', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (397, 0, '苗', 'Miao', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:10:57', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (398, 0, '维吾尔', 'Uyghur', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:11:14', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (399, 0, '土家', 'Tujia', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:11:30', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (400, 0, '彝', 'Yi', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:11:47', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (401, 0, '蒙古', 'Mongolian', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:12:05', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (402, 0, '藏', 'Tibetan', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:12:19', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (403, 0, '布依', 'Buyei', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:12:36', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (404, 0, '侗', 'Dong', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:13:04', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (405, 0, '瑶', 'Yao', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:13:21', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (406, 0, '朝鲜', 'Korean', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:13:38', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (407, 0, '白', 'Bai', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:13:57', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (408, 0, '哈尼', 'Hani', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:14:11', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (409, 0, '哈萨克', 'Kazakh', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:14:30', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (410, 0, '黎', 'Li', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:14:45', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (411, 0, '傣', 'Dai', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:15:00', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (412, 0, '畲', 'She', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:15:15', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (413, 0, '傈僳', 'Lisu', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:15:42', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (414, 0, '仡佬', 'Gelao', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:16:01', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (415, 0, '东乡', 'Dongxiang', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:16:15', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (416, 0, '高山', 'Gaoshan', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:16:31', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (417, 0, '拉祜族', 'Lahu', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:16:45', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (418, 0, '水', 'Shui', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:16:57', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (419, 0, '佤', 'Va', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:17:09', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (420, 0, '纳西', 'Nakhi', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:17:21', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (421, 0, '羌', 'Qiang', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:17:33', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (422, 0, '土', 'Monguor', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:17:46', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (423, 0, '仫佬', 'Mulao', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:18:03', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (424, 0, '锡伯', 'Xibe', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:18:15', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (425, 0, '柯尔克孜', 'Kyrgyz', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:18:28', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (426, 0, '达斡尔', 'Daur', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:18:40', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (427, 0, '景颇', 'Jingpo', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:18:51', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (428, 0, '毛南', 'Maonan', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:19:02', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (429, 0, '撒拉', 'Salar', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:19:13', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (430, 0, '布朗', 'Blang', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:19:23', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (431, 0, '塔吉克', 'Tajik', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:19:42', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (432, 0, '阿昌', 'Achang', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:19:54', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (433, 0, '普米', 'Pumi', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:20:06', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (434, 0, '鄂温克', 'Evenk', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:20:16', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (435, 0, '怒', 'Nu', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:20:25', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (436, 0, '京', 'Kinh', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:20:35', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (437, 0, '基诺', 'Jino', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:20:45', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (438, 0, '德昂', 'Deang', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:20:57', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (439, 0, '保安', 'Bonan', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:21:07', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (440, 0, '俄罗斯', 'Russian', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:21:16', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (441, 0, '裕固', 'Yughur', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:21:26', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (442, 0, '乌孜别克', 'Uzbek', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:21:36', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (443, 0, '门巴', 'Monpa', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:21:45', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (444, 0, '鄂伦春', 'Oroqen', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:21:56', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (445, 0, '独龙', 'Derung', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:22:05', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (446, 0, '塔塔尔', 'Tatar', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:22:14', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (447, 0, '赫哲', 'Nanai', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:22:23', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (448, 0, '珞巴', 'Lhoba', 'nationality', NULL, 'default', 'N', '0', '1', '2022-08-19 14:22:36', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (449, 0, '身份证', 'IDCard', 'idType', NULL, 'default', 'N', '0', '1', '2022-08-19 14:25:58', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (450, 0, '护照', 'passport', 'idType', NULL, 'default', 'N', '0', '1', '2022-08-19 14:26:44', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (455, 0, '本科', 'undergraduate', 'education', NULL, 'default', 'N', '0', '1', '2022-08-19 14:35:42', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (456, 0, '大专', 'juniorCollege', 'education', NULL, 'default', 'N', '0', '1', '2022-08-19 14:36:17', '1', '2022-08-19 14:37:07', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (1444, 1, '立项', '1', 'projectStatus', NULL, 'default', 'N', '0', '1', '2023-08-11 14:19:47', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1445, 2, '研发', '2', 'projectStatus', NULL, 'default', 'N', '0', '1', '2023-08-11 14:19:54', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1446, 3, '维保', '3', 'projectStatus', NULL, 'default', 'N', '0', '1', '2023-08-11 14:20:34', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1447, 4, '归档', '4', 'projectStatus', NULL, 'default', 'N', '0', '1', '2023-08-11 14:20:48', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1448, 5, '公司管理', '5', 'projectStatus', NULL, 'default', 'N', '0', '1', '2023-08-11 14:20:58', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1449, 1, '自研', '1', 'projectCategory', NULL, 'default', 'N', '0', '1', '2023-08-11 15:13:00', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1450, 2, '大数据', '2', 'projectCategory', NULL, 'default', 'N', '0', '1', '2023-08-11 15:13:09', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1451, 3, '鲁能', '3', 'projectCategory', NULL, 'default', 'N', '0', '1', '2023-08-11 15:13:20', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1452, 4, '重工', '4', 'projectCategory', NULL, 'default', 'N', '0', '1', '2023-08-11 15:13:31', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1453, 5, '京博', '5', 'projectCategory', NULL, 'default', 'N', '0', '1', '2023-08-11 15:13:43', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1454, 1, '商城', '商城', 'projectTag', NULL, 'default', 'N', '0', '1', '2023-08-11 15:17:57', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1455, 2, '大数据', '大数据', 'projectTag', NULL, 'default', 'N', '0', '1', '2023-08-11 15:18:08', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1456, 3, '工业互联网', '工业互联网', 'projectTag', NULL, 'default', 'N', '0', '1', '2023-08-11 15:18:18', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1457, 1, '自研', '1', 'projectType', NULL, 'default', 'N', '0', '1', '2023-08-11 15:19:22', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1458, 2, '合作', '2', 'projectType', NULL, 'default', 'N', '0', '1', '2023-08-11 15:19:32', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1459, 3, '外包', '3', 'projectType', NULL, 'default', 'N', '0', '1', '2023-08-11 15:19:40', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1460, 9, '内部管理', '9', 'projectType', NULL, 'default', 'N', '0', '1', '2023-08-11 15:19:50', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1469, 1, '模块', '1', 'specificationType', NULL, 'default', 'N', '0', '1', '2023-08-11 17:55:15', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1470, 2, '功能项', '2', 'specificationType', NULL, 'default', 'N', '0', '1', '2023-08-11 17:55:23', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1471, 1, '原需求', '1', 'specificationNature', NULL, 'default', 'N', '0', '1', '2023-08-11 17:55:51', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1472, 2, '新增', '2', 'specificationNature', NULL, 'default', 'N', '0', '1', '2023-08-11 17:56:00', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1473, 3, '变更', '3', 'specificationNature', NULL, 'default', 'N', '0', '1', '2023-08-11 17:56:09', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1474, 1, '新增', '1', 'specificationStatus', NULL, 'default', 'N', '0', '1', '2023-08-11 17:56:51', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1475, 2, '计划', '2', 'specificationStatus', NULL, 'default', 'N', '0', '1', '2023-08-11 17:57:00', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1476, 3, '开发', '3', 'specificationStatus', NULL, 'default', 'N', '0', '1', '2023-08-11 17:57:07', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1477, 4, '修改', '4', 'specificationStatus', NULL, 'default', 'N', '0', '1', '2023-08-11 17:57:19', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1478, 1, '正常', '1', 'role_status', '', '', 'N', '0', NULL, '2021-11-09 10:03:06', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1479, 1, '正常', '1', 'user_status', '', '', 'N', '0', NULL, '2021-02-01 02:42:58', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1480, 1, '锁定', '2', 'user_status', '', '', 'N', '0', NULL, '2021-02-01 02:43:05', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1481, 1, '菜单', '1', 'res_type', '', '', 'N', '0', NULL, '2021-02-01 05:05:52', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1482, 1, '接口', '2', 'res_type', '', '', 'N', '0', NULL, '2021-02-01 05:06:01', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1483, 1, '按钮', '3', 'res_type', '', '', 'N', '0', NULL, '2021-02-01 05:06:09', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1484, 1, '报表', '4', 'res_type', '', '', 'N', '0', NULL, '2021-02-01 05:06:17', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1485, 1, '仪表板', '5', 'res_type', '', '', 'N', '0', NULL, '2021-02-01 05:06:23', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1486, 1, '普通字典', '1', 'dict_type', '', '', 'N', '0', NULL, '2021-02-01 05:48:37', NULL, '2021-02-01 05:53:16', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1487, 1, '树形字典', '2', 'dict_type', '', '', 'N', '0', NULL, '2021-02-01 05:48:45', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1488, 1, '全局角色', '1', 'role_type', '', '', 'N', '0', NULL, '2021-02-01 05:55:21', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1489, 1, '部门角色', '2', 'role_type', '', '', 'N', '0', NULL, '2021-02-01 05:55:27', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1490, 1, '正式合同', '1', 'contractType', '', '', 'N', '0', NULL, '2021-08-31 15:07:15', NULL, '2021-10-25 16:50:46', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1491, 1, '副本合同', '2', 'contractType', '', '', 'N', '0', NULL, '2021-08-31 15:07:26', NULL, '2021-10-25 16:51:03', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1492, 1, '补充协议', '3', 'contractType', '', '', 'N', '0', NULL, '2021-08-31 15:07:38', NULL, '2021-10-25 16:51:06', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1493, 1, '第三方正本合同', '4', 'contractType', '', '', 'N', '0', NULL, '2021-08-31 15:07:47', NULL, '2021-10-25 16:51:10', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1494, 1, '第三方副本合同', '5', 'contractType', '', '', 'N', '0', NULL, '2021-08-31 15:07:56', NULL, '2021-10-25 16:51:12', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1495, 1, '第三方补充协议', '6', 'contractType', '', '', 'N', '0', NULL, '2021-08-31 15:08:06', NULL, '2021-10-25 16:51:16', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1496, 1, '人民币', 'CNY', 'currencyType', '', '', 'N', '0', NULL, '2021-08-31 15:10:08', NULL, '2021-08-31 15:33:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1497, 1, '美元', 'USD', 'currencyType', '', '', 'N', '0', NULL, '2021-08-31 15:10:28', NULL, '2021-08-31 15:33:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1498, 1, '类型1', 'type1', 'elContractType', '', '', 'N', '0', NULL, '2021-08-31 15:16:21', NULL, '2021-08-31 15:33:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1499, 1, '类型2', 'type2', 'elContractType', '', '', 'N', '0', NULL, '2021-08-31 15:16:31', NULL, '2021-08-31 15:33:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1500, 1, '预付款', 'pre', 'receivableType', '', '', 'N', '0', NULL, '2021-08-31 15:20:45', NULL, '2021-08-31 15:33:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1501, 1, '初验款', 'preli', 'receivableType', '', '', 'N', '0', NULL, '2021-08-31 15:21:54', NULL, '2021-08-31 15:33:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1502, 1, '终验款', 'final', 'receivableType', '', '', 'N', '0', NULL, '2021-08-31 15:22:17', NULL, '2021-08-31 15:33:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1503, 1, '质保金', 'warr', 'receivableType', '', '', 'N', '0', NULL, '2021-08-31 15:22:41', NULL, '2021-08-31 15:33:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1504, 1, '银行', 'bank', 'payMode', '', '', 'N', '0', NULL, '2021-09-07 10:00:56', NULL, '2021-09-09 10:39:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1505, 1, '现金', 'cash', 'payMode', '', '', 'N', '0', NULL, '2021-09-07 10:01:27', NULL, '2021-09-09 10:39:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1506, 1, '其他', 'other', 'payMode', '', '', 'N', '0', NULL, '2021-09-07 10:01:37', NULL, '2021-09-09 10:39:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1507, 1, '一级', '1', 'classLevel', '', '', 'N', '0', NULL, '2021-10-08 11:32:27', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1508, 1, '二级', '2', 'classLevel', '', '', 'N', '0', NULL, '2021-10-08 11:32:39', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1509, 1, '草稿', '1', 'planStatus', '', '', 'N', '0', NULL, '2021-10-13 10:38:05', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1510, 1, '新建', '1', 'pWeekPlanStatus', '', '', 'N', '0', NULL, '2021-10-13 10:44:02', NULL, '2021-10-14 11:08:59', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1511, 1, '待审核', '2', 'pWeekPlanStatus', '', '', 'N', '0', NULL, '2021-10-13 10:44:12', NULL, '2021-11-15 16:21:07', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1512, 1, '是', '1', 'criticalPath', '', '', 'N', '0', NULL, '2021-10-13 10:47:06', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1513, 1, '否', '2', 'criticalPath', '', '', 'N', '0', NULL, '2021-10-13 10:47:18', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1514, 1, '是', '1', 'commonTrueFalse', '', '', 'N', '0', NULL, '2021-10-13 10:50:05', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1515, 1, '否', '2', 'commonTrueFalse', '', '', 'N', '0', NULL, '2021-10-13 10:50:12', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1516, 1, '整体计划', '1', 'planType', '', '', 'N', '0', NULL, '2021-10-13 10:50:25', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1517, 1, '阶段计划', '2', 'planType', '', '', 'N', '0', NULL, '2021-10-13 10:50:33', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1518, 1, '部门周计划', '1', 'projectPlanType', '', '', 'N', '0', NULL, '2021-10-13 17:36:44', NULL, '2021-10-20 09:47:35', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1519, 1, '项目周计划', '2', 'projectPlanType', '', '', 'N', '0', NULL, '2021-10-13 17:36:59', NULL, '2021-10-20 09:47:26', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1520, 1, '个人周计划', '3', 'projectPlanType', '', '', 'N', '0', NULL, '2021-10-13 17:37:12', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1521, 1, '待签收', '3', 'pWeekPlanStatus', '', '', 'N', '0', NULL, '2021-10-14 11:09:23', NULL, '2021-11-15 16:21:26', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1522, 1, '待办', '4', 'pWeekPlanStatus', '', '', 'N', '0', NULL, '2021-10-14 11:09:34', NULL, '2021-11-15 16:21:46', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1523, 1, '待办', '4', 'taskStatus', '', '', 'N', '0', NULL, '2021-10-14 14:05:59', NULL, '2021-11-15 16:22:50', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1524, 1, '在办', '5', 'taskStatus', '', '', 'N', '0', NULL, '2021-10-14 14:06:09', NULL, '2021-11-15 16:22:47', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1525, 1, '转测', '6', 'taskStatus', '', '', 'N', '0', NULL, '2021-10-14 14:06:20', NULL, '2021-11-15 16:22:43', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1526, 1, '完成', '7', 'taskStatus', '', '', 'N', '0', NULL, '2021-10-14 14:06:29', NULL, '2021-11-15 16:22:39', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1527, 1, 'Bug', '1', 'taskReasonType', '', '', 'N', '0', NULL, '2021-10-14 15:55:44', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1528, 1, '质量', '2', 'taskReasonType', '', '', 'N', '0', NULL, '2021-10-14 15:55:53', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1529, 1, '新建', '1', 'weekPlanStatus', '', '', 'N', '0', NULL, '2021-10-14 16:11:19', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1530, 1, '待审核', '2', 'weekPlanStatus', '', '', 'N', '0', NULL, '2021-10-14 16:11:38', NULL, '2021-11-15 17:56:08', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1531, 1, '周计划', '3', 'planType', '', '', 'N', '0', NULL, '2021-10-15 09:27:11', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1532, 1, '设计', '1', 'heTypeOptions', '', '', 'N', '0', NULL, '2021-10-15 17:00:53', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1533, 1, '开发', '2', 'heTypeOptions', '', '', 'N', '0', NULL, '2021-10-15 17:01:04', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1534, 1, '测试', '3', 'heTypeOptions', '', '', 'N', '0', NULL, '2021-10-15 17:01:20', NULL, '2021-10-15 17:01:35', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1535, 1, '方案', '4', 'heTypeOptions', '', '', 'N', '0', NULL, '2021-10-15 17:01:54', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1536, 1, '文档', '5', 'heTypeOptions', '', '', 'N', '0', NULL, '2021-10-15 17:02:03', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1537, 1, '前端', '1', 'funcTypeOptions', '', '', 'N', '0', NULL, '2021-10-15 17:18:58', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1538, 1, '后端', '2', 'funcTypeOptions', '', '', 'N', '0', NULL, '2021-10-15 17:19:07', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1539, 1, '个人周计划', '4', 'planType', '', '', 'N', '0', NULL, '2021-10-19 09:58:06', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1540, 1, '待办理', '2', 'planStatus', '', '', 'N', '0', NULL, '2021-10-19 15:52:25', NULL, '2021-11-01 14:42:41', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1541, 1, '办理中', '3', 'planStatus', '', '', 'N', '0', NULL, '2021-10-19 15:52:33', NULL, '2021-11-01 14:42:45', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1542, 1, '已完成', '4', 'planStatus', '', '', 'N', '0', NULL, '2021-10-19 15:52:39', NULL, '2021-11-01 14:42:50', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1543, 1, '待签收', '3', 'weekPlanStatus', '', '', 'N', '0', NULL, '2021-10-19 15:53:08', NULL, '2021-11-15 17:56:15', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1544, 1, '待办理', '4', 'weekPlanStatus', '', '', 'N', '0', NULL, '2021-10-19 15:53:18', NULL, '2021-11-15 17:56:26', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1545, 1, '在办', '5', 'weekPlanStatus', '', '', 'N', '0', NULL, '2021-10-19 15:53:34', NULL, '2021-11-15 17:56:52', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1546, 1, '在办', '5', 'pWeekPlanStatus', '', '', 'N', '0', NULL, '2021-10-20 10:24:14', NULL, '2021-11-15 16:21:55', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1547, 1, '驳回', '2', 'approveResult', '', '', 'N', '0', NULL, '2021-10-20 13:56:15', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1548, 1, '通过', '1', 'approveResult', '', '', 'N', '0', NULL, '2021-10-20 13:56:34', NULL, '2021-10-20 13:56:56', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1549, 1, '事前', '1', 'srChangeType', '', '', 'N', '0', NULL, '2021-10-21 10:31:04', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1550, 1, '事中', '2', 'srChangeType', '', '', 'N', '0', NULL, '2021-10-21 10:31:18', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1551, 1, '事后', '3', 'srChangeType', '', '', 'N', '0', NULL, '2021-10-21 10:31:25', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1552, 1, '待准备', '1', 'deliverableStatus', '', '', 'N', '0', NULL, '2021-10-22 11:46:49', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1553, 1, '准备中', '2', 'deliverableStatus', '', '', 'N', '0', NULL, '2021-10-22 14:40:58', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1554, 1, '待交付', '3', 'deliverableStatus', '', '', 'N', '0', NULL, '2021-10-22 14:41:16', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1555, 1, '已交付', '4', 'deliverableStatus', '', '', 'N', '0', NULL, '2021-10-22 14:41:36', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1556, 1, '正常', '1', 'projectResourceStatus', '', '', 'N', '0', NULL, '2021-10-22 15:15:57', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1557, 1, '停用', '2', 'projectResourceStatus', '', '', 'N', '0', NULL, '2021-10-22 15:16:19', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1558, 1, '正常', '1', 'joinMeetingStatus', '', '', 'N', '0', NULL, '2021-10-25 14:50:28', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1559, 1, '迟到', '2', 'joinMeetingStatus', '', '', 'N', '0', NULL, '2021-10-25 14:50:37', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1560, 1, '请假', '3', 'joinMeetingStatus', '', '', 'N', '0', NULL, '2021-10-25 14:50:45', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1561, 1, '3', '1', 'contractRate', '', '', 'N', '0', NULL, '2021-10-25 16:18:38', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1562, 1, '6', '2', 'contractRate', '', '', 'N', '0', NULL, '2021-10-25 16:18:45', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1563, 1, '13', '3', 'contractRate', '', '', 'N', '0', NULL, '2021-10-25 16:18:54', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1564, 1, '起草', '1', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-25 16:19:33', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1565, 1, '邮寄', '2', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-25 16:19:40', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1566, 1, '签署', '3', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-25 16:19:51', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1567, 1, '执行', '4', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-25 16:20:06', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1568, 1, '需求文档', 'requirement', 'docType', '', '', 'N', '0', NULL, '2021-10-25 17:33:20', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1569, 1, '方案文档', 'scheme', 'docType', '', '', 'N', '0', NULL, '2021-10-25 17:35:04', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1570, 1, '设计文档', 'design', 'docType', '', '', 'N', '0', NULL, '2021-10-25 17:35:43', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1571, 1, '交付文档', 'deliverable', 'docType', '', '', 'N', '0', NULL, '2021-10-25 17:36:17', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1572, 1, '项目总结', 'summarize', 'docType', '', '', 'N', '0', NULL, '2021-10-25 17:37:02', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1573, 1, '首付款已开票', '5', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-25 18:10:09', NULL, '2021-10-25 18:16:31', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1574, 1, '首付款', '1', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-25 18:12:23', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1575, 1, '第二期', '2', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-25 18:12:37', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1576, 1, '第三期', '3', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-25 18:12:44', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1577, 1, '首付款已回款', '6', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-25 18:16:50', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1578, 1, '第二期已开票', '7', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-25 18:17:10', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1579, 1, '第二期已回款', '8', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-25 18:17:20', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1580, 1, '第三期已开票', '9', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-25 18:17:32', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1581, 1, '第三期已回款', '10', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-25 18:17:41', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1582, 1, '新增', '1', 'riskStatus', '', '', 'N', '0', NULL, '2021-10-26 09:47:58', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1583, 1, '处理中', '2', 'riskStatus', '', '', 'N', '0', NULL, '2021-10-26 09:48:13', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1584, 1, '消失', '3', 'riskStatus', '', '', 'N', '0', NULL, '2021-10-26 09:48:34', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1585, 1, '不处理', '4', 'riskStatus', '', '', 'N', '0', NULL, '2021-10-26 09:48:44', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1586, 1, '已发生', '5', 'riskStatus', '', '', 'N', '0', NULL, '2021-10-26 09:49:02', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1587, 1, '已处理', '6', 'riskStatus', '', '', 'N', '0', NULL, '2021-10-26 09:49:17', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1588, 1, '估算', '001', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:51:05', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1589, 1, '客户', '002', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:51:26', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1590, 1, '工作条件', '003', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:52:02', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1591, 1, '进度', '004', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:52:31', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1592, 1, '技术', '005', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:53:09', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1593, 1, '管理', '006', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:53:21', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1594, 1, '质量', '007', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:53:48', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1595, 1, '成本', '008', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:54:10', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1596, 1, '资源', '009', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:54:40', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1597, 1, '需求不明确', '001001', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:56:07', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1598, 1, '估算方法', '001002', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:56:51', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1599, 1, '估算结果', '001003', 'riskType', '', '', 'N', '0', NULL, '2021-10-26 10:57:12', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1600, 1, '一级风险', '5', 'riskSeverity', '', '', 'N', '0', NULL, '2021-10-26 14:52:58', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1601, 1, '二级风险', '4', 'riskSeverity', '', '', 'N', '0', NULL, '2021-10-26 14:53:19', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1602, 1, '三级风险', '3', 'riskSeverity', '', '', 'N', '0', NULL, '2021-10-26 14:53:30', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1603, 1, '四级风险', '2', 'riskSeverity', '', '', 'N', '0', NULL, '2021-10-26 14:53:51', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1604, 1, '五级风险', '1', 'riskSeverity', '', '', 'N', '0', NULL, '2021-10-26 14:54:03', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1605, 1, '基本确定', '5', 'riskProbability', '', '', 'N', '0', NULL, '2021-10-26 14:55:27', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1606, 1, '很可能', '4', 'riskProbability', '', '', 'N', '0', NULL, '2021-10-26 14:55:38', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1607, 1, '有可能', '3', 'riskProbability', '', '', 'N', '0', NULL, '2021-10-26 14:55:57', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1608, 1, '不太可能', '2', 'riskProbability', '', '', 'N', '0', NULL, '2021-10-26 14:56:10', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1609, 1, '极小', '1', 'riskProbability', '', '', 'N', '0', NULL, '2021-10-26 14:56:35', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1610, 1, '第四期已开票', '11', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:07:52', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1611, 1, '第四期已回款', '12', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:08:07', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1612, 1, '第五期已开票', '13', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:08:23', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1613, 1, '第五期已回款', '14', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:08:42', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1614, 1, '第六期已开票', '15', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:09:07', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1615, 1, '第六期已回款', '16', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:09:23', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1616, 1, '第七期已开票', '17', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:09:48', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1617, 1, '第七期已回款', '18', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:09:58', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1618, 1, '第八期已开票', '19', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:10:44', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1619, 1, '第八期已回款', '20', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:11:04', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1620, 1, '第九期已开票', '21', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:11:23', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1621, 1, '第九期已回款', '22', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:11:35', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1622, 1, '第十期已开票', '23', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:11:52', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1623, 1, '第十期已回款', '24', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:12:20', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1624, 1, '尾款', '0', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-27 16:13:45', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1625, 1, '尾款已开票', '25', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:15:41', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1626, 1, '尾款已回款', '26', 'contractStatus', '', '', 'N', '0', NULL, '2021-10-27 16:15:51', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1627, 1, '第四期', '4', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-27 16:16:48', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1628, 1, '第五期', '5', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-27 16:16:55', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1629, 1, '第六期', '6', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-27 16:17:01', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1630, 1, '第七期', '7', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-27 16:17:07', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1631, 1, '第八期', '8', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-27 16:17:15', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1632, 1, '第九期', '9', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-27 16:17:23', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1633, 1, '第十期', '10', 'conReturnStatus', '', '', 'N', '0', NULL, '2021-10-27 16:17:30', NULL, '2021-11-24 09:30:49', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1634, 1, '男', '1', 'gender', '', '', 'N', '0', NULL, '2021-10-27 17:59:58', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1635, 1, '女', '2', 'gender', '', '', 'N', '0', NULL, '2021-10-27 18:00:06', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1636, 1, '未知', '3', 'gender', '', '', 'N', '0', NULL, '2021-10-27 18:00:22', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1637, 1, '员工', '1', 'personnelType', '', '', 'N', '0', NULL, '2021-10-27 18:00:35', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1638, 1, '兼职', '2', 'personnelType', '', '', 'N', '0', NULL, '2021-10-27 18:00:46', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1639, 1, '合作方', '3', 'personnelType', '', '', 'N', '0', NULL, '2021-10-27 18:00:55', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1640, 1, '在职', '1', 'personnelStatus', '', '', 'N', '0', NULL, '2021-10-27 18:01:07', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1641, 1, '离职', '2', 'personnelStatus', '', '', 'N', '0', NULL, '2021-10-27 18:01:13', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1642, 1, '身份证', '1', 'credentialsType', '', '', 'N', '0', NULL, '2021-10-27 18:01:24', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1643, 1, '护照', '2', 'credentialsType', '', '', 'N', '0', NULL, '2021-10-27 18:01:30', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1644, 1, '项目中', '1', 'memberStatus', '', '', 'N', '0', NULL, '2021-10-28 13:51:23', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1645, 1, '离开项目', '2', 'memberStatus', '', '', 'N', '0', NULL, '2021-10-28 13:51:36', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1646, 1, '差旅费', '1', 'eaType', '', '', 'N', '0', NULL, '2021-10-29 16:43:11', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1647, 1, '停用', '2', 'role_status', '', '', 'N', '0', NULL, '2021-11-09 10:03:14', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1648, 1, '正常', '1', 'general_status', '', '', 'N', '0', NULL, '2021-11-09 10:26:27', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1649, 1, '停用', '2', 'general_status', '', '', 'N', '0', NULL, '2021-11-09 10:26:34', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1650, 1, '转测', '6', 'pWeekPlanStatus', '', '', 'N', '0', NULL, '2021-11-11 15:52:21', NULL, '2021-11-15 16:22:04', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1651, 1, '完成', '7', 'pWeekPlanStatus', '', '', 'N', '0', NULL, '2021-11-15 17:10:27', NULL, '2021-11-15 17:09:55', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1652, 1, '挂起', '8', 'pWeekPlanStatus', '', '', 'N', '0', NULL, '2021-11-15 17:10:44', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1653, 1, '挂起', '8', 'taskStatus', '', '', 'N', '0', NULL, '2021-11-15 17:11:06', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1654, 1, '转测', '6', 'weekPlanStatus', '', '', 'N', '0', NULL, '2021-11-15 17:57:05', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1655, 1, '完成', '7', 'weekPlanStatus', '', '', 'N', '0', NULL, '2021-11-15 17:57:14', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1656, 1, '测试人员', 'tester', 'trunTestRoles', '', '', 'N', '0', NULL, '2021-11-18 10:44:53', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1657, 1, '归档', '99', 'contractStatus', '', '', 'N', '0', NULL, '2021-11-19 15:10:51', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1658, 1, '临时性周计划', '5', 'planType', '', '', 'N', '0', NULL, '2021-11-22 19:16:21', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1659, 1, 'AgentId', '1415924406', 'dingtalkConfig', '', '', 'N', '0', NULL, '2021-12-23 10:38:52', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1660, 1, 'AppKey', 'dingnwpey8lzuac796do', 'dingtalkConfig', '', '', 'N', '0', NULL, '2021-12-23 10:39:11', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1661, 1, 'AppSecret', 'FofmCT0-kq9kkioUCVCSMjxuc0IeHlzxluRLeXf4qphXMn3JlYQBQqGNI7nrApAf', 'dingtalkConfig', '', '', 'N', '0', NULL, '2021-12-23 10:39:30', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1662, 1, '企业', '1', 'clueType', '', '', 'N', '0', NULL, '2021-12-23 11:58:48', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1663, 2, '个人', '2', 'clueType', '', '', 'N', '0', NULL, '2021-12-23 11:58:55', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1664, 3, '其他', '9', 'clueType', '', '', 'N', '0', NULL, '2021-12-23 11:59:02', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1665, 1, '正常', '1', 'cluestatus', '', '', 'N', '0', NULL, '2021-12-23 11:59:13', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1666, 1, '暂停合作', '2', 'cluestatus', '', '', 'N', '0', NULL, '2021-12-23 11:59:30', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1667, 1, 'true', 'updateDingTalkUser', 'dailyScheduledTasks', '', '', 'N', '0', NULL, '2021-12-29 14:17:13', NULL, '2021-12-29 16:17:54', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1668, 1, '上班', 'OnDuty', 'checkType', '', '', 'N', '0', NULL, '2021-12-30 11:24:02', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1669, 1, '下班', 'OffDuty', 'checkType', '', '', 'N', '0', NULL, '2021-12-30 11:24:11', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1670, 1, '正常', 'Normal', 'timeResult', '', '', 'N', '0', NULL, '2021-12-30 11:24:59', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1671, 1, '早退', 'Early', 'timeResult', '', '', 'N', '0', NULL, '2021-12-30 11:25:17', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1672, 1, '迟到', 'Late', 'timeResult', '', '', 'N', '0', NULL, '2021-12-30 11:25:29', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1673, 1, '严重迟到', 'SeriousLate', 'timeResult', '', '', 'N', '0', NULL, '2021-12-30 11:29:48', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1674, 1, '旷工', 'Absenteeism', 'timeResult', '', '', 'N', '0', NULL, '2021-12-30 11:29:58', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1675, 1, '未打卡', 'NotSigned', 'timeResult', '', '', 'N', '0', NULL, '2021-12-30 11:30:11', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1676, 1, '范围内', 'Normal', 'locationResult', '', '', 'N', '0', NULL, '2021-12-30 11:40:43', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1677, 1, '范围外', 'Outside', 'locationResult', '', '', 'N', '0', NULL, '2021-12-30 11:40:54', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1678, 1, '未打卡', 'NotSigned', 'locationResult', '', '', 'N', '0', NULL, '2021-12-30 11:41:04', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1679, 1, '招标', '1', 'bidType', '', '', 'N', '0', NULL, '2022-01-04 17:08:38', NULL, '2022-01-04 17:09:08', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1680, 1, '议标', '2', 'bidType', '', '', 'N', '0', NULL, '2022-01-04 17:09:01', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1681, 1, '发现商机', '1', 'saleStage', '', '', 'N', '0', NULL, '2022-01-04 17:13:44', NULL, '2022-01-06 14:57:04', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1682, 1, '外部连接', '6', 'res_type', '', '', 'N', '0', NULL, '2022-01-05 16:00:29', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1683, 1, '售前交流', '2', 'saleStage', '', '', 'N', '0', NULL, '2022-01-06 14:55:33', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1684, 1, '商务谈判', '3', 'saleStage', '', '', 'N', '0', NULL, '2022-01-06 14:55:44', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1685, 1, '合同签订', '4', 'saleStage', '', '', 'N', '0', NULL, '2022-01-06 14:55:59', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1686, 1, '产品交付', '5', 'saleStage', '', '', 'N', '0', NULL, '2022-01-06 14:56:18', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1687, 1, '售后服务', '6', 'saleStage', '', '', 'N', '0', NULL, '2022-01-06 14:56:37', NULL, '2022-01-06 14:56:49', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1688, 1, '研发', '1', 'memberRole', '', '', 'N', '0', NULL, '2022-01-07 10:22:38', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1689, 1, '测试', '2', 'memberRole', '', '', 'N', '0', NULL, '2022-01-07 10:22:47', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1690, 1, '研发组长', '3', 'memberRole', '', '', 'N', '0', NULL, '2022-01-07 10:23:04', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1691, 1, '测试组长', '4', 'memberRole', '', '', 'N', '0', NULL, '2022-01-07 10:23:17', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1692, 1, '设计', '5', 'memberRole', '', '', 'N', '0', NULL, '2022-01-07 10:23:27', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1693, 1, '项目经理', '9', 'memberRole', '', '', 'N', '0', NULL, '2022-01-07 10:23:43', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1694, 1, '未延期', '1', 'returnDelay', '', '', 'N', '0', NULL, '2022-01-10 10:28:44', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1695, 1, '已延期', '2', 'returnDelay', '', '', 'N', '0', NULL, '2022-01-10 10:28:52', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1696, 1, '服务', '1', 'serviceType', '', '', 'N', '0', NULL, '2022-01-11 18:15:48', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1697, 1, '演示系统', '2', 'serviceType', '', '', 'N', '0', NULL, '2022-01-11 18:16:03', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1698, 1, '内网', '1', 'serviceLocation', '', '', 'N', '0', NULL, '2022-01-11 18:16:18', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1699, 1, '公网', '2', 'serviceLocation', '', '', 'N', '0', NULL, '2022-01-11 18:16:58', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1700, 1, '正常', '1', 'serviceState', '', '', 'N', '0', NULL, '2022-01-11 18:17:21', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1701, 1, '停用', '2', 'serviceState', '', '', 'N', '0', NULL, '2022-01-11 18:17:31', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1702, 1, '删除', '3', 'serviceState', '', '', 'N', '0', NULL, '2022-01-11 18:17:44', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1703, 1, '正常', '1', 'stakeholderState', '', '', 'N', '0', NULL, '2022-01-25 09:44:19', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1704, 1, '停用', '2', 'stakeholderState', '', '', 'N', '0', NULL, '2022-01-25 09:44:27', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1705, 1, '交通费', '2', 'eaType', '', '', 'N', '0', NULL, '2022-03-21 13:47:12', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1706, 1, '团建费', '3', 'eaType', '', '', 'N', '0', NULL, '2022-03-21 13:47:26', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1707, 1, '商务费', '4', 'eaType', '', '', 'N', '0', NULL, '2022-03-21 13:47:43', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1708, 1, '其它', '5', 'eaType', '', '', 'N', '0', NULL, '2022-03-21 13:48:34', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1709, 1, '9:00', '上午', 'holiday_time_config', '', '', 'N', '0', NULL, '2022-03-29 16:07:10', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1710, 1, '18:00', '下午', 'holiday_time_config', '', '', 'N', '0', NULL, '2022-03-29 16:07:31', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1711, 1, '跟踪中', '1', 'clue_status', '', '', 'N', '0', NULL, '2022-04-18 10:07:33', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1712, 1, '签约线索', '2', 'clue_status', '', '', 'N', '0', NULL, '2022-04-18 10:07:59', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1713, 1, '归档线索', '3', 'clue_status', '', '', 'N', '0', NULL, '2022-04-18 10:08:16', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1714, 1, '考评类', '1', 'evaluationFlag', '', '', 'N', '0', NULL, '2022-06-10 21:10:49', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1715, 1, '非考评类', '2', 'evaluationFlag', '', '', 'N', '0', NULL, '2022-06-10 21:11:00', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1716, 1, '正常', '1', 'evaluateStatus', '', '', 'N', '0', NULL, '2022-07-06 09:59:46', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1717, 1, '异常', '2', 'evaluateStatus', '', '', 'N', '0', NULL, '2022-07-06 09:59:54', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1718, 1, 'A', '1', 'attendanceStatus', '', '', 'N', '0', NULL, '2022-07-06 11:28:18', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1719, 1, 'B', '2', 'attendanceStatus', '', '', 'N', '0', NULL, '2022-07-06 11:28:25', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1720, 1, 'C', '3', 'attendanceStatus', '', '', 'N', '0', NULL, '2022-07-06 11:28:31', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1721, 1, 'beginMonth', '月初', 'wrorkItem', '', '', 'N', '0', NULL, '2022-07-13 15:13:33', NULL, '2022-07-13 15:15:23', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1722, 1, 'middleMonth', '月中', 'wrorkItem', '', '', 'N', '0', NULL, '2022-07-13 15:14:18', NULL, '2022-07-13 15:15:31', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1723, 1, 'endMonth', '月末', 'wrorkItem', '', '', 'N', '0', NULL, '2022-07-13 15:14:54', NULL, '2022-07-13 15:15:39', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1724, 1, '待处理', '2', 'releaseStatus', '', '', 'N', '0', NULL, '2022-08-15 16:33:57', NULL, '2022-08-17 10:01:57', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1725, 1, '处理中', '3', 'releaseStatus', '', '', 'N', '0', NULL, '2022-08-15 16:34:06', NULL, '2022-08-17 10:01:55', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1726, 1, '项目结束', '1', 'releaseReason', '', '', 'N', '0', NULL, '2022-08-16 11:02:23', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1727, 1, '中途释放', '2', 'releaseReason', '', '', 'N', '0', NULL, '2022-08-16 11:02:36', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1728, 1, '其他', '3', 'releaseReason', '', '', 'N', '0', NULL, '2022-08-16 11:02:41', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1729, 1, '待提交', '1', 'releaseStatus', '', '', 'N', '0', NULL, '2022-08-17 09:47:04', NULL, '2022-08-17 10:01:49', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1730, 1, '已处理', '4', 'releaseStatus', '', '', 'N', '0', NULL, '2022-08-17 09:47:11', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1731, 1, '项目中', '1', 'personnelFree', '', '', 'N', '0', NULL, '2022-08-19 18:48:18', NULL, '2022-08-26 18:11:34', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1732, 1, '空闲', '2', 'personnelFree', '', '', 'N', '0', NULL, '2022-08-19 18:48:33', NULL, '2022-08-26 18:11:45', NULL, 1);
INSERT INTO `sys_dict_data` VALUES (1733, 0, '单聊', '1', 'channel_type', NULL, 'default', 'N', '0', NULL, '2023-11-27 11:48:05', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1734, 0, '部门群', '2', 'channel_type', NULL, 'default', 'N', '0', NULL, '2023-11-27 11:48:17', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1735, 0, '项目群', '3', 'channel_type', NULL, 'default', 'N', '0', NULL, '2023-11-27 11:48:26', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1736, 0, '任务群', '4', 'channel_type', NULL, 'default', 'N', '0', NULL, '2023-11-27 11:48:39', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1737, 0, '讨论组', '5', 'channel_type', NULL, 'default', 'N', '0', NULL, '2023-11-27 11:48:49', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1738, 0, '正常任务', '1', 'work_type', NULL, 'default', 'N', '0', '1', '2023-12-06 18:23:07', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1739, 0, '问题修改', '2', 'work_type', NULL, 'default', 'N', '0', '1', '2023-12-06 18:23:21', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1740, 0, 'bug处理', '3', 'work_type', NULL, 'default', 'N', '0', '1', '2023-12-06 18:23:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1741, 0, '其它', '4', 'work_type', NULL, 'default', 'N', '0', '1', '2023-12-06 18:23:50', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1742, 0, '正常', '1', 'work_status', NULL, 'default', 'N', '0', '1', '2023-12-07 14:46:20', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1743, 0, '补录', '2', 'work_status', NULL, 'default', 'N', '0', '1', '2023-12-07 14:46:27', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1744, 0, '紧急重要', '1', 'task_priority', NULL, 'default', 'N', '0', '1', '2023-12-07 14:57:17', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1745, 0, '重要', '2', 'task_priority', NULL, 'default', 'N', '0', '1', '2023-12-07 14:57:24', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1746, 0, '一般', '3', 'task_priority', NULL, 'default', 'N', '0', '1', '2023-12-07 14:57:31', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1747, 0, '低', '4', 'task_priority', NULL, 'default', 'N', '0', '1', '2023-12-07 14:57:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1748, 0, '项目任务', '1', 'task_type', NULL, 'default', 'N', '0', '1', '2023-12-07 16:05:20', '1', '2024-01-20 07:47:28', '项', 0);
INSERT INTO `sys_dict_data` VALUES (1749, 0, '部门任务', '2', 'task_type', NULL, 'default', 'N', '0', '1', '2023-12-07 16:05:38', '1', '2024-01-20 07:50:24', '部', 0);
INSERT INTO `sys_dict_data` VALUES (1750, 0, '临时任务', '3', 'task_type', NULL, 'default', 'N', '0', '1', '2023-12-07 16:05:51', '1', '2024-01-20 07:50:36', '临', 0);
INSERT INTO `sys_dict_data` VALUES (1751, 0, '常规任务', '4', 'task_type', NULL, 'default', 'N', '0', '1', '2023-12-07 16:06:05', '1', '2024-01-20 07:50:43', '常', 0);
INSERT INTO `sys_dict_data` VALUES (1752, 0, '待处理', '1', 'task_status', NULL, 'default', 'N', '0', '1', '2023-12-07 16:16:07', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1753, 0, '进行中', '2', 'task_status', NULL, 'default', 'N', '0', '1', '2023-12-07 16:16:19', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1754, 0, '已提交', '3', 'task_status', NULL, 'default', 'N', '0', '1', '2023-12-07 16:16:27', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1755, 0, '完成', '4', 'task_status', NULL, 'default', 'N', '0', '1', '2023-12-07 16:16:35', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1756, 0, '软件项目', '1', 'task_project_type', NULL, 'default', 'N', '0', '1', '2023-12-09 17:17:53', '1', '2024-01-20 08:05:16', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1757, 0, '部门任务', '2', 'task_project_type', NULL, 'default', 'N', '0', '1', '2023-12-09 17:18:06', '1', '2024-01-22 11:18:15', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1758, 0, '财务任务', '3', 'task_project_type', NULL, 'default', 'N', '0', '1', '2023-12-09 17:18:14', '1', '2024-01-20 08:05:30', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1759, 1, 'bug问题', '1', 'task_question_type', NULL, 'default', 'N', '0', '1', '2023-12-11 17:29:49', '1', '2023-12-12 15:07:39', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1760, 2, '功能缺失', '2', 'task_question_type', NULL, 'default', 'N', '0', '1', '2023-12-11 17:30:12', '1', '2023-12-12 15:14:12', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1761, 3, '功能优化', '3', 'task_question_type', NULL, 'default', 'N', '0', '1', '2023-12-11 17:30:31', '1', '2023-12-12 15:14:16', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1764, 0, '新建', '1', 'task_question_status', NULL, 'default', 'N', '0', '1', '2023-12-11 17:58:14', '1', '2023-12-12 15:17:45', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1765, 2, '已提交', '3', 'task_question_status', NULL, 'default', 'N', '0', '1', '2023-12-11 17:58:24', '1', '2023-12-12 15:18:31', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1766, 3, '已解决', '4', 'task_question_status', NULL, 'default', 'N', '0', '1', '2023-12-11 17:58:35', '1', '2023-12-12 15:18:44', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1767, 0, '一般', '1', 'task_priority_deprecated', NULL, 'default', 'N', '0', '1', '2023-12-11 18:05:19', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1768, 0, '普通', '2', 'task_priority_deprecated', NULL, 'default', 'N', '0', '1', '2023-12-11 18:05:25', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1769, 0, '重要', '3', 'task_priority_deprecated', NULL, 'default', 'N', '0', '1', '2023-12-11 18:05:35', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1770, 0, '紧急', '4', 'task_priority_deprecated', NULL, 'default', 'N', '0', '1', '2023-12-11 18:05:44', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1772, 1, '严重问题', '2', 'task_question_leave', NULL, 'default', 'N', '0', '1', '2023-12-11 18:09:41', '1', '2023-12-12 15:15:50', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1773, 0, '致命问题', '1', 'task_question_leave', NULL, 'default', 'N', '0', '1', '2023-12-11 18:10:04', '1', '2023-12-12 15:15:34', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1774, 2, '一般问题', '3', 'task_question_leave', NULL, 'default', 'N', '0', '1', '2023-12-11 18:10:18', '1', '2023-12-12 15:15:54', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1775, 3, '提示问题', '4', 'task_question_leave', NULL, 'default', 'N', '0', '1', '2023-12-11 18:10:32', '1', '2023-12-12 15:15:59', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1776, 1, '处理中', '2', 'task_question_status', NULL, 'default', 'N', '0', '1', '2023-12-12 15:18:09', '1', '2023-12-12 15:18:17', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1777, 4, '挂起', '5', 'task_question_status', NULL, 'default', 'N', '0', '1', '2023-12-12 15:19:06', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1778, 5, '关闭', '6', 'task_question_status', NULL, 'default', 'N', '0', '1', '2023-12-12 15:19:17', '1', '2023-12-24 15:28:16', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1780, 0, '执行人', '1', 'task_user_type', NULL, 'default', 'N', '0', '1', '2023-12-12 15:20:59', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1781, 1, '跟踪人', '2', 'task_user_type', NULL, 'default', 'N', '0', '1', '2023-12-12 15:21:35', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1782, 2, '测试人', '3', 'task_user_type', NULL, 'default', 'N', '0', '1', '2023-12-12 15:21:44', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1783, 0, '普通', '1', 'em_grad_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:32:31', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1784, 0, '211', '2', 'em_grad_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:32:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1785, 0, '985', '3', 'em_grad_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:32:47', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1786, 0, '双一流', '4', 'em_grad_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:32:59', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1787, 0, '专科院校', '5', 'em_grad_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:33:16', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1788, 0, '民办', '6', 'em_grad_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:33:23', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1789, 0, '本科', '1', 'em_grad_type_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:34:42', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1790, 0, '专科', '2', 'em_grad_type_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:34:51', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1791, 0, '硕士', '3', 'em_grad_type_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:35:01', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1792, 0, '博士', '4', 'em_grad_type_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:35:08', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1793, 0, '全职', '1', 'em_type_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:36:31', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1794, 0, '兼职', '2', 'em_type_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:36:36', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1795, 0, '挂靠', '3', 'em_type_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:36:44', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1796, 0, '代缴', '4', 'em_type_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:36:50', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1797, 0, '使用', '5', 'em_type_status', NULL, 'default', 'N', '0', '1', '2023-12-12 17:36:56', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1798, 1, '用户消息', '1', 'im_message_type', NULL, 'default', 'N', '0', '1', '2023-12-21 09:57:10', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1799, 2, '系统消息', '2', 'im_message_type', NULL, 'default', 'N', '0', '1', '2023-12-21 09:57:23', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1800, 3, '机器人消息', '3', 'im_message_type', NULL, 'default', 'N', '0', '1', '2023-12-21 09:57:33', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1801, 1, '单聊', '1', 'im_channel_type', NULL, 'default', 'N', '0', '1', '2023-12-21 09:58:43', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1802, 2, '部门', '2', 'im_channel_type', NULL, 'default', 'N', '0', '1', '2023-12-21 10:00:31', '1', '2023-12-24 10:08:47', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1803, 3, '项目', '3', 'im_channel_type', NULL, 'default', 'N', '0', '1', '2023-12-21 10:00:42', '1', '2023-12-24 10:08:51', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1804, 4, '任务', '4', 'im_channel_type', NULL, 'default', 'N', '0', '1', '2023-12-21 10:00:57', '1', '2023-12-24 10:08:54', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1805, 5, '讨论组', '5', 'im_channel_type', NULL, 'default', 'N', '0', '1', '2023-12-21 10:01:18', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1806, 6, '机器人', '6', 'im_channel_type', NULL, 'default', 'N', '0', '1', '2023-12-21 10:01:29', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1807, 0, '1', '08:30:00', 'work_time', NULL, 'default', 'N', '0', '1', '2023-12-22 15:20:17', '1', '2024-06-03 01:21:14', '上午开始时间', 0);
INSERT INTO `sys_dict_data` VALUES (1808, 0, '2', '12:00:00', 'work_time', NULL, 'default', 'N', '0', '1', '2023-12-22 15:20:42', '', NULL, '上午结束时间', 0);
INSERT INTO `sys_dict_data` VALUES (1809, 0, '3', '13:30:00', 'work_time', NULL, 'default', 'N', '0', '1', '2023-12-22 15:21:01', '', NULL, '下午开始时间', 0);
INSERT INTO `sys_dict_data` VALUES (1810, 0, '4', '18:00:00', 'work_time', NULL, 'default', 'N', '0', '1', '2023-12-22 15:21:24', '1', '2024-06-03 01:21:22', '下午结束时间', 0);
INSERT INTO `sys_dict_data` VALUES (1811, 0, '5', '8', 'work_time', NULL, 'default', 'N', '0', '1', '2023-12-22 15:45:11', '', NULL, '工作时长', 0);
INSERT INTO `sys_dict_data` VALUES (1812, 1, '离职', '0', 'job_status', NULL, 'default', 'N', '0', '1', '2023-12-23 16:36:34', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1813, 2, '在职', '1', 'job_status', NULL, 'default', 'N', '0', '1', '2023-12-23 16:36:45', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1815, 0, '未提交', '1', 'process_status', NULL, 'default', 'N', '0', '1', '2023-12-28 15:42:56', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1816, 0, '运行中', '2', 'process_status', NULL, 'default', 'N', '0', '1', '2023-12-28 15:43:02', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1817, 0, '已挂起', '3', 'process_status', NULL, 'default', 'N', '0', '1', '2023-12-28 15:43:08', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1818, 0, '已结束', '4', 'process_status', NULL, 'default', 'N', '0', '1', '2023-12-28 15:43:18', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1819, 0, '已删除', '5', 'process_status', NULL, 'default', 'N', '0', '1', '2023-12-28 15:43:24', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1820, 5, '兼职任务', '5', 'task_type', NULL, 'default', 'N', '0', '1', '2024-01-20 07:51:06', '', NULL, '兼', 0);
INSERT INTO `sys_dict_data` VALUES (1821, 0, '项目经理', '1', 'task_edit_mode', NULL, 'default', 'N', '0', '1', '2024-01-20 07:58:06', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1822, 1, '项目经理/阶段负责人', '2', 'task_edit_mode', NULL, 'default', 'N', '0', '1', '2024-01-20 07:58:16', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1823, 2, '全项目成员', '3', 'task_edit_mode', NULL, 'default', 'N', '0', '1', '2024-01-20 07:58:29', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1824, 0, '研发交付', '1', 'task_project_class', NULL, 'default', 'N', '0', '1', '2024-01-20 08:01:22', '1', '2024-01-22 11:21:34', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1825, 1, '人力合作', '2', 'task_project_class', NULL, 'default', 'N', '0', '1', '2024-01-20 08:01:33', '1', '2024-01-22 11:21:46', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1826, 2, '项目外包', '3', 'task_project_class', NULL, 'default', 'N', '0', '1', '2024-01-20 08:01:48', '1', '2024-01-22 11:21:55', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1827, 1, '工作日加班', '1', 'overWork', NULL, 'default', 'N', '0', '1', '2024-01-22 03:17:46', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1828, 2, '周末加班', '2', 'overWork', NULL, 'default', 'N', '0', '1', '2024-01-22 03:17:56', '1', '2024-01-22 03:18:17', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1829, 3, '节假日加班', '3', 'overWork', NULL, 'default', 'N', '0', '1', '2024-01-22 03:18:11', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1830, 5, '售前任务', '4', 'task_project_type', NULL, 'default', 'N', '0', '1', '2024-01-22 11:19:51', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1831, 6, '培训任务', '6', 'task_project_type', NULL, 'default', 'N', '0', '1', '2024-01-22 11:20:16', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1832, 7, '销售任务', '7', 'task_project_type', NULL, 'primary', 'N', '0', '1', '2024-01-22 11:20:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1833, 4, '产品研发', '4', 'task_project_class', NULL, 'success', 'N', '0', '1', '2024-01-22 11:22:16', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1834, 5, '订单类交付', '5', 'task_project_class', NULL, 'default', 'N', '0', '1', '2024-01-22 11:22:55', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1835, 6, '敞口框架协议', '6', 'task_project_class', NULL, 'default', 'N', '0', '1', '2024-01-22 11:23:15', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1836, 1, '使用技术不当', '1', 'task_info_delay_reason', NULL, 'default', 'N', '0', '1', '2024-03-06 09:51:35', '', NULL, '个人原因', 0);
INSERT INTO `sys_dict_data` VALUES (1837, 2, '项目要求沟通不当', '2', 'task_info_delay_reason', NULL, 'default', 'N', '0', '1', '2024-03-06 09:51:50', '', NULL, '个人原因', 0);
INSERT INTO `sys_dict_data` VALUES (1838, 3, '成员请假', '3', 'task_info_delay_reason', NULL, 'default', 'N', '0', '1', '2024-03-06 09:52:02', '', NULL, '个人原因', 0);
INSERT INTO `sys_dict_data` VALUES (1839, 4, '项目范围变更', '4', 'task_info_delay_reason', NULL, 'default', 'N', '0', '1', '2024-03-06 09:52:16', '', NULL, '公司/项目经理原因', 0);
INSERT INTO `sys_dict_data` VALUES (1840, 5, '规划不足', '5', 'task_info_delay_reason', NULL, 'default', 'N', '0', '1', '2024-03-06 09:52:29', '', NULL, '公司/项目经理原因', 0);
INSERT INTO `sys_dict_data` VALUES (1841, 6, '客户方面的项目变更', '6', 'task_info_delay_reason', NULL, 'default', 'N', '0', '1', '2024-03-06 09:52:48', '', NULL, '客户原因', 0);
INSERT INTO `sys_dict_data` VALUES (1842, 7, '其他', '7', 'task_info_delay_reason', NULL, 'default', 'N', '0', '1', '2024-03-06 09:52:57', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1843, 1, '公式', '0', 'rule_type', NULL, 'default', 'N', '0', '1', '2024-03-29 02:52:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1844, 2, '手动', '1', 'rule_type', NULL, 'default', 'N', '0', '1', '2024-03-29 02:52:51', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1845, 3, '脚本', '2', 'rule_type', NULL, 'default', 'N', '0', '1', '2024-03-29 02:53:04', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1846, 4, '枚举', '3', 'rule_type', NULL, 'default', 'N', '0', '1', '2024-03-29 02:53:15', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1847, 0, '1', '3', 'assessment_time', NULL, 'default', 'N', '0', '1', '2024-05-06 06:58:32', '1', '2024-05-06 07:07:03', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1848, 0, '2', '7', 'assessment_time', NULL, 'default', 'N', '0', '1', '2024-05-06 06:58:39', '1', '2024-05-06 07:07:01', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1849, 0, 'IP', 'https://oapi.dingtalk.com', 'ding_talk_config', NULL, 'default', 'N', '0', '1', '2024-05-07 03:06:47', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1850, 1, 'AgentId', '3095653162', 'ding_talk_config', NULL, 'default', 'N', '0', '1', '2024-05-07 03:07:01', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1851, 2, 'AppKey', 'dingnt1zqwcaeg3aqyga', 'ding_talk_config', NULL, 'default', 'N', '0', '1', '2024-05-07 03:07:15', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1852, 3, 'AppSecret', '3XqqzYuGff8Re6EzpYhpMcKsbIdQHq_uYvpfDUUmKSegi0qaWqJvuvOa695o0q2B', 'ding_talk_config', NULL, 'default', 'N', '0', '1', '2024-05-07 03:07:29', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1853, 0, '离职', 'resignation', 'form_type', NULL, 'default', 'N', '0', '1', '2024-05-16 00:44:53', '1', '2024-05-16 03:17:13', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1854, 1, '报销', 'finance', 'form_type', NULL, 'default', 'N', '0', '1', '2024-05-16 00:45:37', '1', '2024-05-16 03:17:17', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1855, 2, '加班', 'over_work', 'form_type', NULL, 'default', 'N', '0', '1', '2024-05-16 00:45:57', '1', '2024-05-16 03:17:20', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1856, 3, '转正', 'regularization', 'form_type', NULL, 'default', 'N', '0', '1', '2024-05-16 00:46:32', '1', '2024-05-16 03:17:23', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1857, 4, '补卡', 'mend_clock', 'form_type', NULL, 'default', 'N', '0', '1', '2024-05-16 00:47:02', '1', '2024-05-16 03:17:26', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1858, 5, '请假', 'test_level', 'form_type', NULL, 'default', 'N', '0', '1', '2024-05-16 00:47:31', '1', '2024-05-16 03:17:30', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1859, 6, '入职', 'onboarding', 'form_type', NULL, 'default', 'N', '0', '1', '2024-05-16 00:47:58', '1', '2024-05-16 03:17:33', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1860, 1, '飞智协作平台', 'FizTask', 'app_list', NULL, 'default', 'N', '0', '1', '2024-07-24 00:43:12', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1861, 0, '安卓', '1', 'app_platform', NULL, 'default', 'N', '0', '1', '2024-07-24 01:21:19', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1862, 1, 'IOS', '2', 'app_platform', NULL, 'default', 'N', '0', '1', '2024-07-24 01:21:29', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1863, 2, 'Windows', '3', 'app_platform', NULL, 'default', 'N', '0', '1', '2024-07-24 01:21:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1900, 1, '年度', 'year', 'time_dimension', NULL, 'default', 'N', '0', '1', '2024-05-18 11:44:50', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1901, 2, '半年', 'halfYear', 'time_dimension', NULL, 'default', 'N', '0', '1', '2024-05-18 11:45:06', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1902, 3, '季度', 'quarter', 'time_dimension', NULL, 'default', 'N', '0', '1', '2024-05-18 11:45:33', '1', '2024-05-18 11:45:37', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1903, 4, '月度', 'month', 'time_dimension', NULL, 'default', 'N', '0', '1', '2024-05-18 11:45:58', '1', '2024-05-18 11:46:04', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1904, 5, '周', 'week', 'time_dimension', NULL, 'default', 'N', '0', '1', '2024-05-18 11:46:54', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1905, 6, '天', 'day', 'time_dimension', NULL, 'default', 'N', '0', '1', '2024-05-18 11:47:03', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1906, 0, 'S', '100', 'score_rate', NULL, 'default', 'N', '0', '1', '2024-05-20 17:10:54', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1907, 1, 'A', '90', 'score_rate', NULL, 'default', 'N', '0', '1', '2024-05-20 17:11:07', '1', '2024-05-20 17:11:13', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1908, 2, 'B', '80', 'score_rate', NULL, 'default', 'N', '0', '1', '2024-05-20 17:11:26', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1909, 3, 'C', '70', 'score_rate', NULL, 'default', 'N', '0', '1', '2024-05-20 17:11:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1910, 4, 'D', '0', 'score_rate', NULL, 'default', 'N', '0', '1', '2024-05-20 17:11:58', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1911, 0, '上月少发', '1', 'salary_reason', NULL, 'default', 'N', '0', '1', '2024-05-28 15:59:47', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1912, 1, '上月多发', '2', 'salary_reason', NULL, 'default', 'N', '0', '1', '2024-05-28 16:00:00', '1', '2024-05-29 17:21:37', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1913, 2, '本月补平', '3', 'salary_reason', NULL, 'default', 'N', '0', '1', '2024-05-28 16:00:13', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1931, 0, 'year', '2024', 'built_parameter', NULL, 'default', 'N', '0', '1', '2024-05-31 13:55:55', '1', '2024-05-31 13:56:14', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1932, 0, 'month', '5', 'built_parameter', NULL, 'default', 'N', '0', '1', '2024-05-31 13:56:25', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1933, 0, 'userId', '1', 'built_parameter', NULL, 'default', 'N', '0', '1', '2024-05-31 13:56:35', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1938, 0, '大于', '&gt;', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:07:24', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1939, 1, '小于', '&lt;', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:07:42', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1940, 2, '大于等于', '&gt;=', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:07:55', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1941, 3, '小于等于', '&lt;=', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:08:07', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1942, 4, '不等于', '!=', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:08:21', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1943, 5, '等于', '=', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:08:32', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1944, 6, '模糊匹配', 'like', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:09:00', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1945, 7, '包含', 'in', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:09:22', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1946, 8, '不包含', 'not in', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:09:33', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1947, 8, '不为null', 'not null', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:10:00', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1948, 9, '范围之间', 'between and', 'sql_operator', NULL, 'default', 'N', '0', '1', '2024-06-06 16:10:47', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1949, 1, 'Integer', 'java.lang.Integer', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:49:31', '1', '2024-06-12 09:12:25', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1950, 1, 'Long', 'java.lang.Long', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:49:47', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1951, 2, 'Double', 'java.lang.Double', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:50:01', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1952, 3, 'Float', 'java.lang.Float', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:50:19', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1953, 4, 'Boolean', 'java.lang.Boolean', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:50:36', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1954, 5, 'Character', 'java.lang.Character', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:50:48', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1955, 6, 'Byte', 'java.lang.Byte', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:51:01', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1956, 7, 'Short', 'java.lang.Short', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:51:15', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1958, 9, 'Long[]', '[Ljava.lang.Long;', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:51:46', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1959, 10, 'Double[]', '[Ljava.lang.Double;', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:52:06', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1960, 11, 'Float[]', '[Ljava.lang.Float;', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:52:24', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1961, 12, 'Boolean[]', '[Ljava.lang.Boolean;', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:52:48', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1962, 13, 'Character[]', '[Ljava.lang.Character;', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:53:04', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1963, 14, 'Byte[]', '[Ljava.lang.Byte;', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:53:20', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1964, 15, 'Short[]', '[Ljava.lang.Short;', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-11 15:53:36', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1965, 0, 'String', 'java.lang.String', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-12 09:12:20', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1966, 8, 'String[]', '[Ljava.lang.String;', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-12 09:13:32', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1967, 9, 'Integer[]', '[Ljava.lang.Integer;', 'basic_type_class', NULL, 'default', 'N', '0', '1', '2024-06-12 09:15:50', '1', '2024-06-12 09:16:26', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1968, 0, '左连接', 'left join', 'join_type', NULL, 'default', 'N', '0', '1', '2024-06-12 09:35:02', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1969, 1, '右连接', 'right join', 'join_type', NULL, 'default', 'N', '0', '1', '2024-06-12 09:35:20', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1970, 2, '内连接', 'inner join', 'join_type', NULL, 'default', 'N', '0', '1', '2024-06-12 09:35:30', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1971, 3, '全连接', 'full join', 'join_type', NULL, 'default', 'N', '0', '1', '2024-06-12 09:37:09', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1972, 0, '985', '0', 'institution_type', NULL, 'default', 'N', '0', '1', '2024-06-24 09:03:21', '1', '2024-07-03 11:46:23', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1973, 1, '211', '1', 'institution_type', NULL, 'default', 'N', '0', '1', '2024-06-24 09:04:07', '1', '2024-07-03 11:46:26', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1974, 2, '双一流', '2', 'institution_type', NULL, 'default', 'N', '0', '1', '2024-06-24 09:09:37', '1', '2024-07-03 11:46:04', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1975, 3, '海外留学', '3', 'institution_type', NULL, 'default', 'N', '0', '1', '2024-06-24 09:09:46', '1', '2024-07-03 11:46:18', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1978, 0, '学士', '0', 'academic_degree', NULL, 'default', 'N', '0', '1', '2024-06-24 09:13:12', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1979, 1, '硕士', '1', 'academic_degree', NULL, 'default', 'N', '0', '1', '2024-06-24 09:13:22', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1980, 2, '博士', '2', 'academic_degree', NULL, 'default', 'N', '0', '1', '2024-06-24 09:13:30', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1981, 0, '智联招聘', '0', 'resume_source', NULL, 'default', 'N', '0', '1', '2024-06-24 09:16:11', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1982, 1, 'Boss直聘', '1', 'resume_source', NULL, 'default', 'N', '0', '1', '2024-06-24 09:16:24', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1983, 2, '齐鲁人才网', '2', 'resume_source', NULL, 'default', 'N', '0', '1', '2024-06-24 09:16:34', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1984, 3, '拉钩网', '3', 'resume_source', NULL, 'default', 'N', '0', '1', '2024-06-24 09:16:45', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1985, 4, '内推', '4', 'resume_source', NULL, 'default', 'N', '0', '1', '2024-06-24 09:17:11', '1', '2024-06-26 09:05:43', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1986, 0, '新增简历', '0', 'interview_result', NULL, 'default', 'N', '0', '1', '2024-06-24 09:19:09', '1', '2024-06-27 14:35:01', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1988, 2, '面试通过', '2', 'interview_result', NULL, 'default', 'N', '0', '1', '2024-06-24 09:19:26', '1', '2024-06-27 14:35:09', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1989, 3, '待入职', '3', 'interview_result', NULL, 'default', 'N', '0', '1', '2024-06-24 09:19:43', '1', '2024-06-27 14:35:16', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1990, 4, '淘汰', '4', 'interview_result', NULL, 'default', 'N', '0', '1', '2024-06-24 09:19:54', '1', '2024-07-16 15:19:27', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1992, 0, '无试用期', '0', 'probation_period', NULL, 'default', 'N', '0', '1', '2024-06-24 09:22:14', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1993, 1, '1个月试用期', '1', 'probation_period', NULL, 'default', 'N', '0', '1', '2024-06-24 09:22:21', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1994, 2, '3个月试用期', '2', 'probation_period', NULL, 'default', 'N', '0', '1', '2024-06-24 09:22:35', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1995, 3, '6个月试用期', '3', 'probation_period', NULL, 'default', 'N', '0', '1', '2024-06-24 09:22:42', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1996, 4, '一年试用期', '4', 'probation_period', NULL, 'default', 'N', '0', '1', '2024-06-24 09:22:49', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1997, 0, '予以录用', '0', 'employment_decision', NULL, 'default', 'N', '0', '1', '2024-06-24 10:01:17', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1998, 1, '储备', '1', 'employment_decision', NULL, 'default', 'N', '0', '1', '2024-06-24 10:01:30', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (1999, 2, '不予考虑', '2', 'employment_decision', NULL, 'default', 'N', '0', '1', '2024-06-24 10:01:39', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2000, 0, '主动投递', '0', 'obtain_method', NULL, 'default', 'N', '0', '1', '2024-06-24 10:35:34', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2001, 1, 'hr沟通', '1', 'obtain_method', NULL, 'default', 'N', '0', '1', '2024-06-24 10:35:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2002, 0, '初中', '0', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-24 14:17:04', '1', '2024-06-25 10:41:44', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2003, 1, '高中', '1', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-24 14:17:14', '1', '2024-06-25 10:44:23', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2004, 2, '中专/中技', '2', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-24 14:17:20', '1', '2024-06-25 10:44:37', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2005, 0, '无经验', '0-0', 'work_year', NULL, 'default', 'N', '0', '1', '2024-06-24 14:28:11', '1', '2024-06-25 16:10:19', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2006, 1, '1年以内', '0-1', 'work_year', NULL, 'default', 'N', '0', '1', '2024-06-24 14:28:23', '1', '2024-06-25 16:10:23', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2007, 2, '1-3年', '1-3', 'work_year', NULL, 'default', 'N', '0', '1', '2024-06-24 14:28:47', '1', '2024-06-25 16:10:27', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2008, 3, '3-5年', '3-5', 'work_year', NULL, 'default', 'N', '0', '1', '2024-06-24 14:29:00', '1', '2024-06-25 16:10:30', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2009, 4, '5-10年', '5-10', 'work_year', NULL, 'default', 'N', '0', '1', '2024-06-24 14:29:24', '1', '2024-06-25 16:10:34', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2010, 5, '10年以上', '10-100', 'work_year', NULL, 'default', 'N', '0', '1', '2024-06-24 14:29:44', '1', '2024-06-25 16:10:38', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2011, 0, '待沟通', '05', 'candidate_status', NULL, 'default', 'N', '0', '1', '2024-06-24 15:58:49', '1', '2024-07-03 14:30:12', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2013, 1, '待签到', '07', 'candidate_status', NULL, 'default', 'N', '0', '1', '2024-06-24 15:59:25', '1', '2024-07-03 14:31:00', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2014, 2, '待面试', '08', 'candidate_status', NULL, 'default', 'N', '0', '1', '2024-06-24 15:59:35', '1', '2024-07-03 14:31:02', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2015, 3, '(几轮)面试成功', '12', 'candidate_status', NULL, 'default', 'N', '0', '1', '2024-06-24 15:59:49', '1', '2024-07-03 14:31:06', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2018, 3, '大专', '3', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-25 10:44:46', '1', '2024-06-25 10:44:49', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2019, 4, '本科', '4', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-25 10:44:57', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2020, 5, '硕士', '5', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-25 10:45:10', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2021, 6, 'MBA/EMBA', '6', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-25 10:45:29', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2022, 7, '博士', '7', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-25 10:45:38', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2023, 8, '大专及以上', '&gt;2', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-25 11:35:48', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2024, 9, '本科及以上', '&gt;3', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-25 11:36:02', '1', '2024-07-10 14:03:47', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2025, 10, '硕士及以上', '&gt;4', 'worker_education', NULL, 'default', 'N', '0', '1', '2024-06-25 11:36:47', '1', '2024-06-25 11:37:22', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2026, 0, '20-25', '20-25', 'age_range', NULL, 'default', 'N', '0', '1', '2024-06-25 13:49:51', '1', '2024-06-25 17:43:35', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2027, 1, '25-30', '25-30', 'age_range', NULL, 'default', 'N', '0', '1', '2024-06-25 13:50:03', '1', '2024-06-25 17:43:41', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2028, 2, '30-35', '30-35', 'age_range', NULL, 'default', 'N', '0', '1', '2024-06-25 13:50:15', '1', '2024-06-25 17:43:46', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2029, 3, '35-40', '35-40', 'age_range', NULL, 'default', 'N', '0', '1', '2024-06-25 13:50:26', '1', '2024-06-25 17:43:51', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2030, 4, '40岁以上', '40-100', 'age_range', NULL, 'default', 'N', '0', '1', '2024-06-25 13:50:38', '1', '2024-06-25 17:43:58', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2031, 0, '想看机会', '0', 'person_status', NULL, 'default', 'N', '0', '1', '2024-06-25 13:53:29', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2032, 1, '现状稳定', '1', 'person_status', NULL, 'default', 'N', '0', '1', '2024-06-25 13:53:48', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2033, 2, '电话空号', '2', 'person_status', NULL, 'default', 'N', '0', '1', '2024-06-25 13:54:25', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2034, 3, '成功offer', '3', 'person_status', NULL, 'default', 'N', '0', '1', '2024-06-25 13:54:34', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2035, 4, '需再联系', '4', 'person_status', NULL, 'default', 'N', '0', '1', '2024-06-25 13:54:55', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2036, 5, '暂不换工作', '5', 'person_status', NULL, 'default', 'N', '0', '1', '2024-06-25 13:55:14', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2037, 5, '其他公司介绍', '5', 'resume_source', NULL, 'default', 'N', '0', '1', '2024-06-26 09:05:57', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2038, 6, '猎头介绍', '6', 'resume_source', NULL, 'default', 'N', '0', '1', '2024-06-26 09:06:05', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2039, 7, '其他', '7', 'resume_source', NULL, 'default', 'N', '0', '1', '2024-06-26 09:06:13', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2040, 0, '离职-找工作', '0', 'work_condition', NULL, 'default', 'N', '0', '1', '2024-06-26 09:27:30', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2041, 1, '在职-找工作', '1', 'work_condition', NULL, 'default', 'N', '0', '1', '2024-06-26 09:27:42', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2042, 2, '在职-看机会', '2', 'work_condition', NULL, 'default', 'N', '0', '1', '2024-06-26 09:27:52', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2043, 3, '在职-不找工作', '3', 'work_condition', NULL, 'default', 'N', '0', '1', '2024-06-26 09:28:02', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2044, 0, '予以录用', '1', 'interview_status', NULL, 'default', 'N', '0', '1', '2024-06-26 10:46:02', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2045, 1, '待定', '2', 'interview_status', NULL, 'default', 'N', '0', '1', '2024-06-26 10:46:16', '1', '2024-06-27 15:06:27', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2046, 2, '不予考虑', '3', 'interview_status', NULL, 'default', 'N', '0', '1', '2024-06-26 10:46:24', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2049, 0, '项目需要', '0', 'employ_reason', NULL, 'default', 'N', '0', '1', '2024-06-27 14:16:57', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2050, 0, '后端开发', '0', 'position', NULL, 'default', 'N', '0', '1', '2024-06-27 14:21:18', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2051, 1, '前端开发', '1', 'position', NULL, 'default', 'N', '0', '1', '2024-06-27 14:21:29', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2052, 2, '产品经理', '2', 'position', NULL, 'default', 'N', '0', '1', '2024-06-27 14:21:35', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2053, 0, '技术不符合要求', '0', 'interrupt_reason', NULL, 'default', 'N', '0', '1', '2024-07-03 14:04:56', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2054, 1, '不接受offer', '1', 'interrupt_reason', NULL, 'default', 'N', '0', '1', '2024-07-03 14:05:04', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2055, 2, '沟通不畅', '2', 'interrupt_reason', NULL, 'default', 'N', '0', '1', '2024-07-03 14:05:16', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2056, 4, '(几轮)面试淘汰', '16', 'candidate_status', NULL, 'default', 'N', '0', '1', '2024-07-03 14:30:54', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2057, 5, '发放offer ', '18', 'candidate_status', NULL, 'default', 'N', '0', '1', '2024-07-03 14:31:20', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2058, 6, '已入职', '19', 'candidate_status', NULL, 'default', 'N', '0', '1', '2024-07-03 14:31:32', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2059, 7, '试用期', '20', 'candidate_status', NULL, 'default', 'N', '0', '1', '2024-07-03 14:31:42', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2060, 8, '未到场', '21', 'candidate_status', NULL, 'default', 'N', '0', '1', '2024-07-03 14:31:52', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2061, 0, '发起需求', '00', 'main_flow_status', NULL, 'default', 'N', '0', '1', '2024-07-03 15:36:23', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2062, 1, '流程进行中', '03', 'main_flow_status', NULL, 'default', 'N', '0', '1', '2024-07-03 15:36:41', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2063, 2, '招聘完成', '04', 'main_flow_status', NULL, 'default', 'N', '0', '1', '2024-07-03 15:37:14', '1', '2024-07-09 15:37:39', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2064, 3, '终止', '21', 'main_flow_status', NULL, 'default', 'N', '0', '1', '2024-07-03 15:37:22', '1', '2024-07-09 15:37:45', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2065, 0, '用人单位取消', '0', 'recruit_stop_reason', NULL, 'default', 'N', '0', '1', '2024-07-03 17:38:39', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2066, 1, '项目计划变更', '1', 'recruit_stop_reason', NULL, 'default', 'N', '0', '1', '2024-07-03 17:38:50', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2067, 2, '用人需求已满足', '2', 'recruit_stop_reason', NULL, 'default', 'N', '0', '1', '2024-07-03 17:39:04', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2068, 0, '履历不符合要求', 'LLBFHYQ', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-04 14:35:29', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2069, 1, '大佬', 'DL', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-04 14:35:38', '1', '2024-07-04 14:35:58', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2070, 2, '大厂经验', 'DCJY', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-04 14:35:54', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2071, 3, '未接通', 'WJT', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-04 14:36:11', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2072, 4, '素质不高', 'SZBG', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-04 14:36:23', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2073, 6, '还可以', 'hky', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-04 15:57:10', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2074, 7, '测试啊', 'csa', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-04 16:18:13', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2075, 7, '还有一个', 'hyyg', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-04 16:56:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2076, 8, '不知道', 'bzd', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-04 16:56:53', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2077, 9, '1313123', '1313123', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-04 17:22:15', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2078, 0, '微信', '1', 'contact_type', NULL, 'default', 'N', '0', '1', '2024-07-05 15:16:48', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2079, 0, 'qq', '2', 'contact_type', NULL, 'default', 'N', '0', '1', '2024-07-05 15:16:55', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2080, 0, '电话', '3', 'contact_type', NULL, 'default', 'N', '0', '1', '2024-07-05 15:17:02', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2081, 0, 'boss直聘', '4', 'contact_type', NULL, 'default', 'N', '0', '1', '2024-07-05 15:17:12', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2082, 0, '面试', '1', 'contactItem', NULL, 'default', 'N', '0', '1', '2024-07-05 15:18:00', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2083, 0, '入职', '2', 'contactItem', NULL, 'default', 'N', '0', '1', '2024-07-05 15:18:07', '1', '2024-07-05 15:18:13', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2084, 0, '近况', '3', 'contactItem', NULL, 'default', 'N', '0', '1', '2024-07-05 15:18:23', '1', '2024-07-05 15:18:29', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2085, 0, '一轮', '1', 'irInter_rounds', NULL, 'default', 'N', '0', '1', '2024-07-05 17:25:22', '1', '2024-07-10 09:16:11', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2086, 0, '二轮', '2', 'irInter_rounds', NULL, 'default', 'N', '0', '1', '2024-07-05 17:25:27', '1', '2024-07-10 09:16:08', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2087, 0, '三轮', '3', 'irInter_rounds', NULL, 'default', 'N', '0', '1', '2024-07-05 17:25:35', '1', '2024-07-10 09:16:06', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2088, 0, '四轮', '4', 'irInter_rounds', NULL, 'default', 'N', '0', '1', '2024-07-05 17:25:41', '1', '2024-07-10 09:16:04', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2089, 0, '现场面试', '1', 'irInterview_type', NULL, 'default', 'N', '0', '1', '2024-07-05 17:38:37', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2090, 0, '视频面试', '2', 'irInterview_type', NULL, 'default', 'N', '0', '1', '2024-07-05 17:38:53', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2091, 0, '电话面试', '3', 'irInterview_type', NULL, 'default', 'N', '0', '1', '2024-07-05 17:39:01', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2092, 9, '试用期淘汰', '22', 'candidate_status', NULL, 'default', 'N', '0', '1', '2024-07-08 16:08:53', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2093, 3, '薪资不合适', '3', 'interrupt_reason', NULL, 'default', 'N', '0', '1', '2024-07-09 14:39:00', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2094, 4, '工作经验不足', '4', 'interrupt_reason', NULL, 'default', 'N', '0', '1', '2024-07-09 14:40:55', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2095, 5, '分析能力不足', '5', 'interrupt_reason', NULL, 'default', 'N', '0', '1', '2024-07-09 14:44:33', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2096, 0, '男', '1', 'user_sex', NULL, 'default', 'N', '0', '1', '2024-07-09 16:13:00', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2097, 0, '女', '2', 'user_sex', NULL, 'default', 'N', '0', '1', '2024-07-09 16:13:04', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2098, 10, '这是标签', 'zsbq', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-10 11:57:25', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2099, 11, '1', '1', 'talent_bank_labels', NULL, 'default', 'N', '0', '1', '2024-07-10 11:57:41', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2100, 6, '123', '123', 'interrupt_reason', NULL, 'default', 'N', '0', '1', '2024-07-10 14:00:12', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2101, 0, '公司临时调走', '1', 'employ_reason', NULL, 'default', 'N', '0', '1', '2024-07-10 14:19:07', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2103, 0, '测试消息', '2', 'employ_reason', NULL, 'default', 'N', '0', '1', '2024-07-11 09:24:09', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2104, 0, 'FizTask用户', '1', 'account_right', NULL, 'default', 'N', '0', '1', '2024-07-12 08:59:22', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2105, 1, 'Gitlab账号', '2', 'account_right', NULL, 'default', 'N', '0', '1', '2024-07-12 08:59:31', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2106, 2, '钉钉用户', '3', 'account_right', NULL, 'default', 'N', '0', '1', '2024-07-12 08:59:42', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2107, 1, '一级指标', '1', 'indi_level', NULL, 'default', 'N', '0', '1', '2024-07-23 16:00:18', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2108, 2, '二级指标', '2', 'indi_level', NULL, 'default', 'N', '0', '1', '2024-07-23 16:00:24', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2109, 3, '三级指标', '3', 'indi_level', NULL, 'default', 'N', '0', '1', '2024-07-23 16:00:30', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2110, 4, '四级指标', '4', 'indi_level', NULL, 'default', 'N', '0', '1', '2024-07-23 16:00:38', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2115, 1, '公式', '1', 'calculation_type', NULL, 'default', 'N', '0', '1', '2024-07-24 14:54:17', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2116, 2, '同比', '2', 'calculation_type', NULL, 'default', 'N', '0', '1', '2024-07-24 14:54:33', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2117, 3, '环比', '3', 'calculation_type', NULL, 'default', 'N', '0', '1', '2024-07-24 14:54:42', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2118, 1, '新增', '1', 'task_risk_status', NULL, 'default', 'N', '0', '1', '2024-03-13 14:33:47', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2119, 2, '处理中', '2', 'task_risk_status', NULL, 'default', 'N', '0', '1', '2024-03-13 14:34:03', '1', '2024-03-19 09:10:35', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2120, 3, '消失', '3', 'task_risk_status', NULL, 'default', 'N', '0', '1', '2024-03-13 14:34:55', '1', '2024-03-19 09:10:31', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2121, 5, '不处理', '5', 'task_risk_status', NULL, 'default', 'N', '0', '1', '2024-03-13 14:35:12', '1', '2024-03-19 09:10:20', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2122, 4, '已发生', '4', 'task_risk_status', NULL, 'default', 'N', '0', '1', '2024-03-13 14:35:27', '1', '2024-03-19 09:10:09', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2123, 6, '已处理', '6', 'task_risk_status', NULL, 'default', 'N', '0', '1', '2024-03-13 14:35:36', '1', '2024-03-19 09:10:25', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2124, 1, '估算', '1', 'task_risk_category', NULL, 'default', 'N', '0', '1', '2024-03-13 15:03:50', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2125, 1, '客户', '2', 'task_risk_category', NULL, 'default', 'N', '0', '1', '2024-03-13 15:03:58', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2126, 1, '工作条件', '3', 'task_risk_category', NULL, 'default', 'N', '0', '1', '2024-03-13 15:04:06', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2127, 1, '进度', '4', 'task_risk_category', NULL, 'default', 'N', '0', '1', '2024-03-13 15:04:18', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2128, 1, '技术', '5', 'task_risk_category', NULL, 'default', 'N', '0', '1', '2024-03-13 15:04:30', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2129, 1, '管理', '6', 'task_risk_category', NULL, 'default', 'N', '0', '1', '2024-03-13 15:04:44', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2130, 1, '质量', '7', 'task_risk_category', NULL, 'default', 'N', '0', '1', '2024-03-13 15:05:05', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2131, 1, '成本', '8', 'task_risk_category', NULL, 'default', 'N', '0', '1', '2024-03-13 15:05:28', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2132, 1, '资源', '9', 'task_risk_category', NULL, 'default', 'N', '0', '1', '2024-03-13 15:05:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2133, 1, '需求不明确', '10', 'task_risk_category', NULL, 'default', 'N', '0', '1', '2024-03-13 15:05:53', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2134, 1, '一级风险', '1', 'task_risk_severity', NULL, 'default', 'N', '0', '1', '2024-03-13 15:06:22', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2135, 2, '二级风险', '2', 'task_risk_severity', NULL, 'default', 'N', '0', '1', '2024-03-13 15:06:37', '1', '2024-03-14 15:03:42', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2136, 3, '三级风险', '3', 'task_risk_severity', NULL, 'default', 'N', '0', '1', '2024-03-13 15:06:46', '1', '2024-03-14 15:03:25', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2137, 4, '四级风险', '4', 'task_risk_severity', NULL, 'default', 'N', '0', '1', '2024-03-13 15:07:02', '1', '2024-03-14 15:03:38', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2138, 5, '五级风险', '5', 'task_risk_severity', NULL, 'default', 'N', '0', '1', '2024-03-13 15:07:11', '1', '2024-03-14 15:03:35', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2139, 1, '基本确定', '5', 'task_risk_probability', NULL, 'default', 'N', '0', '1', '2024-03-13 15:08:56', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2140, 1, '很可能', '4', 'task_risk_probability', NULL, 'default', 'N', '0', '1', '2024-03-13 15:09:07', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2141, 1, '有可能', '3', 'task_risk_probability', NULL, 'default', 'N', '0', '1', '2024-03-13 15:09:35', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2142, 1, '不大可能', '2', 'task_risk_probability', NULL, 'default', 'N', '0', '1', '2024-03-13 15:10:20', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2143, 1, '极小', '1', 'task_risk_probability', NULL, 'default', 'N', '0', '1', '2024-03-13 15:10:46', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2144, 1, '风险创建', '1', 'task_risk_change_type', NULL, 'default', 'N', '0', '1', '2024-03-13 15:12:13', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2145, 1, '风险确认', '2', 'task_risk_change_type', NULL, 'default', 'N', '0', '1', '2024-03-13 15:12:25', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2146, 1, '风险修改', '3', 'task_risk_change_type', NULL, 'default', 'N', '0', '1', '2024-03-13 15:12:42', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2147, 1, '风险处理', '4', 'task_risk_change_type', NULL, 'default', 'N', '0', '1', '2024-03-13 15:13:05', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2148, 0, '火车', 'train', 'trafficType', NULL, 'default', 'N', '0', '1', '2024-03-20 14:21:00', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2149, 1, '汽车', 'automobile', 'trafficType', NULL, 'default', 'N', '0', '1', '2024-03-20 14:42:03', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2150, 2, '高铁', 'highSpeedRail', 'trafficType', NULL, 'default', 'N', '0', '1', '2024-03-20 14:43:02', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2151, 3, '飞机', 'aircraft', 'trafficType', NULL, 'default', 'N', '0', '1', '2024-03-20 14:43:43', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2153, 0, 'sum', 'sum', 'af_type', NULL, 'default', 'N', '0', '1', '2024-05-07 19:05:01', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2154, 1, 'count', 'count', 'af_type', NULL, 'default', 'N', '0', '1', '2024-05-07 19:05:14', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2155, 2, 'avg', 'avg', 'af_type', NULL, 'default', 'N', '0', '1', '2024-05-07 19:05:24', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2156, 0, '人事', '001', 'other_position', NULL, 'default', 'N', '0', '1', '2024-05-11 10:27:38', '1', '2024-05-20 10:44:49', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2157, 0, '财务', '002', 'other_position', NULL, 'default', 'N', '0', '1', '2024-05-11 10:28:22', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2158, 1, '天', '1', 'sub_cycle', NULL, 'default', 'N', '0', '1', '2024-05-14 15:09:36', '1', '2024-05-14 15:10:03', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2159, 2, '月', '2', 'sub_cycle', NULL, 'default', 'N', '0', '1', '2024-05-14 15:09:46', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2160, 3, '小时', '3', 'sub_cycle', NULL, 'default', 'N', '0', '1', '2024-05-14 15:10:15', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2161, 1, '指标', '1', 'source_type', NULL, 'default', 'N', '0', '1', '2024-05-14 15:10:58', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2162, 2, '人工输入', '2', 'source_type', NULL, 'default', 'N', '0', '1', '2024-05-14 15:11:09', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2163, 3, 'sql定义', '3', 'source_type', NULL, 'default', 'N', '0', '1', '2024-05-14 15:11:25', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2164, 4, '文件', '4', 'source_type', NULL, 'default', 'N', '0', '1', '2024-05-14 15:11:35', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2165, 5, '自定义脚本', '5', 'source_type', NULL, 'default', 'N', '0', '1', '2024-05-14 15:11:47', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2166, 1, '基本值', '0', 'sub_prop', NULL, 'default', 'N', '0', '1', '2024-05-14 15:17:42', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2167, 2, '定值', '1', 'sub_prop', NULL, 'default', 'N', '0', '1', '2024-05-14 15:17:54', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2168, 3, '动态值', '2', 'sub_prop', NULL, 'default', 'N', '0', '1', '2024-05-14 15:18:10', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2169, 3, 'max', 'max', 'af_type', NULL, 'default', 'N', '0', '1', '2024-06-05 17:51:03', '1', '2024-06-05 17:51:08', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2170, 4, 'min', 'min', 'af_type', NULL, 'default', 'N', '0', '1', '2024-06-05 17:51:19', '', NULL, NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2171, 5, '标准差', 'stddev', 'af_type', NULL, 'default', 'N', '0', '1', '2024-06-06 09:49:36', '1', '2024-06-06 09:49:57', NULL, 0);
INSERT INTO `sys_dict_data` VALUES (2172, 6, '方差', 'variance', 'af_type', NULL, 'default', 'N', '0', '1', '2024-06-06 09:50:22', '1', '2024-06-06 09:50:28', NULL, 0);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '备注',
  `inher_dict` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 571 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', '1', '2022-03-14 16:32:42', '1', '2023-01-31 16:54:14', '用户性别列表', NULL);
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '菜单状态列表', NULL);
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '系统开关列表', NULL);
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '任务状态列表', NULL);
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '任务分组列表', NULL);
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '系统是否列表', NULL);
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '通知类型列表', NULL);
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '通知状态列表', NULL);
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', '1', '2022-03-14 16:32:42', NULL, NULL, '操作类型列表', NULL);
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', '1', '2022-03-14 16:32:42', '1', '2022-03-23 11:30:01', '登录状态列表', NULL);
INSERT INTO `sys_dict_type` VALUES (105, '模型类型', 'modelType', '0', '1', '2022-05-18 13:33:40', '1', '2022-05-18 13:34:04', '流程模型类型', NULL);
INSERT INTO `sys_dict_type` VALUES (106, '密钥类型', 'secretType', '0', '1', '2022-05-19 20:08:02', NULL, NULL, '密钥类型', NULL);
INSERT INTO `sys_dict_type` VALUES (107, '认证协议类型', 'authProtocol', '0', '1', '2022-05-19 20:08:18', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (108, '授权模式', 'authMode', '0', '1', '2022-05-19 20:08:36', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (109, '单点登录类型', 'ssoType', '0', '1', '2022-05-19 20:14:20', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (110, '应用状态', 'appStatus', '0', '1', '2022-05-19 20:27:21', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (111, '部门', 'department', '0', '1', '2022-05-23 16:22:43', '1', '2022-07-04 09:52:35', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (127, '系统通用状态', 'status', '0', '1', '2022-07-11 09:43:00', NULL, NULL, '系统通用状态：正常 停用', NULL);
INSERT INTO `sys_dict_type` VALUES (156, '民族', 'nationality', '0', '1', '2022-08-19 14:08:19', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (157, '证件类型', 'idType', '0', '1', '2022-08-19 14:25:29', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (159, '学历', 'education', '0', '1', '2022-08-19 14:33:45', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (344, '项目状态', 'projectStatus', '0', '1', '2023-08-11 14:18:50', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (345, '项目分类', 'projectCategory', '0', '1', '2023-08-11 15:12:42', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (346, '项目标签', 'projectTag', '0', '1', '2023-08-11 15:17:43', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (347, '项目类型', 'projectType', '0', '1', '2023-08-11 15:19:10', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (349, '系统需求规格说明书类型', 'specificationType', '0', '1', '2023-08-11 17:54:55', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (350, '系统需求规格说明书性质', 'specificationNature', '0', '1', '2023-08-11 17:55:39', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (351, '系统需求规格说明书状态', 'specificationStatus', '0', '1', '2023-08-11 17:56:32', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (352, '用户状态', 'user_status', '0', NULL, '2021-02-01 02:42:23', NULL, '2021-02-01 05:04:42', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (353, '资源类型', 'res_type', '0', NULL, '2021-02-01 05:05:38', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (354, '字典类型', 'dict_type', '0', NULL, '2021-02-01 05:47:55', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (355, '角色类型', 'role_type', '0', NULL, '2021-02-01 05:55:10', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (356, '货币类型', 'currencyType', '0', NULL, '2021-08-31 15:05:40', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (357, '合同类型', 'contractType', '0', NULL, '2021-08-31 15:06:07', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (358, '电子合同类型', 'elContractType', '0', NULL, '2021-08-31 15:16:06', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (359, '回款类型', 'receivableType', '0', NULL, '2021-08-31 15:20:22', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (360, '支付方式', 'payMode', '0', NULL, '2021-09-07 10:00:39', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (361, '班级级别', 'classLevel', '0', NULL, '2021-10-08 11:32:09', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (362, '计划状态', 'planStatus', '0', NULL, '2021-10-13 10:37:45', NULL, '2021-10-13 10:37:54', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (363, '个人周计划状态', 'pWeekPlanStatus', '0', NULL, '2021-10-13 10:42:47', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (364, '关键路径', 'criticalPath', '0', NULL, '2021-10-13 10:46:55', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (365, '计划类型', 'planType', '0', NULL, '2021-10-13 10:49:17', NULL, '2021-10-13 10:49:54', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (366, '通用是否状态', 'commonTrueFalse', '0', NULL, '2021-10-13 10:49:46', NULL, '2021-10-13 10:51:22', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (367, '项目计划类型', 'projectPlanType', '0', NULL, '2021-10-13 17:35:16', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (368, '我的任务状态', 'taskStatus', '0', NULL, '2021-10-14 14:05:38', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (369, '任务原因类型', 'taskReasonType', '0', NULL, '2021-10-14 15:55:33', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (370, '项目周计划状态', 'weekPlanStatus', '0', NULL, '2021-10-14 16:10:32', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (371, '工时经验库类型', 'heTypeOptions', '0', NULL, '2021-10-15 16:59:13', NULL, '2021-10-15 17:18:40', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (372, '工时经验库功能项类型', 'funcTypeOptions', '0', NULL, '2021-10-15 17:18:17', NULL, '2021-10-15 17:18:35', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (373, '审核结果', 'approveResult', '0', NULL, '2021-10-20 13:55:44', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (374, '规格说明书变更类型', 'srChangeType', '0', NULL, '2021-10-21 10:30:42', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (375, '交付物类型', 'deliverableType', '0', NULL, '2021-10-22 11:30:32', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (376, '交付物状态', 'deliverableStatus', '0', NULL, '2021-10-22 11:31:17', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (377, '项目资源状态', 'projectResourceStatus', '0', NULL, '2021-10-22 15:15:38', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (378, '合同状态', 'contractStatus', '0', NULL, '2021-10-25 11:48:53', NULL, '2021-10-25 11:58:41', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (379, '税率', 'contractRate', '0', NULL, '2021-10-25 11:49:53', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (380, '参会状态', 'joinMeetingStatus', '0', NULL, '2021-10-25 14:50:12', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (381, '合同回款状态', 'conReturnStatus', '0', NULL, '2021-10-25 17:20:16', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (382, '文档类型', 'docType', '0', NULL, '2021-10-25 17:32:04', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (383, '风险状态', 'riskStatus', '0', NULL, '2021-10-26 09:46:33', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (384, '风险类别', 'riskType', '0', NULL, '2021-10-26 09:46:59', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (385, '风险严重程度', 'riskSeverity', '0', NULL, '2021-10-26 14:51:13', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (386, '风险发生概率', 'riskProbability', '0', NULL, '2021-10-26 14:51:30', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (387, '性别', 'gender', '0', NULL, '2021-10-27 17:58:44', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (388, '人员类型', 'personnelType', '0', NULL, '2021-10-27 17:58:57', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (389, '人员状态', 'personnelStatus', '0', NULL, '2021-10-27 17:59:12', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (390, '证件类型', 'credentialsType', '0', NULL, '2021-10-27 17:59:46', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (391, '项目成员状态', 'memberStatus', '0', NULL, '2021-10-28 13:51:05', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (392, '报销类型', 'eaType', '0', NULL, '2021-10-29 16:42:43', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (393, '角色状态', 'role_status', '0', NULL, '2021-11-09 10:02:55', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (394, '通用状态', 'general_status', '0', NULL, '2021-11-09 10:26:12', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (395, '测试人员集合', 'trunTestRoles', '0', NULL, '2021-11-18 10:44:06', NULL, '2021-11-18 10:44:18', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (397, '钉钉接口配置', 'dingtalkConfig', '0', NULL, '2021-12-22 17:29:49', NULL, '2021-12-23 10:27:48', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (398, '线索客户管理类型', 'clueType', '0', NULL, '2021-12-23 11:57:40', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (399, '线索客户管理状态', 'cluestatus', '0', NULL, '2021-12-23 11:58:13', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (400, '每日定时任务开关', 'dailyScheduledTasks', '0', NULL, '2021-12-29 14:14:45', NULL, '2021-12-29 14:26:44', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (401, '打卡类型', 'checkType', '0', NULL, '2021-12-30 11:23:41', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (402, '打卡结果', 'timeResult', '0', NULL, '2021-12-30 11:24:40', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (403, '定位结果', 'locationResult', '0', NULL, '2021-12-30 11:39:49', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (404, '投标类型', 'bidType', '0', NULL, '2022-01-04 17:08:24', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (405, '销售阶段', 'saleStage', '0', NULL, '2022-01-04 17:13:10', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (406, '成员角色', 'memberRole', '0', NULL, '2022-01-07 10:22:09', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (407, '合同回款延期状态', 'returnDelay', '0', NULL, '2022-01-10 10:28:25', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (408, '服务类型', 'serviceType', '0', NULL, '2022-01-11 18:14:42', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (409, '网络位置', 'serviceLocation', '0', NULL, '2022-01-11 18:14:59', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (410, '服务状态', 'serviceState', '0', NULL, '2022-01-11 18:15:12', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (411, '干系人状态', 'stakeholderState', '0', NULL, '2022-01-25 09:41:43', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (412, '年假时间配置', 'holiday_time_config', '0', NULL, '2022-03-29 16:05:20', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (413, '线索状态', 'clue_status', '0', NULL, '2022-04-18 10:07:00', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (414, '考评标记', 'evaluationFlag', '0', NULL, '2022-06-10 21:10:07', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (415, '评价结果', 'evaluateStatus', '0', NULL, '2022-07-06 09:59:18', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (416, '绩效考评类别', 'attendanceStatus', '0', NULL, '2022-07-06 11:28:05', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (417, '考评月', 'wrorkItem', '0', NULL, '2022-07-13 15:12:12', NULL, '2022-07-13 16:30:36', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (418, '考评时间配置', 'setAttendance', '0', NULL, '2022-07-13 16:25:43', NULL, '2022-07-13 16:31:37', NULL, 1);
INSERT INTO `sys_dict_type` VALUES (419, '释放人员状态', 'releaseStatus', '0', NULL, '2022-08-15 16:33:02', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (420, '释放原因', 'releaseReason', '0', NULL, '2022-08-16 11:01:47', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (421, '人员空闲状态', 'personnelFree', '0', NULL, '2022-08-19 18:47:23', NULL, NULL, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (479, '频道类型', 'channel_type', '1', NULL, '2023-11-27 11:46:20', '1', '2023-12-24 09:40:49', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (480, '文件类型', 'resource_type', '0', NULL, '2023-11-27 11:46:20', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (481, '文件类型细分', 'resource_type_detail', '0', NULL, '2023-11-27 11:46:20', NULL, NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (482, '工作类型', 'work_type', '0', '1', '2023-12-06 18:06:09', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (483, '工时状态', 'work_status', '0', '1', '2023-12-07 14:45:56', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (484, '任务优先级', 'task_priority', '0', '1', '2023-12-07 14:55:46', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (485, '任务类型', 'task_type', '0', '1', '2023-12-07 15:56:59', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (486, '任务状态', 'task_status', '0', '1', '2023-12-07 16:14:43', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (487, '项目类型', 'task_project_type', '0', '1', '2023-12-09 17:16:39', '1', '2024-01-20 10:47:04', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (488, '项目任务问题类型', 'task_question_type', '0', '1', '2023-12-11 17:28:24', '1', '2023-12-12 15:10:47', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (489, '项目质量控制状态', 'task_question_status', '0', '1', '2023-12-11 17:57:35', '1', '2023-12-12 15:12:42', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (490, '项目质量优先级', 'task_priority_deprecated', '1', '1', '2023-12-11 18:04:54', '1', '2023-12-12 15:25:07', '项目质量控制优先级', 0);
INSERT INTO `sys_dict_type` VALUES (491, '项目质量控制程度', 'task_question_leave', '0', '1', '2023-12-11 18:09:15', '1', '2023-12-12 15:15:11', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (492, '用户类型', 'task_user_type', '0', '1', '2023-12-12 15:20:24', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (493, '院校类型', 'em_grad_status', '0', '1', '2023-12-12 17:31:37', '', NULL, '院校类型列表', 0);
INSERT INTO `sys_dict_type` VALUES (494, '学位类型', 'em_grad_type_status', '0', '1', '2023-12-12 17:34:12', '1', '2023-12-12 17:34:32', '学位类型列表', 0);
INSERT INTO `sys_dict_type` VALUES (495, '入职类型', 'em_type_status', '0', '1', '2023-12-12 17:36:18', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (496, '消息类型', 'im_message_type', '0', '1', '2023-12-21 09:56:54', '1', '2023-12-21 10:03:07', '聊天：消息类型', 0);
INSERT INTO `sys_dict_type` VALUES (497, '频道类型', 'im_channel_type', '0', '1', '2023-12-21 09:58:09', '1', '2023-12-21 10:02:05', '聊天：频道类型', 0);
INSERT INTO `sys_dict_type` VALUES (498, '工作时间', 'work_time', '0', '1', '2023-12-22 15:17:52', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (499, '在职状态', 'job_status', '0', '1', '2023-12-23 16:35:27', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (500, '流程状态', 'process_status', '0', '1', '2023-12-28 15:42:47', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (501, '任务编辑模式', 'task_edit_mode', '0', '1', '2024-01-20 07:52:48', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (502, '项目分类', 'task_project_class', '0', '1', '2024-01-20 08:00:56', '1', '2024-01-20 10:47:09', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (503, '加班类型', 'overWork', '0', '1', '2024-01-22 03:17:30', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (504, '任务延期原因', 'task_info_delay_reason', '0', '1', '2024-03-05 09:52:25', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (505, '打分规则', 'rule_type', '0', '1', '2024-03-29 02:52:12', '', NULL, '打分规则', 0);
INSERT INTO `sys_dict_type` VALUES (506, '考评时间', 'assessment_time', '0', '1', '2024-05-06 06:58:09', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (507, '大数钉钉接口配置', 'ding_talk_config', '0', '1', '2024-05-07 03:06:25', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (508, '表单类型', 'form_type', '0', '1', '2024-05-16 00:42:56', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (509, '应用列表', 'app_list', '0', '1', '2024-07-24 00:42:44', '', NULL, '应用升级包管理：应用列表', 0);
INSERT INTO `sys_dict_type` VALUES (510, '应用平台', 'app_platform', '0', '1', '2024-07-24 01:21:05', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (522, '时间维度', 'time_dimension', '0', '1', '2024-05-18 11:44:26', '', NULL, '时间维度周期', 0);
INSERT INTO `sys_dict_type` VALUES (523, '得分评级', 'score_rate', '0', '1', '2024-05-20 17:09:53', '', NULL, '绩效考核得分评级', 0);
INSERT INTO `sys_dict_type` VALUES (524, '工资增补项原因', 'salary_reason', '0', '1', '2024-05-28 15:38:01', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (525, 'SQL内置参数', 'built_parameter', '1', '1', '2024-05-31 13:55:12', '1', '2024-06-24 09:00:40', '绩效管理sql内置参数', 0);
INSERT INTO `sys_dict_type` VALUES (526, '操作符', 'sql_operator', '0', '1', '2024-06-06 16:04:32', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (527, 'Java基础类型以及数组类', 'basic_type_class', '0', '1', '2024-06-11 15:48:34', '1', '2024-06-11 15:48:46', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (528, '表关联类型', 'join_type', '0', '1', '2024-06-12 09:34:41', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (529, '院校类型', 'institution_type', '0', '1', '2024-06-24 09:01:50', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (530, '学位', 'academic_degree', '0', '1', '2024-06-24 09:12:55', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (531, '简历来源', 'resume_source', '0', '1', '2024-06-24 09:15:09', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (532, '面试状态', 'interview_result', '0', '1', '2024-06-24 09:18:58', '1', '2024-06-27 14:44:58', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (533, '试用期', 'probation_period', '0', '1', '2024-06-24 09:22:04', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (534, '录用决策', 'employment_decision', '1', '1', '2024-06-24 10:01:01', '1', '2024-06-27 15:03:55', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (535, '简历获取方式', 'obtain_method', '0', '1', '2024-06-24 10:35:21', '1', '2024-06-24 14:15:14', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (536, '学历', 'worker_education', '0', '1', '2024-06-24 14:16:39', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (537, '工作年限', 'work_year', '0', '1', '2024-06-24 14:27:53', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (538, '候选人状态', 'candidate_status', '0', '1', '2024-06-24 15:57:43', '1', '2024-07-03 14:28:24', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (539, '年龄范围', 'age_range', '0', '1', '2024-06-25 13:49:43', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (540, '人员状态', 'person_status', '0', '1', '2024-06-25 13:53:06', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (541, '工作状态', 'work_condition', '0', '1', '2024-06-26 09:26:56', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (542, '录用决策', 'interview_status', '0', '1', '2024-06-26 10:45:40', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (543, '用人原因', 'employ_reason', '0', '1', '2024-06-27 14:16:41', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (544, '职位', 'position', '0', '1', '2024-06-27 14:21:09', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (545, '入职中断原因', 'interrupt_reason', '0', '1', '2024-07-03 14:03:55', '1', '2024-07-03 14:04:37', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (546, '主流程状态', 'main_flow_status', '0', '1', '2024-07-03 15:36:07', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (547, '招聘终止原因', 'recruit_stop_reason', '0', '1', '2024-07-03 17:38:25', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (548, '人才库标签', 'talent_bank_labels', '0', '1', '2024-07-04 14:33:43', '1', '2024-07-04 14:34:01', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (549, '沟通方式', 'contact_type', '0', '1', '2024-07-05 15:16:16', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (550, '沟通事项', 'contactItem', '0', '1', '2024-07-05 15:17:50', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (551, '面试类型', 'irInterview_type', '0', '1', '2024-07-05 17:24:25', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (552, '面试轮数', 'irInter_rounds', '0', '1', '2024-07-05 17:24:56', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (554, 'hr服务用户性别', 'user_sex', '0', '1', '2024-07-09 16:12:48', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (555, '入职账号权限', 'account_right', '0', '1', '2024-07-12 08:58:28', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (556, '指标层级', 'indi_level', '0', '1', '2024-07-23 15:59:18', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (559, '复合指标计算类型', 'calculation_type', '0', '1', '2024-07-24 14:53:59', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (560, '风险状态', 'task_risk_status', '0', '1', '2024-03-13 14:29:30', '1', '2024-03-18 18:21:15', '风险状态列表', 0);
INSERT INTO `sys_dict_type` VALUES (561, '风险类别', 'task_risk_category', '0', '1', '2024-03-13 14:59:28', '1', '2024-03-18 18:22:02', '风险类别列表', 0);
INSERT INTO `sys_dict_type` VALUES (562, '风险严重程度', 'task_risk_severity', '0', '1', '2024-03-13 15:02:26', '1', '2024-03-18 18:22:29', '风险严重程度列表', 0);
INSERT INTO `sys_dict_type` VALUES (563, '风险发生概率', 'task_risk_probability', '0', '1', '2024-03-13 15:08:19', '1', '2024-03-18 18:23:08', '风险发生概率列表', 0);
INSERT INTO `sys_dict_type` VALUES (564, '风险变更类型', 'task_risk_change_type', '0', '1', '2024-03-13 15:11:48', '1', '2024-03-18 18:23:45', '风险变更类型列表', 0);
INSERT INTO `sys_dict_type` VALUES (565, '交通类型', 'trafficType', '0', '1', '2024-03-20 14:18:29', '1', '2024-03-20 14:19:07', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (566, '聚合公式', 'af_type', '0', '1', '2024-05-07 19:04:43', '1', '2024-06-05 17:50:34', '指标管理聚合公式', 0);
INSERT INTO `sys_dict_type` VALUES (567, '人事、财务等特殊岗位（HR服务）', 'other_position', '0', '1', '2024-05-11 10:22:58', '1', '2024-05-11 10:25:44', NULL, 0);
INSERT INTO `sys_dict_type` VALUES (568, '计算周期', 'sub_cycle', '0', '1', '2024-05-14 15:08:29', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (569, '数据来源', 'source_type', '0', '1', '2024-05-14 15:10:38', '', NULL, NULL, 0);
INSERT INTO `sys_dict_type` VALUES (570, '科目属性', 'sub_prop', '0', '1', '2024-05-14 15:17:20', '', NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_division
-- ----------------------------
DROP TABLE IF EXISTS `sys_division`;
CREATE TABLE `sys_division`  (
  `division_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int NULL DEFAULT NULL,
  `division_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区划编码',
  `division_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区划名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `short_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区划简称',
  `parent_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级区划编码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态;0-正常；1-停用',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`division_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1230 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '区划' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_division
-- ----------------------------
INSERT INTO `sys_division` VALUES (1, NULL, '0531', '济南市', 0, NULL, '0', '0', '1', NULL, NULL, NULL);
INSERT INTO `sys_division` VALUES (3, NULL, '0532', '历城区', 0, NULL, '0531', '0', '1', NULL, NULL, NULL);
INSERT INTO `sys_division` VALUES (4, NULL, '0533', '章丘区', 0, '', '0531', '0', '1', NULL, NULL, NULL);
INSERT INTO `sys_division` VALUES (5, NULL, '0534', '历下区', 0, NULL, '0531', '0', '1', NULL, NULL, NULL);
INSERT INTO `sys_division` VALUES (6, NULL, '053301', '绣惠镇', 0, NULL, '0533', '0', '1', NULL, NULL, NULL);
INSERT INTO `sys_division` VALUES (16, NULL, '001', '北京市', 0, NULL, '0', '0', '1', '2022-06-30 09:08:33', NULL, '2022-07-20 14:17:25');
INSERT INTO `sys_division` VALUES (1223, NULL, '460000', '海南省', 0, NULL, '0', '0', NULL, '2023-03-15 20:24:13', NULL, NULL);
INSERT INTO `sys_division` VALUES (1224, NULL, '460100', '海口市', 1, NULL, '460000', '0', NULL, '2023-03-15 20:26:11', NULL, NULL);
INSERT INTO `sys_division` VALUES (1225, NULL, '460200', '三亚市', 2, NULL, '460000', '0', NULL, '2023-03-15 20:26:31', NULL, NULL);
INSERT INTO `sys_division` VALUES (1226, NULL, '460300', '三沙市', 3, NULL, '460000', '0', NULL, '2023-03-15 20:26:50', NULL, NULL);
INSERT INTO `sys_division` VALUES (1227, NULL, '460400', '儋州市', 4, NULL, '460000', '0', NULL, '2023-03-15 20:27:09', NULL, NULL);

-- ----------------------------
-- Table structure for sys_file_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info`  (
  `file_id` int NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `file_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '文件名称',
  `file_path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '文件路径',
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '文件信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_file_info
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logic_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_logic_dept`;
CREATE TABLE `sys_logic_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门Id',
  `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级部门Id',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '逻辑部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_logic_dept
-- ----------------------------
INSERT INTO `sys_logic_dept` VALUES (2, '226', '227', '0', 'admin', '2022-07-20 17:15:37', NULL, NULL);
INSERT INTO `sys_logic_dept` VALUES (3, '232', '227', '0', 'admin', '2022-07-20 17:15:43', NULL, NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '菜单图标',
  `app_id` bigint NULL DEFAULT NULL COMMENT '应用Id',
  `is_owner` varchar(5) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '是否自有菜单',
  `belong_appId` bigint NULL DEFAULT NULL COMMENT '所属微前端',
  `prefix_path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '微前端前缀',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 211283 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 3, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'menu-sysManage', -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2023-01-10 11:31:43', '系统管理目录');
INSERT INTO `sys_menu` VALUES (4, '组织机构', 0, 2, 'organ', NULL, '', 1, 0, 'M', '0', '0', '', 'menu-insFramework', -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2023-01-13 09:29:38', '系统管理目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 4, 1, 'user', 'system/user/User', NULL, 1, 0, 'C', '0', '0', 'system:user:list', NULL, -1, 'null', NULL, NULL, 'admin', '2022-03-14 16:32:41', '1', '2024-06-17 06:28:46', '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 4, 4, 'role', 'system/role/Role', NULL, 1, 0, 'C', '0', '0', 'system:role:list', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2022-07-19 16:47:54', '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 4, 7, 'menu', 'system/menu/Menu', NULL, 1, 0, 'C', '0', '0', 'system:menu:list', '', -1, 'null', NULL, NULL, 'admin', '2022-03-14 16:32:41', '1', '2024-06-17 06:43:45', '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 4, 2, 'dept', 'system/dept/Dept', NULL, 1, 0, 'C', '0', '0', 'system:dept:list', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2022-07-19 16:47:39', '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 4, 3, 'post', 'system/post/Post', NULL, 1, 0, 'C', '0', '0', 'system:post:list', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2022-07-19 16:47:47', '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 3, 'dict', 'system/dict/Dict', NULL, 1, 0, 'C', '0', '0', 'system:dict:list', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2022-08-02 10:07:02', '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 5, 'config', 'system/config/Config', NULL, 1, 0, 'C', '0', '0', 'system:config:list', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2022-08-02 10:07:12', '参数设置菜单');
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2022-03-24 09:42:53', '');
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2022-03-24 09:36:46', '');
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2022-03-24 09:30:07', '');
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2022-08-04 17:05:45', '');
INSERT INTO `sys_menu` VALUES (1018, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:list', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', 'admin', '2023-04-06 17:25:03', '');
INSERT INTO `sys_menu` VALUES (1022, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', NULL, -1, NULL, NULL, NULL, 'admin', '2022-03-14 16:32:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2376, '用户列表查询', 100, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:user:list', NULL, -1, NULL, NULL, NULL, 'admin', '2022-08-01 17:19:31', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2412, '部门导出', 103, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'system:dept:export', NULL, -1, NULL, NULL, NULL, '', NULL, '', NULL, '');
INSERT INTO `sys_menu` VALUES (2413, '部门导入', 103, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'system:dept:import', NULL, -1, NULL, NULL, NULL, '', NULL, '', NULL, '');
INSERT INTO `sys_menu` VALUES (2713, '字典分页查询', 105, 6, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'system:dict:list', NULL, -1, NULL, NULL, NULL, 'admin', '2022-12-28 10:34:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (13360, '密码设置', 1, 10, 'sysPassword', 'system/sys-password/SysPassword', NULL, 1, 0, 'C', '0', '0', 'system:sysPassword:list', '', -1, NULL, NULL, NULL, 'admin', '2023-04-24 14:12:40', 'admin', '2023-04-24 20:04:40', '密码设置菜单');
INSERT INTO `sys_menu` VALUES (13361, '密码设置查询', 13360, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:sysPassword:query', '', -1, NULL, NULL, NULL, 'admin', '2023-04-24 14:12:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (13362, '密码设置新增', 13360, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:sysPassword:add', '', -1, NULL, NULL, NULL, 'admin', '2023-04-24 14:12:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (13363, '密码设置修改', 13360, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:sysPassword:edit', '', -1, NULL, NULL, NULL, 'admin', '2023-04-24 14:12:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (13364, '密码设置删除', 13360, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:sysPassword:remove', '', -1, NULL, NULL, NULL, 'admin', '2023-04-24 14:12:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (13365, '密码设置导出', 13360, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:sysPassword:export', '', -1, NULL, NULL, NULL, 'admin', '2023-04-24 14:12:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111123, '审批中心', 0, 0, '/approve', NULL, NULL, 1, 0, 'M', '0', '0', NULL, '404', 40, '1', -1, NULL, '1', '2024-01-22 08:53:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111124, '报销流程', 111123, 0, '/bpmns/bpm/process-shell/finance', NULL, NULL, 1, 0, 'W', '0', '0', '', '404', 40, '1', -1, NULL, '1', '2024-01-22 08:55:12', '1', '2024-01-24 01:32:34', '');
INSERT INTO `sys_menu` VALUES (111125, '加班流程', 111123, 1, '/bpmns/bpm/process-shell/over_work', NULL, NULL, 1, 0, 'W', '0', '0', 'approve:work-overtime', '404', 40, '1', -1, NULL, '1', '2024-01-22 08:55:51', '1', '2024-01-22 08:58:12', '');
INSERT INTO `sys_menu` VALUES (111126, '请假流程', 111123, 2, '/bpmns/bpm/process-shell/test_level', NULL, NULL, 1, 0, 'W', '0', '0', 'approve:leave', '404', 40, '1', -1, NULL, '1', '2024-01-22 08:56:22', '1', '2024-01-22 08:59:36', '');
INSERT INTO `sys_menu` VALUES (111127, '补卡流程', 111123, 3, '/bpmns/bpm/process-shell/mend_clock', NULL, NULL, 1, 0, 'W', '0', '0', 'approve:mend-lock', '404', 40, '1', -1, NULL, '1', '2024-01-22 08:57:07', '1', '2024-01-22 09:01:16', '');
INSERT INTO `sys_menu` VALUES (111128, '办公', 0, 1, '/office', NULL, NULL, 1, 0, 'M', '0', '0', '', '404', 40, '0', 40, NULL, '1', '2024-01-22 10:26:59', '1', '2024-01-22 11:35:58', '');
INSERT INTO `sys_menu` VALUES (111129, '考勤打卡', 111128, 0, '/personnelclockin', NULL, NULL, 1, 0, 'W', '0', '0', NULL, NULL, 40, '1', 39, NULL, '1', '2024-01-22 10:27:59', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111178, '查看流程', 111124, 1, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:instance:detail', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:27:30', '1', '2024-01-24 01:31:43', '');
INSERT INTO `sys_menu` VALUES (111179, '查看流程', 111125, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:instance:detail', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:36:30', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111180, '查看流程', 111126, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:instance:detail', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:36:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111181, '查看流程', 111127, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:instance:detail', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:36:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111182, '审批流程', 111124, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:task:approveSelfFrom', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:40:57', '1', '2024-01-24 01:41:48', '');
INSERT INTO `sys_menu` VALUES (111183, '审批流程', 111125, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:task:approveSelfFrom', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:41:28', '1', '2024-01-24 01:41:41', '');
INSERT INTO `sys_menu` VALUES (111184, '审批流程', 111126, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:task:approveSelfFrom', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:42:01', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111185, '审批流程', 111127, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:task:approveSelfFrom', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:42:15', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111186, '查看流程定义', 111124, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:definition:resource', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:50:22', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111187, '查看流程定义', 111125, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:definition:resource', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:50:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111188, '查看流程定义', 111126, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:definition:resource', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:51:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111189, '查看流程定义', 111127, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:definition:resource', NULL, 40, '0', 40, NULL, '1', '2024-01-24 01:51:19', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111190, '删除运行时的流程实例', 111125, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:instance:revoke', NULL, 40, '0', 40, NULL, '1', '2024-01-24 02:21:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111191, '删除运行时的流程实例', 111126, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:instance:revoke', NULL, 40, '0', 40, NULL, '1', '2024-01-24 02:22:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (111192, '删除运行时的流程实例', 111127, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:instance:revoke', NULL, 40, '0', 40, NULL, '1', '2024-01-24 02:22:18', '1', '2024-01-24 02:22:56', '');
INSERT INTO `sys_menu` VALUES (211171, '离职流程', 111123, 7, '/bpmns/bpm/process-shell/resignation', NULL, NULL, 1, 0, 'W', '0', '0', '', NULL, 40, '1', -1, NULL, '1', '2024-05-16 09:15:15', '1', '2024-05-16 09:33:33', '');
INSERT INTO `sys_menu` VALUES (211172, '查看流程', 211171, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:instance:detail', NULL, 40, '0', 40, NULL, '1', '2024-05-16 09:31:02', '1', '2024-05-16 09:34:23', '');
INSERT INTO `sys_menu` VALUES (211173, '入职流程', 111123, 4, '/base/bpmns/bpm/process-shell/onboarding', NULL, NULL, 1, 0, 'W', '0', '0', '', NULL, 40, '0', 40, NULL, '1', '2024-05-16 09:32:36', '1', '2024-07-04 02:40:40', '');
INSERT INTO `sys_menu` VALUES (211174, '转正流程', 111123, 5, '/base/bpmns/bpm/process-shell/regularization', NULL, NULL, 1, 0, 'W', '0', '0', '', NULL, 40, '0', 40, NULL, '1', '2024-05-16 09:33:17', '1', '2024-05-16 09:33:43', '');
INSERT INTO `sys_menu` VALUES (211175, '流程审批', 211171, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:task:approveSelfFrom', NULL, 40, '0', 40, NULL, '1', '2024-05-16 09:34:19', '', NULL, '');
INSERT INTO `sys_menu` VALUES (211176, '流程删除', 211171, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:instance:revoke', NULL, 40, '0', 40, NULL, '1', '2024-05-16 09:34:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (211177, '查看定义', 211171, 0, NULL, NULL, NULL, 1, 0, 'F', '0', '0', 'bpm:definition:resource', NULL, 40, '0', 40, NULL, '1', '2024-05-16 09:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (211179, '用印申请', 111123, 8, '/base/bpmns/bpm/process-shell/seal', NULL, NULL, 1, 0, 'W', '0', '0', '', NULL, 40, '0', 40, NULL, '1', '2024-05-17 01:14:51', '1', '2024-05-17 01:20:42', '');

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `msg_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `msg_link` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '链接地址',
  `msg_type` tinyint NULL DEFAULT NULL COMMENT '业务模块,对应字典sys_message_type',
  `msg_content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容',
  `msg_status` tinyint NULL DEFAULT NULL COMMENT '0:未读 1:已读',
  `msg_userid` bigint NULL DEFAULT NULL,
  `create_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `update_time` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `remark` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `tanent_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_message
-- ----------------------------

-- ----------------------------
-- Table structure for sys_password
-- ----------------------------
DROP TABLE IF EXISTS `sys_password`;
CREATE TABLE `sys_password`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `single_login` tinyint(1) NULL DEFAULT NULL COMMENT '是否单一登录：0-否；1-是，默认0',
  `kick_type` tinyint(1) NULL DEFAULT NULL COMMENT '踢出类型：0-后登录踢出先登录；1-已登录禁止再登录；默认0',
  `strength_limit` tinyint(1) NULL DEFAULT NULL COMMENT '是否密码强度限制：0-否；1-是，默认0',
  `need_length` tinyint(1) NULL DEFAULT NULL COMMENT '是否需要最小长度限制：0-否；1-是，默认0',
  `min_length` tinyint(1) NULL DEFAULT NULL COMMENT '最小长度',
  `need_digit` tinyint(1) NULL DEFAULT NULL COMMENT '包含数字：0-否；1-是，默认0',
  `need_lower_case` tinyint(1) NULL DEFAULT NULL COMMENT '包含小写字母：0-否；1-是，默认0',
  `need_upper_case` tinyint(1) NULL DEFAULT NULL COMMENT '包含大写字母：0-否；1-是，默认0',
  `need_special_character` tinyint(1) NULL DEFAULT NULL COMMENT '包含特殊字符：0-否；1-是，默认0',
  `not_user_name` tinyint(1) NULL DEFAULT NULL COMMENT '不包含用户名：0-否；1-是，默认0',
  `force_change` tinyint(1) NULL DEFAULT NULL COMMENT '初始密码强制修改：0-否；1-是，默认0',
  `limit_failed_count` tinyint NULL DEFAULT NULL COMMENT '密码错误次数：0-不限制；取值范围0-10，默认-5',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varbinary(64) NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '密码设置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_password
-- ----------------------------
INSERT INTO `sys_password` VALUES (1, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 5, '1', '2023-05-03 22:13:44', 0x31, '2024-08-16 14:40:10', NULL);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '备注',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 312 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (303, '100101', '超级管理', 0, '0', '1', '2024-01-20 09:59:43', '1', '2024-05-11 03:40:18', NULL, 1);
INSERT INTO `sys_post` VALUES (304, '100102', '部门主管', 0, '0', '1', '2024-01-20 09:59:55', '1', '2024-07-31 07:21:40', NULL, 1);
INSERT INTO `sys_post` VALUES (305, '100103', '项目管理', 0, '0', '1', '2024-01-20 10:00:11', '1', '2024-07-30 01:08:50', NULL, 1);
INSERT INTO `sys_post` VALUES (306, '100104', '普通成员', 0, '0', '1', '2024-01-20 10:00:41', '1', '2024-07-30 01:08:17', NULL, 1);
INSERT INTO `sys_post` VALUES (307, '100105', '行政管理', 0, '0', '1', '2024-01-20 10:00:57', '1', '2024-07-30 01:06:38', NULL, 1);
INSERT INTO `sys_post` VALUES (311, 'A', 'A', 0, '0', '1', '2024-08-16 06:42:27', '', NULL, NULL, 1);

-- ----------------------------
-- Table structure for sys_post_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_post_role`;
CREATE TABLE `sys_post_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `post_id` bigint NULL DEFAULT NULL COMMENT '岗位Id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色Id',
  `app_id` bigint NULL DEFAULT NULL COMMENT '应用Id',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 270 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post_role
-- ----------------------------
INSERT INTO `sys_post_role` VALUES (178, 306, 186, 40, '1', NULL, NULL, NULL);
INSERT INTO `sys_post_role` VALUES (179, 306, 189, 40, '1', NULL, NULL, NULL);
INSERT INTO `sys_post_role` VALUES (186, 303, 186, 40, '1', '2024-01-22 20:27:10', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (187, 304, 186, 40, '1', '2024-01-22 20:27:10', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (188, 305, 186, 40, '1', '2024-01-22 20:27:10', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (189, 307, 186, 40, '1', '2024-01-22 20:27:10', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (195, 303, 189, 40, '1', '2024-01-22 20:28:43', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (196, 304, 189, 40, '1', '2024-01-22 20:28:43', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (197, 305, 189, 40, '1', '2024-01-22 20:28:43', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (198, 307, 189, 40, '1', '2024-01-22 20:28:43', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (214, 303, 151, -1, '1', NULL, NULL, NULL);
INSERT INTO `sys_post_role` VALUES (264, 303, 200, -1, '1', '2024-08-16 14:42:42', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (265, 304, 200, -1, '1', '2024-08-16 14:42:42', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (266, 305, 200, -1, '1', '2024-08-16 14:42:42', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (267, 306, 200, -1, '1', '2024-08-16 14:42:42', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (268, 307, 200, -1, '1', '2024-08-16 14:42:42', NULL, NULL);
INSERT INTO `sys_post_role` VALUES (269, 311, 200, -1, '1', '2024-08-16 14:42:42', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '备注',
  `app_id` bigint NULL DEFAULT NULL COMMENT '应用Id',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 201 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2022-03-14 16:32:41', '', NULL, '超级管理员', -1);
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 0, 0, '0', '0', 'admin', '2022-03-14 16:32:41', 'admin', '2022-06-01 09:46:11', '普通角色', -1);
INSERT INTO `sys_role` VALUES (100, '管理员', '@PreAuthorize(`@ss.hasRole(\'admin\')`)', 3, '1', 1, 1, '0', '0', 'admin', '2022-03-15 10:23:42', 'admin', '2022-05-26 15:20:38', NULL, -1);
INSERT INTO `sys_role` VALUES (151, '普通管理员', 'admin-normal', 0, '1', 1, 1, '0', '0', 'admin', '2022-08-11 18:01:54', 'admin', '2022-08-12 10:52:25', NULL, -1);
INSERT INTO `sys_role` VALUES (186, 'fiz-流程', 'fiztask-process', 0, '1', 1, 1, '0', '0', '1', '2024-01-22 09:11:47', '', NULL, NULL, 40);
INSERT INTO `sys_role` VALUES (189, '考勤', 'fiz-cchekin', 1, '1', 1, 1, '0', '0', '1', '2024-01-22 11:41:29', '', NULL, NULL, 40);
INSERT INTO `sys_role` VALUES (200, 'A', 'A', 0, '1', 1, 1, '0', '0', '1', '2024-08-16 06:42:33', '', NULL, NULL, -1);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (100, 1);
INSERT INTO `sys_role_menu` VALUES (100, 100);
INSERT INTO `sys_role_menu` VALUES (100, 101);
INSERT INTO `sys_role_menu` VALUES (100, 102);
INSERT INTO `sys_role_menu` VALUES (100, 103);
INSERT INTO `sys_role_menu` VALUES (100, 104);
INSERT INTO `sys_role_menu` VALUES (100, 105);
INSERT INTO `sys_role_menu` VALUES (100, 106);
INSERT INTO `sys_role_menu` VALUES (100, 1001);
INSERT INTO `sys_role_menu` VALUES (100, 1002);
INSERT INTO `sys_role_menu` VALUES (100, 1003);
INSERT INTO `sys_role_menu` VALUES (100, 1004);
INSERT INTO `sys_role_menu` VALUES (100, 1005);
INSERT INTO `sys_role_menu` VALUES (100, 1006);
INSERT INTO `sys_role_menu` VALUES (100, 1007);
INSERT INTO `sys_role_menu` VALUES (100, 1008);
INSERT INTO `sys_role_menu` VALUES (100, 1009);
INSERT INTO `sys_role_menu` VALUES (100, 1010);
INSERT INTO `sys_role_menu` VALUES (100, 1011);
INSERT INTO `sys_role_menu` VALUES (100, 1012);
INSERT INTO `sys_role_menu` VALUES (100, 1013);
INSERT INTO `sys_role_menu` VALUES (100, 1014);
INSERT INTO `sys_role_menu` VALUES (100, 1015);
INSERT INTO `sys_role_menu` VALUES (100, 1016);
INSERT INTO `sys_role_menu` VALUES (100, 1017);
INSERT INTO `sys_role_menu` VALUES (100, 1018);
INSERT INTO `sys_role_menu` VALUES (100, 1019);
INSERT INTO `sys_role_menu` VALUES (100, 1020);
INSERT INTO `sys_role_menu` VALUES (100, 1021);
INSERT INTO `sys_role_menu` VALUES (100, 1022);
INSERT INTO `sys_role_menu` VALUES (100, 1023);
INSERT INTO `sys_role_menu` VALUES (100, 1024);
INSERT INTO `sys_role_menu` VALUES (100, 1025);
INSERT INTO `sys_role_menu` VALUES (100, 1026);
INSERT INTO `sys_role_menu` VALUES (100, 1027);
INSERT INTO `sys_role_menu` VALUES (100, 1028);
INSERT INTO `sys_role_menu` VALUES (100, 1029);
INSERT INTO `sys_role_menu` VALUES (100, 1030);
INSERT INTO `sys_role_menu` VALUES (100, 1031);
INSERT INTO `sys_role_menu` VALUES (100, 1032);
INSERT INTO `sys_role_menu` VALUES (100, 1033);
INSERT INTO `sys_role_menu` VALUES (100, 1034);
INSERT INTO `sys_role_menu` VALUES (100, 1035);
INSERT INTO `sys_role_menu` VALUES (100, 2413);
INSERT INTO `sys_role_menu` VALUES (151, 1);
INSERT INTO `sys_role_menu` VALUES (151, 4);
INSERT INTO `sys_role_menu` VALUES (151, 100);
INSERT INTO `sys_role_menu` VALUES (151, 101);
INSERT INTO `sys_role_menu` VALUES (151, 102);
INSERT INTO `sys_role_menu` VALUES (151, 103);
INSERT INTO `sys_role_menu` VALUES (151, 104);
INSERT INTO `sys_role_menu` VALUES (151, 105);
INSERT INTO `sys_role_menu` VALUES (151, 106);
INSERT INTO `sys_role_menu` VALUES (151, 1001);
INSERT INTO `sys_role_menu` VALUES (151, 1002);
INSERT INTO `sys_role_menu` VALUES (151, 1003);
INSERT INTO `sys_role_menu` VALUES (151, 1004);
INSERT INTO `sys_role_menu` VALUES (151, 1005);
INSERT INTO `sys_role_menu` VALUES (151, 1006);
INSERT INTO `sys_role_menu` VALUES (151, 1007);
INSERT INTO `sys_role_menu` VALUES (151, 1008);
INSERT INTO `sys_role_menu` VALUES (151, 1009);
INSERT INTO `sys_role_menu` VALUES (151, 1010);
INSERT INTO `sys_role_menu` VALUES (151, 1011);
INSERT INTO `sys_role_menu` VALUES (151, 1012);
INSERT INTO `sys_role_menu` VALUES (151, 1013);
INSERT INTO `sys_role_menu` VALUES (151, 1014);
INSERT INTO `sys_role_menu` VALUES (151, 1015);
INSERT INTO `sys_role_menu` VALUES (151, 1016);
INSERT INTO `sys_role_menu` VALUES (151, 1017);
INSERT INTO `sys_role_menu` VALUES (151, 1018);
INSERT INTO `sys_role_menu` VALUES (151, 1019);
INSERT INTO `sys_role_menu` VALUES (151, 1020);
INSERT INTO `sys_role_menu` VALUES (151, 1021);
INSERT INTO `sys_role_menu` VALUES (151, 1022);
INSERT INTO `sys_role_menu` VALUES (151, 1023);
INSERT INTO `sys_role_menu` VALUES (151, 1024);
INSERT INTO `sys_role_menu` VALUES (151, 1025);
INSERT INTO `sys_role_menu` VALUES (151, 1026);
INSERT INTO `sys_role_menu` VALUES (151, 1027);
INSERT INTO `sys_role_menu` VALUES (151, 1028);
INSERT INTO `sys_role_menu` VALUES (151, 1029);
INSERT INTO `sys_role_menu` VALUES (151, 1030);
INSERT INTO `sys_role_menu` VALUES (151, 1031);
INSERT INTO `sys_role_menu` VALUES (151, 1032);
INSERT INTO `sys_role_menu` VALUES (151, 1033);
INSERT INTO `sys_role_menu` VALUES (151, 1034);
INSERT INTO `sys_role_menu` VALUES (151, 1035);
INSERT INTO `sys_role_menu` VALUES (151, 2376);
INSERT INTO `sys_role_menu` VALUES (151, 2412);
INSERT INTO `sys_role_menu` VALUES (151, 2413);
INSERT INTO `sys_role_menu` VALUES (186, 111123);
INSERT INTO `sys_role_menu` VALUES (186, 111124);
INSERT INTO `sys_role_menu` VALUES (186, 111125);
INSERT INTO `sys_role_menu` VALUES (186, 111126);
INSERT INTO `sys_role_menu` VALUES (186, 111127);
INSERT INTO `sys_role_menu` VALUES (186, 111178);
INSERT INTO `sys_role_menu` VALUES (186, 111182);
INSERT INTO `sys_role_menu` VALUES (186, 111183);
INSERT INTO `sys_role_menu` VALUES (186, 111184);
INSERT INTO `sys_role_menu` VALUES (186, 111185);
INSERT INTO `sys_role_menu` VALUES (186, 111186);
INSERT INTO `sys_role_menu` VALUES (186, 111187);
INSERT INTO `sys_role_menu` VALUES (186, 111188);
INSERT INTO `sys_role_menu` VALUES (186, 111189);
INSERT INTO `sys_role_menu` VALUES (186, 111190);
INSERT INTO `sys_role_menu` VALUES (186, 111191);
INSERT INTO `sys_role_menu` VALUES (186, 111192);
INSERT INTO `sys_role_menu` VALUES (186, 211171);
INSERT INTO `sys_role_menu` VALUES (186, 211172);
INSERT INTO `sys_role_menu` VALUES (186, 211173);
INSERT INTO `sys_role_menu` VALUES (186, 211174);
INSERT INTO `sys_role_menu` VALUES (186, 211179);
INSERT INTO `sys_role_menu` VALUES (189, 111128);
INSERT INTO `sys_role_menu` VALUES (189, 111129);

-- ----------------------------
-- Table structure for sys_tanent
-- ----------------------------
DROP TABLE IF EXISTS `sys_tanent`;
CREATE TABLE `sys_tanent`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tanent_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户code',
  `tanent_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户名',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户状态',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tanent
-- ----------------------------

-- ----------------------------
-- Table structure for sys_theme
-- ----------------------------
DROP TABLE IF EXISTS `sys_theme`;
CREATE TABLE `sys_theme`  (
  `theme_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主题主键',
  `theme_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题名称',
  `theme_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '主题配置',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `theme_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题类型',
  PRIMARY KEY (`theme_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统主题表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_theme
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `default_pwd_flag` tinyint NOT NULL DEFAULT 1 COMMENT '初始密码是否修改：0-已修改；1-未修改，默认1',
  `login_ip` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '备注',
  `ext1` bigint NULL DEFAULT NULL COMMENT '扩展字段-齐成',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21558 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 100, 'admin', '超级管理员', '00', 'test001@soflyit.com', '15800008001', '0', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 0, '127.0.0.1', NULL, '1', '2022-03-14 16:32:41', '', '2023-05-03 22:46:56', '管理员', 1);
INSERT INTO `sys_user` VALUES (21438, 479, 'A20001', '张伟', '00', 'test002@soflyit.com', '15800008002', '0', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 0, '', NULL, '', '2024-01-20 06:55:48', '1', '2024-07-31 07:30:25', NULL, NULL);
INSERT INTO `sys_user` VALUES (21439, 472, 'A20002', '李娜', '00', 'test003@soflyit.com', '15800008003', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 0, '', NULL, '', '2024-01-20 06:55:49', '21454', '2024-08-05 00:43:15', NULL, NULL);
INSERT INTO `sys_user` VALUES (21440, 488, 'A20003', '刘洋', '00', 'test004@soflyit.com', '15800008004', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 1, '', NULL, '', '2024-01-20 06:55:50', '21454', '2024-07-24 09:14:35', NULL, NULL);
INSERT INTO `sys_user` VALUES (21441, 488, 'A20004', '陈静', '00', 'test005@soflyit.com', '15800008005', '0', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 1, '', NULL, '', '2024-01-20 06:55:51', '1', '2024-08-08 08:18:04', NULL, NULL);
INSERT INTO `sys_user` VALUES (21442, 491, 'A20005', '杨明', '00', 'test006@soflyit.com', '15800008006', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 0, '', NULL, '', '2024-01-20 06:55:52', '1', '2024-08-08 08:18:57', NULL, NULL);
INSERT INTO `sys_user` VALUES (21443, 490, 'A20006', '赵丽', '00', 'test007@soflyit.com', '15800008007', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 0, '', NULL, '', '2024-01-20 06:55:53', '1', '2024-08-08 08:20:11', NULL, NULL);
INSERT INTO `sys_user` VALUES (21444, 488, 'A20007', '黄强', '00', 'test008@soflyit.com', '15800008008', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 1, '', NULL, '', '2024-01-20 06:56:13', '21454', '2024-07-24 09:21:49', NULL, NULL);
INSERT INTO `sys_user` VALUES (21445, 490, 'A20008', '周涛', '00', 'test009@soflyit.com', '15800008009', '2', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 1, '', NULL, '', '2024-01-20 06:56:14', '21454', '2024-07-24 09:22:20', NULL, NULL);
INSERT INTO `sys_user` VALUES (21446, 485, 'A20009', '吴敏', '00', 'test010@soflyit.com', '15800008010', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 0, '', NULL, '', '2024-01-20 06:56:15', '21454', '2024-07-24 09:22:38', NULL, NULL);
INSERT INTO `sys_user` VALUES (21447, 491, 'A20010', '徐翔', '00', 'test011@soflyit.com', '15800008011', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 1, '', NULL, '', '2024-01-20 06:57:19', '21454', '2024-07-24 09:22:53', NULL, NULL);
INSERT INTO `sys_user` VALUES (21448, 476, 'A20011', '孙悦', '00', 'test012@soflyit.com', '15800008012', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 1, '', NULL, '', '2024-01-20 06:57:20', '21454', '2024-07-24 09:23:28', NULL, NULL);
INSERT INTO `sys_user` VALUES (21449, 485, 'A20012', '朱华', '00', 'test013@soflyit.com', '15800008013', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 1, '', NULL, '', '2024-01-20 06:57:21', '21454', '2024-08-05 00:44:29', NULL, NULL);
INSERT INTO `sys_user` VALUES (21450, 491, 'A20013', '胡兵', '00', 'test014@soflyit.com', '15800008014', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 1, '', NULL, '', '2024-01-20 06:57:22', '21454', '2024-07-24 09:23:57', NULL, NULL);
INSERT INTO `sys_user` VALUES (21451, 488, 'A20014', '郭瑾', '00', 'test015@soflyit.com', '15800008015', '2', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 1, '', NULL, '', '2024-01-20 06:57:23', '21454', '2024-07-24 09:24:17', NULL, NULL);
INSERT INTO `sys_user` VALUES (21452, 490, 'A20015', '何鹏', '00', 'test016@soflyit.com', '15800008016', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 0, '', NULL, '', '2024-01-20 06:57:24', '21454', '2024-07-24 09:24:36', NULL, NULL);
INSERT INTO `sys_user` VALUES (21453, 100, 'A20016', '高露', '00', 'test017@soflyit.com', '15800008017', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 0, '', NULL, '', '2024-01-20 07:01:37', '21454', '2024-07-24 09:24:53', NULL, NULL);
INSERT INTO `sys_user` VALUES (21454, 469, 'A20017', '林浩', '00', 'test018@soflyit.com', '15800008018', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 0, '', NULL, '', '2024-01-20 07:01:38', '21454', '2024-07-24 09:25:12', NULL, NULL);
INSERT INTO `sys_user` VALUES (21455, 470, 'A20018', '罗燕', '00', 'test019@soflyit.com', '15800008019', '2', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 0, '', NULL, '', '2024-01-20 07:01:39', '21454', '2024-07-24 09:25:26', NULL, NULL);
INSERT INTO `sys_user` VALUES (21456, 488, 'A20019', '郑洁', '00', 'test020@soflyit.com', '15800008020', '1', NULL, '$2a$10$ZgoMTdS/FTOvTO8DDEm69ebxk69ncEDa8dtrO.4wYcVNsNOpqL6VG', '0', '0', 1, '', NULL, '', '2024-01-20 07:01:40', '21454', '2024-07-24 09:25:51', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  `dept_id` bigint NOT NULL COMMENT '部门Id',
  PRIMARY KEY (`user_id`, `post_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 43, 100);
INSERT INTO `sys_user_post` VALUES (1, 303, 100);
INSERT INTO `sys_user_post` VALUES (1, 304, 100);
INSERT INTO `sys_user_post` VALUES (1, 306, 100);
INSERT INTO `sys_user_post` VALUES (21438, 304, 479);
INSERT INTO `sys_user_post` VALUES (21438, 305, 479);
INSERT INTO `sys_user_post` VALUES (21438, 305, 491);
INSERT INTO `sys_user_post` VALUES (21439, 304, 472);
INSERT INTO `sys_user_post` VALUES (21439, 305, 485);
INSERT INTO `sys_user_post` VALUES (21440, 306, 488);
INSERT INTO `sys_user_post` VALUES (21441, 305, 488);
INSERT INTO `sys_user_post` VALUES (21442, 304, 476);
INSERT INTO `sys_user_post` VALUES (21442, 304, 491);
INSERT INTO `sys_user_post` VALUES (21443, 306, 480);
INSERT INTO `sys_user_post` VALUES (21443, 306, 490);
INSERT INTO `sys_user_post` VALUES (21444, 306, 473);
INSERT INTO `sys_user_post` VALUES (21444, 306, 488);
INSERT INTO `sys_user_post` VALUES (21445, 306, 480);
INSERT INTO `sys_user_post` VALUES (21445, 306, 490);
INSERT INTO `sys_user_post` VALUES (21446, 306, 472);
INSERT INTO `sys_user_post` VALUES (21446, 306, 485);
INSERT INTO `sys_user_post` VALUES (21447, 306, 479);
INSERT INTO `sys_user_post` VALUES (21447, 306, 491);
INSERT INTO `sys_user_post` VALUES (21448, 306, 476);
INSERT INTO `sys_user_post` VALUES (21449, 306, 485);
INSERT INTO `sys_user_post` VALUES (21450, 306, 491);
INSERT INTO `sys_user_post` VALUES (21451, 306, 488);
INSERT INTO `sys_user_post` VALUES (21452, 306, 479);
INSERT INTO `sys_user_post` VALUES (21452, 306, 490);
INSERT INTO `sys_user_post` VALUES (21453, 303, 100);
INSERT INTO `sys_user_post` VALUES (21453, 304, 100);
INSERT INTO `sys_user_post` VALUES (21454, 304, 469);
INSERT INTO `sys_user_post` VALUES (21455, 304, 470);
INSERT INTO `sys_user_post` VALUES (21456, 306, 473);
INSERT INTO `sys_user_post` VALUES (21456, 306, 480);
INSERT INTO `sys_user_post` VALUES (21456, 306, 488);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (1, 2);
INSERT INTO `sys_user_role` VALUES (1, 103);
INSERT INTO `sys_user_role` VALUES (1, 112);

-- ----------------------------
-- View structure for sys_dept_list
-- ----------------------------
DROP VIEW IF EXISTS `sys_dept_list`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `sys_dept_list` AS select `t`.`dept_id` AS `dept_id`,`t`.`ids` AS `ids`,`d0`.`dept_name` AS `deptName0`,`d1`.`dept_name` AS `deptName1`,`d2`.`dept_name` AS `deptName2`,`d3`.`dept_name` AS `deptName3` from (((((select `tmp`.`dept_id` AS `dept_id`,`tmp`.`dept_name` AS `dept_name`,`tmp`.`ids` AS `ids`,concat(sum(`tmp`.`id0`),'') AS `id0`,concat(sum(`tmp`.`id1`),'') AS `id1`,concat(sum(`tmp`.`id2`),'') AS `id2`,concat(sum(`tmp`.`id3`),'') AS `id3` from (select `d`.`dept_id` AS `dept_id`,`d`.`dept_name` AS `dept_name`,`d`.`ids` AS `ids`,`n`.`digit` AS `digit`,(case `n`.`digit` when 0 then (select substring_index(substring_index(`d`.`ids`,',',(`n`.`digit` + 1)),',',-(1))) end) AS `id0`,(case `n`.`digit` when 1 then (select substring_index(substring_index(`d`.`ids`,',',(`n`.`digit` + 1)),',',-(1))) end) AS `id1`,(case `n`.`digit` when 2 then (select substring_index(substring_index(`d`.`ids`,',',(`n`.`digit` + 1)),',',-(1))) end) AS `id2`,(case `n`.`digit` when 3 then (select substring_index(substring_index(`d`.`ids`,',',(`n`.`digit` + 1)),',',-(1))) end) AS `id3` from ((select `sys_dept`.`dept_id` AS `dept_id`,`sys_dept`.`dept_name` AS `dept_name`,concat(`sys_dept`.`ancestors`,',',`sys_dept`.`dept_id`) AS `ids` from `sys_dept`) `d` join (select 0 AS `digit` union all select 1 AS `1` union all select 2 AS `2` union all select 3 AS `3`) `n`) where ((length(`d`.`ids`) - length(replace(`d`.`ids`,',',''))) >= `n`.`digit`)) `tmp` group by `tmp`.`ids`) `t` left join `sys_dept` `d0` on((`t`.`id0` = `d0`.`dept_id`))) left join `sys_dept` `d1` on((`t`.`id1` = `d1`.`dept_id`))) left join `sys_dept` `d2` on((`t`.`id2` = `d2`.`dept_id`))) left join `sys_dept` `d3` on((`t`.`id3` = `d3`.`dept_id`)));

SET FOREIGN_KEY_CHECKS = 1;
