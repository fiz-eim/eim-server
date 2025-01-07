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
 * 频道对象 chat_channel
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "chat_channel")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatChannel extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "频道编码;不需要")
    @TableField(value = "code")
    @ApiModelProperty("频道编码;不需要")
    private String code;


    @Excel(name = "频道名称")
    @TableField(value = "name")
    @ApiModelProperty("频道名称")
    private String name;


    @Excel(name = "频道图标")
    @TableField(value = "icon")
    @ApiModelProperty("频道图标")
    private String icon;


    @Excel(name = "类型;1-单聊,2-部门群,3-项目群,4-任务群,5-讨论组")
    @TableField(value = "type")
    @ApiModelProperty("类型;1-单聊,2-部门群,3-项目群,4-任务群,5-讨论组")
    private Integer type;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最新消息时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "last_msg_time")
    @ApiModelProperty("最新消息时间")
    private Date lastMsgTime;


    @Excel(name = "消息总数")
    @TableField(value = "total_msg_count")
    @ApiModelProperty("消息总数")
    private Long totalMsgCount;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最新根消息时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "last_root_msg_time")
    @ApiModelProperty("最新根消息时间")
    private Date lastRootMsgTime;


    @Excel(name = "根消息数量")
    @TableField(value = "total_root_msg_count")
    @ApiModelProperty("根消息数量")
    private Long totalRootMsgCount;


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


    @Excel(name = "删除时间;默认-1，表示未删除")
    @TableField(value = "delete_time")
    @ApiModelProperty("删除时间;默认-1，表示未删除")
    private Long deleteTime;



    @ApiModelProperty("扩展配置")
    @TableField(value = "ext_data")
    private String extData;

}
