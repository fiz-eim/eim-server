package com.soflyit.chattask.im.system.domain;

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
 * 用户在线状态对象 chat_user_status
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "chat_user_status")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatUserStatus extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "工作状态")
    @TableField(value = "work_status")
    @ApiModelProperty("工作状态")
    private String workStatus;


    @Excel(name = "勿扰标志;1-是, 2-否")
    @TableField(value = "dnd_flag")
    @ApiModelProperty("勿扰标志;1-是, 2-否")
    private Long dndFlag;


    @Excel(name = "勿扰结束时间;默认-0，一直持续")
    @TableField(value = "dnd_end_time")
    @ApiModelProperty("勿扰结束时间;默认-0，一直持续")
    private Long dndEndTime;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;

}
