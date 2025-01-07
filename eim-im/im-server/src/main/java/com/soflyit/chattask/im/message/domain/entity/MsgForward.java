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
 * 消息转发对象 msg_forward
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "msg_forward")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgForward extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "消息Id")
    @TableField(value = "msg_id")
    @ApiModelProperty("消息Id")
    private Long msgId;


    @Excel(name = "被转发消息Id")
    @TableField(value = "forward_msg_id")
    @ApiModelProperty("被转发消息Id")
    private Long forwardMsgId;


    @Excel(name = "排序")
    @TableField(value = "sort_order")
    @ApiModelProperty("排序")
    private Integer sortOrder;


    @Excel(name = "删除标志;标识被转发消息是否被删除：1-是；2-否")
    @TableField(value = "deleted_flag")
    @ApiModelProperty("删除标志;标识被转发消息是否被删除：1-是；2-否")
    private Short deletedFlag;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;

}
