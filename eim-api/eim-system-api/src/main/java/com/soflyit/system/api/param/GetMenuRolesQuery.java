package com.soflyit.system.api.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ps
 * @version 1.0
 * @date 2022/6/6 11:21
 */
@Data
@ApiModel
public class GetMenuRolesQuery {
    @ApiModelProperty("应用ID")
    private Integer appId;

    @ApiModelProperty("菜单ID")
    private Integer menuId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("页码")
    @NotNull
    private Integer pageNum;

    @ApiModelProperty("步长")
    @NotNull
    private Integer pageSize;
}
