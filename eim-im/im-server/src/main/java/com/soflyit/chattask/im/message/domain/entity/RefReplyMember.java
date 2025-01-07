package com.soflyit.chattask.im.message.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 回复消息与用户关系对象 ref_reply_member
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "ref_reply_member")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class RefReplyMember extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "主消息Id")
    @TableField(value = "msg_id")
    @ApiModelProperty("主消息Id")
    private Long msgId;


    @Excel(name = "用户Id")
    @TableField(value = "user_id")
    @ApiModelProperty("用户Id")
    private Long userId;


    @Excel(name = "未读数量")
    @TableField(value = "unread_count")
    @ApiModelProperty("未读数量")
    private Long unreadCount;


    @Excel(name = "未读提及(@)消息")
    @TableField(value = "mention_count")
    @ApiModelProperty("未读提及(@)消息")
    private Long mentionCount;


    @Excel(name = "未读紧急消息数量")
    @TableField(value = "urgent_mention_count")
    @ApiModelProperty("未读紧急消息数量")
    private Long urgentMentionCount;


    @Excel(name = "关注标志;1-关注；2-未关注")
    @TableField(value = "follow_flag")
    @ApiModelProperty("关注标志;1-关注；2-未关注")
    private Long followFlag;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;

}
