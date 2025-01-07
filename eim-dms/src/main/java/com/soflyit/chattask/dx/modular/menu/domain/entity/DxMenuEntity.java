package com.soflyit.chattask.dx.modular.menu.domain.entity;

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
 * 系统资源文档库对象 dx_resource
 *
 * @author soflyit
 * @date 2023-11-07 16:56:57
 */
@TableName(value = "dx_menu")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class DxMenuEntity extends DmsCommonEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "主键")
    @TableId("dxm_id")
    @ApiModelProperty("主键")
    private Long dxmId;


    @Excel(name = "菜单名称")
    @TableField(value = "dxm_name")
    @ApiModelProperty("菜单名称")
    private String dxmName;


    @Excel(name = "是否系统级")
    @TableField(value = "is_sys")
    @ApiModelProperty("是否系统级")
    private Integer isSys;


    @Excel(name = "是否展示")
    @TableField(value = "is_show")
    @ApiModelProperty("是否展示")
    private Integer isShow;


    @Excel(name = "权限类型1私人2公共3系统")
    @TableField(value = "permission_type")
    @ApiModelProperty("权限类型1私人2公共3系统")
    private Integer permissionType;


    @Excel(name = "菜单图标")
    @TableField(value = "dxm_icon")
    @ApiModelProperty("菜单图标")
    private String dxmIcon;


    @Excel(name = "菜单类型")
    @TableField(value = "dxm_type")
    @ApiModelProperty("菜单类型")
    private String dxmType;




    @Excel(name = "备注")
    @TableField(value = "remark")
    @ApiModelProperty("备注")
    private String remark;

}
