package com.soflyit.chattask.im.channel.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 频道成员对象 channel_member
 *
 * @author soflyit
 * @date 2023-11-16 11:05:18
 */
@TableName(value = "channel_member")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelMember extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "用户Id")
    @TableField(value = "user_id")
    @ApiModelProperty("用户Id")
    private Long userId;


    @Excel(name = "频道Id")
    @TableField(value = "channel_id")
    @ApiModelProperty("频道Id")
    private Long channelId;


    @Excel(name = "真实姓名")
    @TableField(value = "real_name")
    @ApiModelProperty("真实姓名")
    private String realName;


    @Excel(name = "管理角色;1-群主,2-管理员(备用),3-成员")
    @TableField(value = "manager")
    @ApiModelProperty("管理角色;1-群主,2-管理员(备用),3-成员")
    private Integer manager;


    @Excel(name = "置顶;1-是；2-否")
    @TableField(value = "pinned_flag")
    @ApiModelProperty("置顶;1-是；2-否")
    private Integer pinnedFlag;


    @Excel(name = "免打扰;1-是；2-否")
    @TableField(value = "dnd_flag")
    @ApiModelProperty("免打扰;1-是；2-否")
    private Integer dndFlag;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后查看时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "last_view_time")
    @ApiModelProperty("最后查看时间")
    private Date lastViewTime;


    @Excel(name = "已读消息数量;用于计算用户在该频道的未读消息数量：未读数量=频道消息数-已读数量")
    @TableField(value = "msg_count")
    @ApiModelProperty("已读消息数量;用于计算用户在该频道的未读消息数量：未读数量=频道消息数-已读数量")
    private Long msgCount;


    @Excel(name = "未读消息")
    @TableField(value = "unread_count")
    @ApiModelProperty("未读消息")
    private Long unreadCount;


    @Excel(name = "未读数量;@自己的")
    @TableField(value = "mention_count")
    @ApiModelProperty("未读数量;@自己的")
    private Long mentionCount;


    @Excel(name = "通知属性;用户自己配置")
    @TableField(value = "notify_props")
    @ApiModelProperty("通知属性;用户自己配置")
    private String notifyProps;


    @Excel(name = "根消息数量")
    @TableField(value = "root_msg_count")
    @ApiModelProperty("根消息数量")
    private Long rootMsgCount;


    @Excel(name = "未读根消息数量;@自己的")
    @TableField(value = "mention_root_count")
    @ApiModelProperty("未读根消息数量;@自己的")
    private Long mentionRootCount;


    @Excel(name = "紧急未读消息;@自己的")
    @TableField(value = "urgent_mention_count")
    @ApiModelProperty("紧急未读消息;@自己的")
    private Long urgentMentionCount;


    @Excel(name = "成员类型")
    @TableField(value = "member_type")
    @ApiModelProperty("成员类型")
    private Short memberType;

    private String nickName;

    private String displayName;

    private Short collapse;

    private String extData;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;

}
