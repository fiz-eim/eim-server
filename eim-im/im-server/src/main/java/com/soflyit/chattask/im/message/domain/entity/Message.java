package com.soflyit.chattask.im.message.domain.entity;

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
 * 消息对象 message
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "message")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class Message extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "消息发送人")
    @TableField(value = "user_id")
    @ApiModelProperty("消息发送人")
    private Long userId;


    @Excel(name = "频道")
    @TableField(value = "channel_id")
    @ApiModelProperty("频道")
    private Long channelId;


    @Excel(name = "根消息Id")
    @TableField(value = "root_id")
    @ApiModelProperty("根消息Id")
    private Long rootId;


    @Excel(name = "原始消息Id")
    @TableField(value = "original_id")
    @ApiModelProperty("原始消息Id")
    private Long originalId;


    @Excel(name = "消息内容")
    @TableField(value = "content")
    @ApiModelProperty("消息内容")
    private String content;


    @Excel(name = "消息类型;1-用户消息,2-加入频道", readConverterExp = "创=建群聊")
    @TableField(value = "type")
    @ApiModelProperty("消息类型;1-用户消息,2-加入频道（创建群聊）,3-添加到频道（群聊邀请）,4-系统欢迎消息,5-应用消息")
    private Short type;


    @Excel(name = "消息属性")
    @TableField(value = "props")
    @ApiModelProperty("消息属性")
    private String propsStr;


    @Excel(name = "文件名列表;[{id:xxx,name:xxx,size:xx,extension:xxx,mimeType:xxx}]")
    @TableField(value = "files")
    @ApiModelProperty("文件名列表;[{id:xxx,name:xxx,size:xx,extension:xxx,mimeType:xxx}]")
    private String msgFiles;


    @Excel(name = "是否有回应;1-是,2-否")
    @TableField(value = "has_reactions")
    @ApiModelProperty("是否有回应;1-是,2-否")
    private Short hasReactions;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "编辑时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "edit_time")
    @ApiModelProperty("编辑时间")
    private Date editTime;


    @Excel(name = "优先级;0-无,1-低,2-中,3-高,4-紧急")
    @TableField(value = "priority")
    @ApiModelProperty("优先级;0-无,1-低,2-中,3-高,4-紧急")
    private Integer priority;


    @Excel(name = "是否需要确认;1-是，2-否")
    @TableField(value = "requested_ack")
    @ApiModelProperty("是否需要确认;1-是，2-否")
    private Short requestedAck;


    @Excel(name = "是否持续通知;1-是，2-否")
    @TableField(value = "persistent_notification")
    @ApiModelProperty("是否持续通知;1-是，2-否")
    private Short persistentNotification;


    @Excel(name = "提及用户;数据格式: {用户Id:true}")
    @TableField(value = "mention_users")
    @ApiModelProperty("提及用户;数据格式: {用户Id:true}")
    private String mentionUsers;


    @Excel(name = "消息扩展属性")
    @TableField(value = "msg_ext_data")
    @ApiModelProperty("消息扩展属性")
    private String msgExtData;


    @TableField(value = "uuid")
    @ApiModelProperty("客户端消息Id")
    private String uuid;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;


    @Excel(name = "删除人")
    @TableField(value = "delete_by")
    @ApiModelProperty("删除人")
    private Long deleteBy;


    @Excel(name = "删除时间;默认为-1，表示未删除")
    @TableField(value = "delete_time")
    @ApiModelProperty("删除时间;默认为-1，表示未删除")
    private Long deleteTime;


    @TableField(exist = false, value = "first_msg_id")
    private Long firstMsgId;

}
