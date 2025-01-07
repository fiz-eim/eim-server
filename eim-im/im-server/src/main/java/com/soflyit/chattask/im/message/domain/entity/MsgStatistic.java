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
 * 消息读取统计对象 msg_statistic
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "msg_statistic")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgStatistic extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "主键")
    @TableId(value = "msg_id")
    @ApiModelProperty("主键")
    private Long msgId;


    @Excel(name = "用户Id")
    @TableField(value = "user_id")
    @ApiModelProperty("用户Id")
    private Long userId;


    @Excel(name = "未读数量")
    @TableField(value = "unread_count")
    @ApiModelProperty("未读数量")
    private Integer unreadCount;


    @Excel(name = "未确认数量")
    @TableField(value = "unack_count")
    @ApiModelProperty("未确认数量")
    private Integer unAckCount;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;

}
