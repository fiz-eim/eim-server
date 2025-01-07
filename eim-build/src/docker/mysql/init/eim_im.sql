/*
 Navicat Premium Dump SQL

 Source Server Type    : MySQL
 Source Server Version : 80040 (8.0.40)
 Source Schema         : eim_im

 Target Server Type    : MySQL
 Target Server Version : 80040 (8.0.40)
 File Encoding         : 65001

 Date: 07/01/2025 10:36:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

create database if not exists `eim_im` CHARACTER SET 'utf8mb4';
GRANT Alter, Alter Routine, Create, Create Routine, Create Temporary Tables, Create View, Delete, Drop, Event, Execute, Index, Insert, Lock Tables, References, Select, Show View, Trigger, Update ON `eim\_im`.* TO `fizeim`@`%`;

use  `eim_im`;

-- ----------------------------
-- Table structure for channel_member
-- ----------------------------
DROP TABLE IF EXISTS `channel_member`;
CREATE TABLE `channel_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户Id',
  `member_type` tinyint NULL DEFAULT NULL COMMENT '成员类型：1-用户；2-机器人',
  `channel_id` bigint NULL DEFAULT NULL COMMENT '频道Id',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `manager` tinyint NULL DEFAULT NULL COMMENT '管理角色;1-群主,2-成员,3-管理员(备用)',
  `pinned_flag` tinyint NOT NULL DEFAULT 2 COMMENT '置顶;1-是；2-否',
  `dnd_flag` tinyint NOT NULL DEFAULT 2 COMMENT '免打扰;1-是；2-否',
  `last_view_time` datetime NULL DEFAULT NULL COMMENT '最后查看时间',
  `msg_count` int NULL DEFAULT NULL COMMENT '已读消息数量;用于计算用户在该频道的未读消息数量：未读数量=频道消息数-已读数量',
  `unread_count` int NULL DEFAULT NULL COMMENT '未读消息',
  `mention_count` int NULL DEFAULT NULL COMMENT '未读数量;@自己的',
  `notify_props` json NULL COMMENT '通知属性;用户自己配置',
  `root_msg_count` int NULL DEFAULT NULL COMMENT '根消息数量',
  `mention_root_count` int NULL DEFAULT NULL COMMENT '未读根消息数量;@自己的',
  `urgent_mention_count` int NULL DEFAULT NULL COMMENT '紧急未读消息;@自己的',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `display_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '群备注',
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '群昵称',
  `collapse` tinyint NOT NULL DEFAULT 2 COMMENT '折叠标志：1-是；2-否',
  `ext_data` json NULL COMMENT '扩展数据',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_channel_id`(`user_id` ASC, `channel_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 157072937601859586 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '频道成员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of channel_member
-- ----------------------------
INSERT INTO `channel_member` VALUES (157072937601859584, 21438, 1, 157072937471836160, '张伟', 3, 2, 2, NULL, 0, 0, 0, NULL, 0, 0, 0, NULL, NULL, NULL, 2, NULL, NULL, NULL, 1, '2025-01-07 10:31:47', 1, '2025-01-07 10:31:47');
INSERT INTO `channel_member` VALUES (157072937601859585, 1, 1, 157072937471836160, '超级管理员', 1, 2, 2, NULL, 0, 0, 0, NULL, 0, 0, 0, NULL, NULL, NULL, 2, NULL, NULL, NULL, 1, '2025-01-07 10:31:47', 1, '2025-01-07 10:31:47');

-- ----------------------------
-- Table structure for chat_channel
-- ----------------------------
DROP TABLE IF EXISTS `chat_channel`;
CREATE TABLE `chat_channel`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '频道编码;不需要',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '频道名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `type` tinyint NULL DEFAULT NULL COMMENT '类型;1-单聊,2-部门群,3-项目群,4-任务群,5-讨论组,6-机器人',
  `last_msg_time` datetime NULL DEFAULT NULL COMMENT '最新消息时间',
  `total_msg_count` bigint NULL DEFAULT NULL COMMENT '消息总数',
  `last_root_msg_time` datetime NULL DEFAULT NULL COMMENT '最新根消息时间',
  `total_root_msg_count` bigint NULL DEFAULT NULL COMMENT '根消息数量',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `ext_data` json NULL COMMENT '扩展配置',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `delete_by` bigint NULL DEFAULT NULL COMMENT '删除人',
  `delete_time` bigint NOT NULL DEFAULT -1 COMMENT '删除时间;默认-1，表示未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 157072937471836161 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '频道' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_channel
-- ----------------------------
INSERT INTO `chat_channel` VALUES (157072937471836160, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, '{\"banConfig\": {\"blacklist\": [], \"whitelist\": [], \"banAllFlag\": false}, \"memberAppFlag\": true, \"ownerManageFlag\": false, \"ownerTopMsgFlag\": false, \"shortcutBarFlag\": true, \"memberReviewFlag\": false, \"ownerMentionAllFlag\": false}', NULL, NULL, 1, '2025-01-07 10:31:47', 1, '2025-01-07 10:31:47', NULL, -1);

-- ----------------------------
-- Table structure for chat_user_status
-- ----------------------------
DROP TABLE IF EXISTS `chat_user_status`;
CREATE TABLE `chat_user_status`  (
  `id` bigint NULL DEFAULT NULL COMMENT '主键',
  `work_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工作状态',
  `dnd_flag` tinyint NULL DEFAULT NULL COMMENT '勿扰标志;1-是, 2-否',
  `dnd_end_time` bigint NULL DEFAULT 0 COMMENT '勿扰结束时间;默认-0，一直持续',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户在线状态' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_user_status
-- ----------------------------

-- ----------------------------
-- Table structure for his_channel_member
-- ----------------------------
DROP TABLE IF EXISTS `his_channel_member`;
CREATE TABLE `his_channel_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `member_id` bigint NULL DEFAULT NULL COMMENT '成员Id',
  `channel_id` bigint NULL DEFAULT NULL COMMENT '频道Id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `member_type` tinyint NULL DEFAULT NULL COMMENT '成员类型',
  `join_time` bigint NULL DEFAULT NULL COMMENT '加入时间',
  `leave_time` bigint NULL DEFAULT 0 COMMENT '离开时间;默认为0',
  `member_data` json NULL COMMENT '成员数据',
  `effect_flag` tinyint NULL DEFAULT 1 COMMENT '有效标志;1- 是；2 - 否',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 104873643654643713 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '频道成员历史表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_channel_member
-- ----------------------------

-- ----------------------------
-- Table structure for im_card
-- ----------------------------
DROP TABLE IF EXISTS `im_card`;
CREATE TABLE `im_card`  (
  `id` bigint NOT NULL,
  `card_type` tinyint NULL DEFAULT NULL,
  `template_id` bigint NULL DEFAULT NULL,
  `channel_id` bigint NULL DEFAULT NULL,
  `card_define` json NULL,
  `card_data` json NULL,
  `pinned_flag` tinyint NULL DEFAULT NULL,
  `category` tinyint NULL DEFAULT NULL,
  `msg_card_flag` tinyint NULL DEFAULT NULL,
  `app_id` bigint NULL DEFAULT NULL,
  `bot_id` bigint NULL DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `revision` int NULL DEFAULT NULL,
  `tenant_id` bigint NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of im_card
-- ----------------------------

-- ----------------------------
-- Table structure for link_meta
-- ----------------------------
DROP TABLE IF EXISTS `link_meta`;
CREATE TABLE `link_meta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接地址',
  `access_time` bigint NULL DEFAULT NULL COMMENT '访问时间;访问时间：精确到小时',
  `hash` int NULL DEFAULT NULL COMMENT 'hash值;hash(url+访问时间)',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型;none-无, image-图片',
  `data` json NULL COMMENT '元数据',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '链接元数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of link_meta
-- ----------------------------

-- ----------------------------
-- Table structure for member_delete_msg
-- ----------------------------
DROP TABLE IF EXISTS `member_delete_msg`;
CREATE TABLE `member_delete_msg`  (
  `id` bigint NOT NULL,
  `member_id` bigint NULL DEFAULT NULL,
  `channel_id` bigint NULL DEFAULT NULL,
  `msg_id` bigint NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `delete_time` datetime NULL DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tenant_id` bigint NULL DEFAULT NULL,
  `revision` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of member_delete_msg
-- ----------------------------

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NULL DEFAULT NULL COMMENT '消息发送人',
  `channel_id` bigint NULL DEFAULT NULL COMMENT '频道',
  `root_id` bigint NULL DEFAULT NULL COMMENT '根消息Id',
  `original_id` bigint NULL DEFAULT NULL COMMENT '原始消息Id',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息内容',
  `type` tinyint NULL DEFAULT NULL COMMENT '消息类型;1-用户消息,2-系统消息,3-机器人消息,4-应用消息',
  `props` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息属性',
  `files` json NULL COMMENT '文件名列表;[{id:xxx,name:xxx,size:xx,extension:xxx,mimeType:xxx}]',
  `has_reactions` tinyint NULL DEFAULT 2 COMMENT '是否有回应;1-是,2-否',
  `edit_time` datetime NULL DEFAULT NULL COMMENT '编辑时间',
  `priority` tinyint NULL DEFAULT 0 COMMENT '优先级;0-无,1-低,2-中,3-高,4-紧急',
  `requested_ack` tinyint NULL DEFAULT 2 COMMENT '是否需要确认;1-是，2-否',
  `persistent_notification` tinyint NULL DEFAULT 2 COMMENT '是否持续通知;1-是，2-否',
  `mention_users` json NULL COMMENT '提及用户;数据格式: {用户Id:true}',
  `msg_ext_data` json NULL COMMENT '消息扩展属性',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端消息Id',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `delete_by` bigint NULL DEFAULT NULL COMMENT '删除人',
  `delete_time` bigint NULL DEFAULT -1 COMMENT '删除时间;默认为-1，表示未删除',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106821622439485441 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for msg_ack
-- ----------------------------
DROP TABLE IF EXISTS `msg_ack`;
CREATE TABLE `msg_ack`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `msg_id` bigint NULL DEFAULT NULL COMMENT '消息Id',
  `channel_id` bigint NULL DEFAULT NULL COMMENT '频道Id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户Id',
  `ack_time` datetime NULL DEFAULT NULL COMMENT '确认时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 104945000769781761 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息确认' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of msg_ack
-- ----------------------------

-- ----------------------------
-- Table structure for msg_favorite
-- ----------------------------
DROP TABLE IF EXISTS `msg_favorite`;
CREATE TABLE `msg_favorite`  (
  `id` bigint NOT NULL,
  `msg_id` bigint NULL DEFAULT NULL COMMENT '消息Id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户Id',
  `channel_id` bigint NULL DEFAULT NULL COMMENT '频道Id',
  `favorite_time` datetime NULL DEFAULT NULL COMMENT '收藏时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息收藏' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of msg_favorite
-- ----------------------------

-- ----------------------------
-- Table structure for msg_forward
-- ----------------------------
DROP TABLE IF EXISTS `msg_forward`;
CREATE TABLE `msg_forward`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `msg_id` bigint NULL DEFAULT NULL COMMENT '消息Id',
  `forward_msg_id` bigint NULL DEFAULT NULL COMMENT '被转发消息Id',
  `sort_order` int NULL DEFAULT NULL COMMENT '排序',
  `deleted_flag` tinyint NULL DEFAULT 2 COMMENT '删除标志;标识被转发消息是否被删除：1-是；2-否',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息转发表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of msg_forward
-- ----------------------------

-- ----------------------------
-- Table structure for msg_pinned
-- ----------------------------
DROP TABLE IF EXISTS `msg_pinned`;
CREATE TABLE `msg_pinned`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel_id` bigint NULL DEFAULT NULL COMMENT '频道Id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户Id',
  `msg_id` bigint NULL DEFAULT NULL COMMENT '消息Id',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息标注表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of msg_pinned
-- ----------------------------

-- ----------------------------
-- Table structure for msg_reply
-- ----------------------------
DROP TABLE IF EXISTS `msg_reply`;
CREATE TABLE `msg_reply`  (
  `id` bigint NOT NULL COMMENT '主键',
  `reply_count` int NULL DEFAULT NULL COMMENT '回复数量',
  `last_reply_time` datetime NULL DEFAULT NULL COMMENT '最后回复时间',
  `participants` json NULL COMMENT '参与人',
  `channel_id` bigint NULL DEFAULT NULL COMMENT '频道Id',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `ext_data` json NULL COMMENT '扩展字段',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `delete_by` bigint NULL DEFAULT NULL COMMENT '删除人',
  `thread_delete_time` bigint NULL DEFAULT -1 COMMENT '删除时间;默认-1,表示未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息回复统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of msg_reply
-- ----------------------------

-- ----------------------------
-- Table structure for msg_statistic
-- ----------------------------
DROP TABLE IF EXISTS `msg_statistic`;
CREATE TABLE `msg_statistic`  (
  `msg_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户Id',
  `unread_count` int NULL DEFAULT NULL COMMENT '未读数量',
  `unack_count` int NULL DEFAULT NULL COMMENT '未确认数量',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106821622439485441 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息读取统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of msg_statistic
-- ----------------------------

-- ----------------------------
-- Table structure for msg_top
-- ----------------------------
DROP TABLE IF EXISTS `msg_top`;
CREATE TABLE `msg_top`  (
  `id` bigint NOT NULL,
  `channel_id` bigint NULL DEFAULT NULL,
  `msg_id` bigint NULL DEFAULT NULL,
  `top_type` tinyint NULL DEFAULT NULL,
  `close_users` json NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `card_id` bigint NULL DEFAULT NULL,
  `card_app_id` bigint NULL DEFAULT NULL,
  `card_define` json NULL,
  `card_data` json NULL,
  `tenant_id` int NULL DEFAULT NULL,
  `revision` int NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of msg_top
-- ----------------------------

-- ----------------------------
-- Table structure for msg_unread
-- ----------------------------
DROP TABLE IF EXISTS `msg_unread`;
CREATE TABLE `msg_unread`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `msg_id` bigint NULL DEFAULT NULL COMMENT '消息Id',
  `channel_id` bigint NULL DEFAULT NULL COMMENT '频道Id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '未读用户Id',
  `read_time` datetime NULL DEFAULT NULL COMMENT '读取时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_msg_id`(`msg_id` ASC, `user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106821622498205697 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息未读明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of msg_unread
-- ----------------------------

-- ----------------------------
-- Table structure for ref_reply_member
-- ----------------------------
DROP TABLE IF EXISTS `ref_reply_member`;
CREATE TABLE `ref_reply_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `msg_id` bigint NULL DEFAULT NULL COMMENT '主消息Id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户Id',
  `unread_count` bigint NULL DEFAULT NULL COMMENT '未读数量',
  `mention_count` bigint NULL DEFAULT NULL COMMENT '未读提及(@)消息',
  `urgent_mention_count` bigint NULL DEFAULT NULL COMMENT '未读紧急消息数量',
  `follow_flag` tinyint NULL DEFAULT NULL COMMENT '关注标志;1-关注；2-未关注',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint NULL DEFAULT NULL COMMENT '租户号',
  `REVISION` int NULL DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint NULL DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint NULL DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '回复消息与用户关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ref_reply_member
-- ----------------------------
CREATE TABLE `msg_text_tag` (
  `msg_id` bigint NOT NULL,
  `tag_data` json DEFAULT NULL COMMENT '标签数据',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint DEFAULT NULL,
  `revision` int DEFAULT NULL,
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;


CREATE TABLE `msg_reaction` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `msg_id` bigint DEFAULT NULL COMMENT '消息Id',
  `user_id` bigint DEFAULT NULL COMMENT '用户Id',
  `emoji_name` varchar(64) DEFAULT NULL COMMENT '表情名称',
  `channel_id` bigint DEFAULT NULL COMMENT '频道Id',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint DEFAULT NULL COMMENT '租户号',
  `REVISION` int DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_by` bigint DEFAULT NULL COMMENT '删除人',
  `delete_time` bigint DEFAULT '-1' COMMENT '删除时间;-1 未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息回应表';


CREATE TABLE `msg_file` (
  `id` bigint DEFAULT NULL COMMENT '主键',
  `msg_id` bigint DEFAULT NULL COMMENT '消息Id',
  `channel_id` bigint DEFAULT NULL COMMENT '频道Id',
  `name` varchar(200) DEFAULT NULL COMMENT '文件名',
  `storage_type` tinyint DEFAULT NULL COMMENT '存储类型;1-本地存在,2-文档库',
  `external_id` varchar(64) DEFAULT NULL COMMENT '外部系统Id',
  `path` varchar(512) DEFAULT NULL COMMENT '文件路径',
  `thumbnail_path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '缩略图路径',
  `preview_path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '预览路径',
  `extension` varchar(64) DEFAULT NULL COMMENT '扩展名',
  `mime_type` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件类型',
  `size` bigint DEFAULT NULL COMMENT '文件大小',
  `width` int DEFAULT NULL COMMENT '宽度',
  `height` int DEFAULT NULL COMMENT '高度',
  `has_preview_image` tinyint DEFAULT NULL COMMENT '是否有预览图片',
  `mini_preview` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '迷你预览图',
  `hash_sha256` varchar(64) DEFAULT NULL COMMENT '文件哈希值',
  `hash_sha1` varchar(62) DEFAULT NULL COMMENT '文件哈希值',
  `hash_md5` varchar(32) DEFAULT NULL COMMENT '文件哈希值',
  `archived` tinyint DEFAULT '2' COMMENT '是否已归档;1-是,2-否',
  `delete_by` bigint DEFAULT NULL COMMENT '删除人',
  `delete_time` bigint DEFAULT '-1' COMMENT '删除时间;默认-1，表示未删除',
  `ext_data` json DEFAULT NULL COMMENT '扩展信息',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `TENANT_ID` bigint DEFAULT NULL COMMENT '租户号',
  `REVISION` int DEFAULT NULL COMMENT '乐观锁',
  `CREATE_BY` bigint DEFAULT NULL COMMENT '创建人',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` bigint DEFAULT NULL COMMENT '更新人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `uuid` varbinary(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息文件';

SET FOREIGN_KEY_CHECKS = 1;
