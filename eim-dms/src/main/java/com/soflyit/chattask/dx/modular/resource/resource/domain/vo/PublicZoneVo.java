package com.soflyit.chattask.dx.modular.resource.resource.domain.vo;

import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.resource.resource.domain.vo
 * @Author: JiangNing.G
 * @CreateTime: 2024-02-23  10:10
 * @Description: 
 * @Version: 1.0
 */
@Data
public class PublicZoneVo {
    private boolean isPublicZone;
    private FolderEntity folder;
}
