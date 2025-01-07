package com.soflyit.chattask.im.channel.domain.entity;

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
 * 频道成员历史对象 his_channel_member
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "his_channel_member")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class HisChannelMember extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "成员Id")
    @TableField(value = "member_id")
    @ApiModelProperty("成员Id")
    private Long memberId;


    @Excel(name = "频道Id")
    @TableField(value = "channel_id")
    @ApiModelProperty("频道Id")
    private Long channelId;


    @Excel(name = "用户id")
    @TableField(value = "user_id")
    @ApiModelProperty("用户id")
    private Long userId;


    @Excel(name = "加入时间")
    @TableField(value = "join_time")
    @ApiModelProperty("加入时间")
    private Long joinTime;


    @Excel(name = "离开时间;默认为0")
    @TableField(value = "leave_time")
    @ApiModelProperty("离开时间;默认为0")
    private Long leaveTime;


    @Excel(name = "成员数据")
    @TableField(value = "member_data")
    @ApiModelProperty("成员数据")
    private String memberData;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;

}
