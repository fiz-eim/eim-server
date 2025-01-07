package com.soflyit.chattask.dx.modular.folder.domain;

import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.folder.organization.domain.param
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-10  11:13
 * @Description: 
 * @Version: 1.0
 */
@Data
public class FolderAddParam {


    private String folderName;


    private Long folderParentId;


    private String relativePath;


    private String folderFlag;


    private Boolean skipAuth;


    private Long[] users;


}
