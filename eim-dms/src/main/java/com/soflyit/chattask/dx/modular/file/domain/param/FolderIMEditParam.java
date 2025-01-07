package com.soflyit.chattask.dx.modular.file.domain.param;

import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.api.domain.param
 * @Author: JiangNing.G
 * @CreateTime: 2024-02-23  14:50
 * @Description: 
 * @Version: 1.0
 */
@Data
public class FolderIMEditParam {
    private String folderName;//新名称
    private Long folderId;//目录ID
}
