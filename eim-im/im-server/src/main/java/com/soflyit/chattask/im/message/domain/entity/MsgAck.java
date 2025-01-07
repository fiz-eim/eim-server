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

import static com.baomidou.mybatisplus.annotation.FieldStrategy.IGNORED;

/**
 * 消息确认对象 msg_ack
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "msg_ack")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgAck extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "消息Id")
    @TableField(value = "msg_id")
    @ApiModelProperty("消息Id")
    private Long msgId;


    @Excel(name = "频道Id")
    @TableField(value = "channel_id")
    @ApiModelProperty("频道Id")
    private Long channelId;


    @Excel(name = "用户Id")
    @TableField(value = "user_id")
    @ApiModelProperty("用户Id")
    private Long userId;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "确认时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "ack_time", updateStrategy = IGNORED)
    @ApiModelProperty("确认时间")
    private Date ackTime;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;

}
