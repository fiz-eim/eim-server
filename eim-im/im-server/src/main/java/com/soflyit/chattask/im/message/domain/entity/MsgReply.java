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
 * 消息回复统计对象 msg_reply
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "msg_reply")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgReply extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "回复数量")
    @TableField(value = "reply_count")
    @ApiModelProperty("回复数量")
    private Integer replyCount;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后回复时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "last_reply_time")
    @ApiModelProperty("最后回复时间")
    private Date lastReplyTime;


    @Excel(name = "参与人")
    @TableField(value = "participants")
    @ApiModelProperty("参与人")
    private String participants;


    @Excel(name = "频道Id")
    @TableField(value = "channel_id")
    @ApiModelProperty("频道Id")
    private Long channelId;


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


    @Excel(name = "删除时间;默认-1,表示未删除")
    @TableField(value = "thread_delete_time")
    @ApiModelProperty("删除时间;默认-1,表示未删除")
    private Long threadDeleteTime;


    @TableField(value = "ext_data")
    private String extData;

}
