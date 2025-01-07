package com.soflyit.chattask.dx.modular.menu.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Package: com.soflyit.chattask.dx.modular.menu.domain.param
 *
 * @Description:
 * @date: 2023/12/18 18:06
 * @author: dddgoal@163.com
 */
@ApiModel
@Data
public class DxMenuAddParam {



    private Long dxmId;


    @ApiModelProperty("菜单名称")
    private String dxmName;


    @ApiModelProperty("是否系统级")
    private Integer isSys ;


    @ApiModelProperty("权限类型1私人2公共3系统")
    private Integer permissionType;


    @ApiModelProperty("是否展示")
    private Integer isShow;


    @ApiModelProperty("菜单图标")
    private String dxmIcon;


    @ApiModelProperty("菜单类型")
    private String dxmType;




    @ApiModelProperty("备注")
    private String remark;
}
