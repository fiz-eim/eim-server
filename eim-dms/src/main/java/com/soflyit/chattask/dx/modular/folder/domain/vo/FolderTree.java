package com.soflyit.chattask.dx.modular.folder.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.tree
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-10  18:12
 * @Description: 
 * @Version: 1.0
 */
@Data
public class FolderTree extends TreeVO{
    private List<FolderTree> nodes;
}
