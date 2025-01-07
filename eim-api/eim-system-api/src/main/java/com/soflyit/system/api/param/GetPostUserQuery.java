package com.soflyit.system.api.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ps
 * @version 1.0
 * @date 2022/6/6 13:59
 */
@Data
@ApiModel
public class GetPostUserQuery {
    @ApiModelProperty("应用ID")
    private Integer appId;

    @ApiModelProperty("岗位ID")
    private Integer postId;

    @ApiModelProperty("用户名称")
    private String nickName;

    @ApiModelProperty("手机号")
    private String phoneNumber;

    @ApiModelProperty("页码")
    @NotNull
    private Integer pageNum;

    @ApiModelProperty("步长")
    @NotNull
    private Integer pageSize;
}
