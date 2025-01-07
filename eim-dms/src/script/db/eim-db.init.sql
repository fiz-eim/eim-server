/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.3.52
 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Host           : 192.168.3.52:3306
 Source Schema         : eim_dms

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 24/12/2024 14:17:48
*/

create database if not exists `eim_dms` CHARACTER SET 'utf8mb4';
use  `eim_dms`;

GRANT Alter, Alter Routine, Create, Create Routine, Create Temporary Tables, Create View, Delete, Drop, Event, Execute, Index, Insert, Lock Tables, References, Select, Show View, Trigger, Update ON `eim\_dms`.* TO `fizeim`@`%`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dx_file_exe_adapt
-- ----------------------------
DROP TABLE IF EXISTS `dx_file_exe_adapt`;
CREATE TABLE `dx_file_exe_adapt`  (
  `exe_adapt_id` bigint NOT NULL COMMENT '主键',
  `exe_adapt_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '适配工具名称;如：文本打开工具',
  `exe_adapt_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '可打开文件类型;[doc,docx,txt]',
  `exe_adapt_api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行操作API;/office/word/open/{resouce_id}；/soft/wps/open/{resouce_id}',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '删除标记;1.删除；2.正常',
  PRIMARY KEY (`exe_adapt_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件打开适配器' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_file_exe_adapt
-- ----------------------------

-- ----------------------------
-- Table structure for dx_file_share
-- ----------------------------
DROP TABLE IF EXISTS `dx_file_share`;
CREATE TABLE `dx_file_share`  (
  `share_id` bigint NOT NULL COMMENT '主键;分享主键ID',
  `share_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分享主题',
  `share_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分享内容的状态;0.正常;-1.过期；-2：文件不存在等',
  `share_pass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分享需要密码验证;为空时，不需要；不为空则需要',
  `share_end_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分享截止时间;为空时，可不限时长分享；不为空进行时长限定',
  `folder_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录ID',
  `resource_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源ID',
  `share_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分享类型;folder:目录；file:文件等',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '删除标记',
  PRIMARY KEY (`share_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件分享' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_file_share
-- ----------------------------

-- ----------------------------
-- Table structure for dx_file_type
-- ----------------------------
DROP TABLE IF EXISTS `dx_file_type`;
CREATE TABLE `dx_file_type`  (
  `file_type_id` bigint NOT NULL COMMENT '主键',
  `file_type_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件分类',
  `file_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件描述',
  `file_type_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型细分',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '删除标记',
  PRIMARY KEY (`file_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件分享' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_file_type
-- ----------------------------
INSERT INTO `dx_file_type` VALUES (1, 'video', '视频', 'mp4', NULL, NULL, NULL, 1, NULL, NULL, NULL, '2');
INSERT INTO `dx_file_type` VALUES (2, 'video', '视频', 'avi', NULL, NULL, NULL, 1, NULL, NULL, NULL, '2');
INSERT INTO `dx_file_type` VALUES (3, 'video', '视频', 'mov', NULL, NULL, NULL, 1, NULL, NULL, NULL, '2');
INSERT INTO `dx_file_type` VALUES (4, 'video', '视频', 'flv', NULL, NULL, NULL, 1, NULL, NULL, NULL, '2');
INSERT INTO `dx_file_type` VALUES (6, 'txt', '文本', 'txt', NULL, NULL, NULL, 1, NULL, NULL, NULL, '2');
INSERT INTO `dx_file_type` VALUES (7, 'txt', '文本', 'ini', NULL, NULL, NULL, 1, NULL, NULL, NULL, '2');

-- ----------------------------
-- Table structure for dx_file_version
-- ----------------------------
DROP TABLE IF EXISTS `dx_file_version`;
CREATE TABLE `dx_file_version`  (
  `file_version_id` bigint NOT NULL COMMENT '主键',
  `merge_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '重复文件ID',
  `folder_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目录ID;目录ID，',
  `resource_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源ID;资源ID，关联字段resource_id字段进行文件定位，可更改文件状态，进行版本回退',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` tinyint NOT NULL COMMENT '删除标记：1.删除；2.正常',
  PRIMARY KEY (`file_version_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件版本' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_file_version
-- ----------------------------

-- ----------------------------
-- Table structure for dx_folder
-- ----------------------------
DROP TABLE IF EXISTS `dx_folder`;
CREATE TABLE `dx_folder`  (
  `FOLDER_ID` bigint NOT NULL COMMENT '主键',
  `FOLDER_NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录名称',
  `FOLDER_FlAG` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件夹标记',
  `FILE_TOTAL` bigint NULL DEFAULT NULL COMMENT '文件树',
  `FOLDER_LEVEL` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '层级;格式：[FOLDER_ID1,FOLDER_ID2,FOLDER_ID3,FOLDER_ID4]',
  `FOLDER_PARENT_ID` bigint NULL DEFAULT NULL COMMENT '上级目录ID;默认规范 ： 0为顶级目录',
  `FOLDER_TOTAL` bigint NULL DEFAULT NULL COMMENT '目录数;子级目录统计数：是否有必要维护？',
  `REMARK` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注;备注说明',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人;创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间;创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人;修改人ID',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间;最新修改时间',
  `DELETE_FLAG` tinyint NOT NULL COMMENT '删除标记;删除标记：1：删除；2：正常',
  PRIMARY KEY (`FOLDER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文档库存储目录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_folder
-- ----------------------------
INSERT INTO `dx_folder` VALUES (0, '顶层文件夹', 'root', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2);
INSERT INTO `dx_folder` VALUES (1, '最近使用', 'recent', NULL, '[0,1]', 0, NULL, NULL, NULL, NULL, 1, '2023-12-19 14:50:31', NULL, NULL, 2);
INSERT INTO `dx_folder` VALUES (2, '我的文档', 'home', NULL, '[0,2]', 0, NULL, NULL, NULL, NULL, 1, '2023-12-19 14:51:28', NULL, NULL, 2);
INSERT INTO `dx_folder` VALUES (3, '文件库', 'group', NULL, '[0,3]', 0, NULL, NULL, NULL, NULL, 1, '2023-12-19 14:51:40', NULL, NULL, 2);

-- ----------------------------
-- Table structure for dx_menu
-- ----------------------------
DROP TABLE IF EXISTS `dx_menu`;
CREATE TABLE `dx_menu`  (
  `dxm_id` bigint NOT NULL COMMENT '主键',
  `dxm_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录删除时使用的关联字段',
  `dxm_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `dxm_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单类型chat,task',
  `permission_type` tinyint NULL DEFAULT NULL COMMENT '权限类型1私人2公共3系统',
  `is_show` tinyint NULL DEFAULT NULL COMMENT '是否展示',
  `is_sys` tinyint NULL DEFAULT NULL COMMENT '是否系统级1是2否',
  `REMARK` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注;备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` tinyint NOT NULL COMMENT '删除标记位;删除标记：1：删除；2：正常',
  PRIMARY KEY (`dxm_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '顶级菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_menu
-- ----------------------------
INSERT INTO `dx_menu` VALUES (1, '最近使用', NULL, 'recent', 1, 1, 1, NULL, NULL, NULL, 1, '2023-12-19 14:50:31', NULL, NULL, 2);
INSERT INTO `dx_menu` VALUES (2, '我的文档', NULL, 'home', 1, 1, 1, NULL, NULL, NULL, 1, '2023-12-19 14:51:28', NULL, NULL, 2);
INSERT INTO `dx_menu` VALUES (3, '文件库', NULL, 'group', 2, 1, 1, NULL, NULL, NULL, 1, '2023-12-19 14:51:40', NULL, NULL, 2);
INSERT INTO `dx_menu` VALUES (4, '我的收藏', NULL, 'collect', 1, 1, 1, NULL, NULL, NULL, 1, '2023-12-19 14:51:54', 1, '2023-12-23 10:03:58', 1);
INSERT INTO `dx_menu` VALUES (5, '回收站', NULL, 'recycle', 1, 1, 1, NULL, NULL, NULL, 1, '2023-12-19 14:52:05', 1, '2023-12-23 13:43:02', 1);

-- ----------------------------
-- Table structure for dx_my_folder
-- ----------------------------
DROP TABLE IF EXISTS `dx_my_folder`;
CREATE TABLE `dx_my_folder`  (
  `ID` bigint NOT NULL COMMENT '主键',
  `FOLDER_ID` bigint NULL DEFAULT NULL,
  `USER_ID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `REMARK` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '删除标记;删除标记：1：删除；2：正常',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '我的文档' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_my_folder
-- ----------------------------
INSERT INTO `dx_my_folder` VALUES (1737004168557760512, 3, '1', NULL, NULL, NULL, 1, '2023-12-19 14:57:20', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1737004169321123840, 1, '1', NULL, NULL, NULL, 1, '2023-12-19 14:57:21', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1737004169715388416, 2, '1', NULL, NULL, NULL, 1, '2023-12-19 14:57:21', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655903634231296, 3, '21438', NULL, NULL, NULL, 0, '2024-01-20 18:37:11', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655905500696576, 1, '21438', NULL, NULL, NULL, 0, '2024-01-20 18:37:11', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655905924321280, 2, '21438', NULL, NULL, NULL, 0, '2024-01-20 18:37:11', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655907157446656, 3, '21439', NULL, NULL, NULL, 0, '2024-01-20 18:37:11', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655908327657472, 1, '21439', NULL, NULL, NULL, 0, '2024-01-20 18:37:12', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655908671590400, 2, '21439', NULL, NULL, NULL, 0, '2024-01-20 18:37:12', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655909757915136, 3, '21440', NULL, NULL, NULL, 0, '2024-01-20 18:37:12', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655911263670272, 1, '21440', NULL, NULL, NULL, 0, '2024-01-20 18:37:12', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655911888621568, 2, '21440', NULL, NULL, NULL, 0, '2024-01-20 18:37:13', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655913172078592, 3, '21441', NULL, NULL, NULL, 0, '2024-01-20 18:37:13', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655915114041344, 1, '21441', NULL, NULL, NULL, 0, '2024-01-20 18:37:13', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655915499917312, 2, '21441', NULL, NULL, NULL, 0, '2024-01-20 18:37:13', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655917106335744, 3, '21442', NULL, NULL, NULL, 0, '2024-01-20 18:37:14', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655918083608576, 1, '21442', NULL, NULL, NULL, 0, '2024-01-20 18:37:14', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655918716948480, 2, '21442', NULL, NULL, NULL, 0, '2024-01-20 18:37:14', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655919652278272, 3, '21443', NULL, NULL, NULL, 0, '2024-01-20 18:37:14', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655922026254336, 1, '21443', NULL, NULL, NULL, 0, '2024-01-20 18:37:15', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655922596679680, 2, '21443', NULL, NULL, NULL, 0, '2024-01-20 18:37:15', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655924194709504, 3, '21444', NULL, NULL, NULL, 0, '2024-01-20 18:37:15', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655925872431104, 1, '21444', NULL, NULL, NULL, 0, '2024-01-20 18:37:16', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655926509965312, 2, '21444', NULL, NULL, NULL, 0, '2024-01-20 18:37:16', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655927764062208, 3, '21445', NULL, NULL, NULL, 0, '2024-01-20 18:37:16', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655928917495808, 1, '21445', NULL, NULL, NULL, 0, '2024-01-20 18:37:17', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655929227874304, 2, '21445', NULL, NULL, NULL, 0, '2024-01-20 18:37:17', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655930133843968, 3, '21446', NULL, NULL, NULL, 0, '2024-01-20 18:37:17', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655931203391488, 1, '21446', NULL, NULL, NULL, 0, '2024-01-20 18:37:17', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655931643793408, 2, '21446', NULL, NULL, NULL, 0, '2024-01-20 18:37:17', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655933308932096, 3, '21447', NULL, NULL, NULL, 0, '2024-01-20 18:37:18', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655935238311936, 1, '21447', NULL, NULL, NULL, 0, '2024-01-20 18:37:18', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655935930372096, 2, '21447', NULL, NULL, NULL, 0, '2024-01-20 18:37:18', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655937385795584, 3, '21448', NULL, NULL, NULL, 0, '2024-01-20 18:37:19', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655939059322880, 1, '21448', NULL, NULL, NULL, 0, '2024-01-20 18:37:19', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655939889795072, 2, '21448', NULL, NULL, NULL, 0, '2024-01-20 18:37:19', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655941118726144, 3, '21449', NULL, NULL, NULL, 0, '2024-01-20 18:37:19', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655942347657216, 1, '21449', NULL, NULL, NULL, 0, '2024-01-20 18:37:20', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655942695784448, 2, '21449', NULL, NULL, NULL, 0, '2024-01-20 18:37:20', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655944394477568, 3, '21450', NULL, NULL, NULL, 0, '2024-01-20 18:37:20', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655946265137152, 1, '21450', NULL, NULL, NULL, 0, '2024-01-20 18:37:21', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655946781036544, 2, '21450', NULL, NULL, NULL, 0, '2024-01-20 18:37:21', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655948270014464, 3, '21451', NULL, NULL, NULL, 0, '2024-01-20 18:37:21', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655950350389248, 1, '21451', NULL, NULL, NULL, 0, '2024-01-20 18:37:22', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655950883065856, 2, '21451', NULL, NULL, NULL, 0, '2024-01-20 18:37:22', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655952107802624, 3, '21452', NULL, NULL, NULL, 0, '2024-01-20 18:37:22', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655953546448896, 1, '21452', NULL, NULL, NULL, 0, '2024-01-20 18:37:22', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655953848438784, 2, '21452', NULL, NULL, NULL, 0, '2024-01-20 18:37:23', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655955035426816, 3, '21453', NULL, NULL, NULL, 0, '2024-01-20 18:37:23', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655956608290816, 1, '21453', NULL, NULL, NULL, 0, '2024-01-20 18:37:23', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655957245825024, 2, '21453', NULL, NULL, NULL, 0, '2024-01-20 18:37:23', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655958487339008, 3, '21454', NULL, NULL, NULL, 0, '2024-01-20 18:37:24', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655959573663744, 1, '21454', NULL, NULL, NULL, 0, '2024-01-20 18:37:24', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655960152477696, 2, '21454', NULL, NULL, NULL, 0, '2024-01-20 18:37:24', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655961280745472, 3, '21455', NULL, NULL, NULL, 0, '2024-01-20 18:37:24', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655962333515776, 1, '21455', NULL, NULL, NULL, 0, '2024-01-20 18:37:25', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655963499532288, 2, '21455', NULL, NULL, NULL, 0, '2024-01-20 18:37:25', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655965047230464, 3, '21456', NULL, NULL, NULL, 0, '2024-01-20 18:37:25', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655967060496384, 1, '21456', NULL, NULL, NULL, 0, '2024-01-20 18:37:26', NULL, NULL, '2');
INSERT INTO `dx_my_folder` VALUES (1748655967756750848, 2, '21456', NULL, NULL, NULL, 0, '2024-01-20 18:37:26', NULL, NULL, '2');

-- ----------------------------
-- Table structure for dx_online_type
-- ----------------------------
DROP TABLE IF EXISTS `dx_online_type`;
CREATE TABLE `dx_online_type`  (
  `online_type_id` bigint NULL DEFAULT NULL COMMENT '主键',
  `online_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '[doc,docx,txt]',
  `online_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建文件名称',
  `online_api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '集成的第三方工具打开/创建API',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '删除标记'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '可在线创建文档类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_online_type
-- ----------------------------

-- ----------------------------
-- Table structure for dx_operate_record
-- ----------------------------
DROP TABLE IF EXISTS `dx_operate_record`;
CREATE TABLE `dx_operate_record`  (
  `operate_id` bigint NOT NULL COMMENT '主键',
  `forder_id` bigint NOT NULL COMMENT '目录ID;dx_folder 的 folder_id',
  `resource_id` bigint NULL DEFAULT NULL COMMENT '资源ID;dx_resource 的 resource_id',
  `operate_target` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作对象（目标）;操作对象（目标）：Spring Data JPA中文文档.pdf',
  `operate_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型;upload 上传；down 下载;create 创建；update修改；delete 删除；open 查看等等',
  `operate_body` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '具体操作分类;create_file.创建文件；create_folder.创建目录；具体要依据操作类型和操作的主体来区分',
  `operate_data` json NULL COMMENT '操作具体概述;',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '删除标记;删除标记：1.删除；2.正常',
  PRIMARY KEY (`operate_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文档库的变更记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_operate_record
-- ----------------------------

-- ----------------------------
-- Table structure for dx_original_file
-- ----------------------------
DROP TABLE IF EXISTS `dx_original_file`;
CREATE TABLE `dx_original_file`  (
  `ORIGINAL_ID` bigint NOT NULL COMMENT '主键;表主键ID',
  `ORIGINAL_NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原始名称;上传文件的',
  `ORIGINAL_TYPE` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源类型;分类：数据来源字典表;类型：聊天文件/网盘文件/系统初始化/OA系统对接',
  `FILE_TYPE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件类型;原始资源文件的类型：文本文件、视频文件、音频文件、 图片文件、可执行文件格式类型；以及未知类型；具体可根据后缀名去定义枚举类型；字典',
  `MIME_TYPE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容类型;内容类型：TXT文本文件；DOC文档；DOCX文档等，细化',
  `FILE_EXT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件后缀;原始文件后缀名：doc/docx/txt/avi等',
  `ORIGINAL_SIZE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件大小;原始资源文件大小：单位bit',
  `ORIGINAL_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '初始上传路径;文件存放路径位置',
  `REMARK` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注;备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '删除标记;删除标记：1：删除；2：正常',
  PRIMARY KEY (`ORIGINAL_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '原始资源文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_original_file
-- ----------------------------

-- ----------------------------
-- Table structure for dx_resource
-- ----------------------------
DROP TABLE IF EXISTS `dx_resource`;
CREATE TABLE `dx_resource`  (
  `RESOURCE_ID` bigint NOT NULL COMMENT '主键;资源ID',
  `ORIGINAL_FOLDER_ID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录删除时使用的关联字段',
  `FOLDER_ID` bigint NULL DEFAULT NULL COMMENT '文件所属目录ID;dx_folder 表 FOLDER_ID 字段',
  `FOLDER_PARENT_ID` bigint NULL DEFAULT NULL COMMENT '资源所属目录ID,上级目录',
  `ORIGINAL_ID` bigint NULL DEFAULT NULL COMMENT '原始资源ID;dx_original_file表 ORIGINAL_ID 字段',
  `MD5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'MD5加密串;在生成路径验证时，生成验证串',
  `RESOURCE_NAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源名称;资源名称',
  `MIME_TYPE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容类型;内容类型：TXT文本文件；DOC文档；DOCX文档；PNG图片；JPG图片等，细化',
  `RESOURCE_TYPE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源类型;原始资源文件的类型：文本文件、视频文件、音频文件、 图片文件、可执行文件格式类型；以及未知类型；具体可根据后缀名去定义枚举类型；字典',
  `RESOURCE_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录发布路径;资源文件存放位置路径',
  `RESOURCE_EXT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件后缀;文档的 如：doc，doxc wpf等',
  `RESOURCE_SIZE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源大小;资源文件的大小',
  `REMARK` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注;备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` tinyint NOT NULL COMMENT '删除标记位;删除标记：1：删除；2：正常',
  `is_show` tinyint NULL DEFAULT NULL COMMENT '是否展示1是2否',
  `is_sys` tinyint NULL DEFAULT NULL COMMENT '是否系统级1是2否',
  `dxm_id` bigint NULL DEFAULT NULL COMMENT '不用',
  PRIMARY KEY (`RESOURCE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统资源文档库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_resource
-- ----------------------------
INSERT INTO `dx_resource` VALUES (1737002452080791552, NULL, 1, 0, NULL, NULL, '最近使用', NULL, 'recent', NULL, NULL, NULL, NULL, NULL, NULL, 1, '2023-12-19 14:50:31', NULL, NULL, 2, NULL, 1, NULL);
INSERT INTO `dx_resource` VALUES (1737002688815697920, NULL, 2, 0, NULL, NULL, '我的文档', NULL, 'home', NULL, NULL, NULL, NULL, NULL, NULL, 1, '2023-12-19 14:51:28', NULL, NULL, 2, NULL, 1, NULL);
INSERT INTO `dx_resource` VALUES (1737002739126374400, NULL, 3, 0, NULL, NULL, '文件库', NULL, 'group', NULL, NULL, NULL, NULL, NULL, NULL, 1, '2023-12-19 14:51:40', NULL, NULL, 2, NULL, 1, NULL);

-- ----------------------------
-- Table structure for dx_resource_attr
-- ----------------------------
DROP TABLE IF EXISTS `dx_resource_attr`;
CREATE TABLE `dx_resource_attr`  (
  `RESOURCE_ATTR_ID` bigint NOT NULL COMMENT '主键;系统资源属性ID',
  `RESOURCE_ID` bigint NOT NULL COMMENT '系统资源ID;dx_resource 表 RESOURCE_ID',
  `IMAGE_SMALL_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `IMAGE_MIDDLE_PATH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `RESOURCE_WIDTH` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源的宽度',
  `RESOURCE_HEIGHT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源的高度;对有高度的资源进行维护',
  `REMARK` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `CREATED_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '删除标记;删除标记：1：删除；2：正常',
  PRIMARY KEY (`RESOURCE_ATTR_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文档属性扩展' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_resource_attr
-- ----------------------------

-- ----------------------------
-- Table structure for dx_resource_path
-- ----------------------------
DROP TABLE IF EXISTS `dx_resource_path`;
CREATE TABLE `dx_resource_path`  (
  `resource_path_id` bigint NOT NULL COMMENT '主键',
  `share_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分享ID',
  `folder_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目录ID;下载时，通过验证，如果需要下载源文件，通过resource_id 查找',
  `resource_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源ID;资源文件ID',
  `safe_verify_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '安全验证串;再分享时，生成的验证串',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`resource_path_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文档目录安全映射' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_resource_path
-- ----------------------------

-- ----------------------------
-- Table structure for dx_resource_recycle
-- ----------------------------
DROP TABLE IF EXISTS `dx_resource_recycle`;
CREATE TABLE `dx_resource_recycle`  (
  `id` bigint NOT NULL COMMENT '主键;系统资源属性ID',
  `resource_id` bigint NOT NULL COMMENT '系统资源ID;dx_resource 表 RESOURCE_ID',
  `folder_parent_id` bigint NOT NULL COMMENT '被删除前的上级目录id',
  `REMARK` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '删除标记;删除标记：1：删除；2：正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文档属性扩展' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_resource_recycle
-- ----------------------------

-- ----------------------------
-- Table structure for dx_sys_config
-- ----------------------------
DROP TABLE IF EXISTS `dx_sys_config`;
CREATE TABLE `dx_sys_config`  (
  `sys_config_id` bigint NOT NULL COMMENT '主键',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置项',
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置值',
  `config_status` tinyint NOT NULL COMMENT '配置信息状态;1.正常；-1.未启用等',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `DELETE_FLAG` tinyint NOT NULL,
  PRIMARY KEY (`sys_config_id`, `config_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文档系统级配置(KV)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dx_sys_config
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
