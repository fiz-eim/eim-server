package com.soflyit.chattask.dx.modular.resource.resource.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统资源文档库对象 dx_resource
 *
 * @author soflyit
 * @date 2023-11-07 16:56:57
 */
@ApiModel
@Data
public class ResourceAddParam {
    @ApiModelProperty("文档库存储ID;dx_folder 表 FOLDER_ID 字段")
    private Long folderParentId;

    @ApiModelProperty("资源名称;资源名称")
    private String resourceName;


    @ApiModelProperty("类型")
    private String type;
}
