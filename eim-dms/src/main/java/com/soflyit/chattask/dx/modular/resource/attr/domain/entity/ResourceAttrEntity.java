package com.soflyit.chattask.dx.modular.resource.attr.domain.entity;

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
 * 文档属性扩展对象 dx_resource_attr
 *
 * @author soflyit
 * @date 2023-11-07 16:57:52
 */
@TableName(value = "dx_resource_attr")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceAttrEntity extends DmsCommonEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "中等压缩图")
    @TableField(value = "IMAGE_MIDDLE_PATH")
    @ApiModelProperty("中等压缩图")
    private String imageMiddlePath;


    @Excel(name = "最小压缩图地址")
    @TableField(value = "IMAGE_SMALL_PATH")
    @ApiModelProperty("最小压缩图地址")
    private String imageSmallPath;


    @Excel(name = "主键;系统资源属性ID")
    @TableId                           (value = "RESOURCE_ATTR_ID")
    @ApiModelProperty("主键;系统资源属性ID")
    private Long resourceAttrId;


    @Excel(name = "资源的高度;对有高度的资源进行维护")
    @TableField(value = "RESOURCE_HEIGHT")
    @ApiModelProperty("资源的高度;对有高度的资源进行维护")
    private String resourceHeight;


    @Excel(name = "系统资源ID;dx_resource 表 RESOURCE_ID")
    @TableField(value = "RESOURCE_ID")
    @ApiModelProperty("系统资源ID;dx_resource 表 RESOURCE_ID")
    private Long resourceId;


    @Excel(name = "资源的宽度")
    @TableField(value = "RESOURCE_WIDTH")
    @ApiModelProperty("资源的宽度")
    private String resourceWidth;


}
