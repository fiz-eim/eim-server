package com.soflyit.chattask.dx.modular.online.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.chattask.dx.common.base.DmsCommonEntity;
import com.soflyit.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 可在线创建文档类型对象 dx_online_type
 *
 * @author soflyit
 * @date 2023-11-06 14:35:44
 */
@TableName(value = "dx_online_type")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class OnlineTypeEntity extends DmsCommonEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "主键")
    @TableId(value = "online_type_id")
    @ApiModelProperty("主键")
    private Long onlineTypeId;


    @Excel(name = "集成的第三方工具打开/创建API")
    @TableField(value = "online_api")
    @ApiModelProperty("集成的第三方工具打开/创建API")
    private String onlineApi;


    @Excel(name = "[doc,docx,txt]")
    @TableField(value = "online_type")
    @ApiModelProperty("[doc,docx,txt]")
    private String onlineType;



    @Excel(name = "创建文件名称")
    @TableField(value = "online_type_name")
    @ApiModelProperty("创建文件名称")
    private String onlineTypeName;


}
