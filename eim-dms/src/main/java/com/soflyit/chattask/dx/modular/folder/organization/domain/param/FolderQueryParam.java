package com.soflyit.chattask.dx.modular.folder.organization.domain.param;

import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文档库存储目录对象 dx_folder
 *
 * @author soflyit
 * @date 2023-11-08 14:54:35
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class FolderQueryParam extends ResourceQueryParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("目录ID")
    private Long folderId;

    @ApiModelProperty("所属目录ID")
    private Long folderParentId;


    @ApiModelProperty("文件夹类型")
    private String folderFlag;

}
