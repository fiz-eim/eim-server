package com.soflyit.chattask.im.message.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 消息收藏对象 msg_pinned
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "msg_pinned")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgPinned extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "频道Id")
    @TableField(value = "channel_id")
    @ApiModelProperty("频道Id")
    private Long channelId;


    @Excel(name = "用户Id")
    @TableField(value = "user_id")
    @ApiModelProperty("用户Id")
    private Long userId;


    @Excel(name = "消息Id")
    @TableField(value = "msg_id")
    @ApiModelProperty("消息Id")
    private Long msgId;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;

}
