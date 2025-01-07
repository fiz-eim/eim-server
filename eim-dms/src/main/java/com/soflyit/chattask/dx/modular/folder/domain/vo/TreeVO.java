package com.soflyit.chattask.dx.modular.folder.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.tree.vo
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-10  21:44
 * @Description: 
 * @Version: 1.0
 */
@Data
public class TreeVO {


    @ApiModelProperty("主键")
    private Long folderId;


    @ApiModelProperty("层级;格式：[FOLDER_ID1,FOLDER_ID2,FOLDER_ID3,FOLDER_ID4]")
    private String folderLevel;


    @ApiModelProperty("目录名称")
    private String folderName;


    @ApiModelProperty("上级目录ID;默认规范 ： 0为顶级目录")
    private Long folderParentId;


}
