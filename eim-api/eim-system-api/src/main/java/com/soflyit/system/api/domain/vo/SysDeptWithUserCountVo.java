package com.soflyit.system.api.domain.vo;

import com.soflyit.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author soflyit
 * @description 部门及所含用户视图
 * @date 2023/5/11
 **/
@ApiModel
@Data
public class SysDeptWithUserCountVo {


    @ApiModelProperty("部门Id")
    private Long deptId;


    @ApiModelProperty("部门名称")
    @Excel(name = "部门名称")
    private String deptName;


    @ApiModelProperty("父部门ID")
    private Long parentId;


    @ApiModelProperty("部门下用户数")
    private Integer userCount;
}
