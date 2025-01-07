package com.soflyit.chattask.dx.modular.folder.my.domain.vo;

import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.folder.my.domain.vo
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-18  00:07
 * @Description: 
 * @Version: 1.0
 */
@Data
public class AddFolderVO {
    private FolderEntity folder;
    private ResourceEntity resource;
}
