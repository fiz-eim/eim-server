package com.soflyit.chattask.im.channel.domain.entity;

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

@TableName(value = "im_card")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class ImCard extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;


    @TableField(value = "card_type")
    @ApiModelProperty("卡片类型")
    private Short cardType;



    @TableField(value = "category")
    @ApiModelProperty("卡片分类")
    private Short category;


    @TableField(value = "msg_card_flag")
    private Short msgCardFlag;



    @Excel(name = "群组Id")
    @TableField(value = "channel_id")
    @ApiModelProperty("群组Id")
    private Long channelId;

    @Excel(name = "模板Id")
    @TableField(value = "template_id")
    @ApiModelProperty("模板Id")
    private Long templateId;


    @Excel(name = "卡片定义")
    @TableField(value = "card_define")
    @ApiModelProperty("卡片定义 - 模板内容")
    private String cardDefine;


    @Excel(name = "卡片数据")
    @TableField(value = "card_data")
    @ApiModelProperty("卡片数据")
    private String cardData;


    @TableField(value = "pinned_flag")
    @ApiModelProperty("是否置顶")
    private Short pinnedFlag;


    @TableField(value = "app_id")
    @ApiModelProperty("应用Id")
    private Long appId;


    @TableField(value = "bot_id")
    @ApiModelProperty("机器人Id")
    private Long botId;



    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;
}
