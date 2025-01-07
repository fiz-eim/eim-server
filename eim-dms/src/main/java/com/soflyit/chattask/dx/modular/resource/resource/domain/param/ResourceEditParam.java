package com.soflyit.chattask.dx.modular.resource.resource.domain.param;

import com.soflyit.chattask.dx.modular.resource.resource.domain.ResourceAddParam;
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
public class ResourceEditParam extends ResourceAddParam {



    @ApiModelProperty("主键;资源ID")
    private Long resourceId;

    @ApiModelProperty("资源原来名称")
    private String oldName;






}
