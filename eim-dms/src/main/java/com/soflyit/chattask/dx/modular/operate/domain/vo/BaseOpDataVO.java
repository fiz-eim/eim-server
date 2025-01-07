package com.soflyit.chattask.dx.modular.operate.domain.vo;

import com.soflyit.chattask.dx.modular.folder.domain.vo.FolderTree;
import com.soflyit.chattask.dx.modular.folder.domain.vo.TreeVO;
import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.operate.domain.vo
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-17  22:09
 * @Description: 
 * @Version: 1.0
 */
@Data
public abstract class BaseOpDataVO {

    private Long userId;


    private String userName;


    private String nickName;
    private OpTypeVO opTypeVO;
    private FolderTree pathTree;
    private List<TreeVO> pathList;
    private String targetName;


    public abstract String desc();

}
