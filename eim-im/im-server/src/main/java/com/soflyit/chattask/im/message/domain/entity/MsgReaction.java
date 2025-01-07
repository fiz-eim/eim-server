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
 * 消息回应对象 msg_reaction
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "msg_reaction")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgReaction extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "消息Id")
    @TableField(value = "msg_id")
    @ApiModelProperty("消息Id")
    private Long msgId;


    @Excel(name = "用户Id")
    @TableField(value = "user_id")
    @ApiModelProperty("用户Id")
    private Long userId;

    @TableField(exist = false)
    private String userName;


    @Excel(name = "表情名称")
    @TableField(value = "emoji_name")
    @ApiModelProperty("表情名称")
    private String emojiName;


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


    @Excel(name = "删除时间;-1 未删除")
    @TableField(value = "delete_time")
    @ApiModelProperty("删除时间;-1 未删除")
    private Long deleteTime;

}
