package com.soflyit.system.api.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ps
 * @version 1.0
 * @date 2022/6/6 13:49
 */
@Data
@ApiModel
public class GetRolePostsQuery {
    @ApiModelProperty("应用ID")
    private Integer appId;

    @ApiModelProperty("角色ID")
    private Integer roleId;

    @ApiModelProperty("岗位名称")
    private String postName;

    @ApiModelProperty("页码")
    @NotNull
    private Integer pageNum;

    @ApiModelProperty("步长")
    @NotNull
    private Integer pageSize;
}
