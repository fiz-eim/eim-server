package com.soflyit.chattask.dx.modular.folder.organization.domain.param;

import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.folder.organization.domain.param
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-10  11:14
 * @Description: 
 * @Version: 1.0
 */
@Data
public class FolderEditParam {
    private Long folderParentId;
    private Long folderId;
    private String folderName;
}
