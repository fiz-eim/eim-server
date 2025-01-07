package com.soflyit.chattask.dx.modular.menu.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Package: com.soflyit.chattask.dx.modular.menu.domain.param
 *
 * @Description:
 * {@code @date:} 2023/12/18 18:06
 * @author: dddgoal@163.com
 */
@ApiModel
@Data
public class DxMenuUpdateParam extends DxMenuAddParam {

    @ApiModelProperty("主键")
    private Long dxmId;


    @ApiModelProperty("菜单名称")
    private String dxmName;


    @ApiModelProperty("权限类型1私人2公共3系统")
    private Integer permissionType;


    @ApiModelProperty("菜单类型")
    private String dxmType;

}
