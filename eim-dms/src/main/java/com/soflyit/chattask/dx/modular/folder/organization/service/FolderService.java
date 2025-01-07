package com.soflyit.chattask.dx.modular.folder.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.dx.common.base.IdsParam;
import com.soflyit.chattask.dx.modular.folder.domain.FolderAddParam;
import com.soflyit.chattask.dx.modular.folder.domain.vo.FolderVO;
import com.soflyit.chattask.dx.modular.folder.my.domain.vo.AddFolderVO;
import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import com.soflyit.chattask.dx.modular.folder.organization.domain.param.FolderQueryParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.domain.vo.PublicZoneVo;

import java.util.List;

/**
 * 文档库存储目录Service接口
 *
 * @author soflyit
 * @date 2023-11-08 14:54:35
 */
public interface FolderService extends IService<FolderEntity> {
    List<FolderEntity> list(FolderQueryParam folderParam);



    PublicZoneVo isPublicZone(Long folderId);


    Boolean isPublicZone(String  folderLevel);


    Boolean isSystemZone(String folderLevel);

    FolderVO addMy(FolderAddParam addParam);

    AddFolderVO add(FolderEntity folder);




    List<FolderEntity> listByTop2This(Long folderId);


    Long reassignFolderParent(String relativePath,  Long folderParentId, Boolean skipAuth);

    FolderEntity orgAdd(FolderAddParam addParam, Boolean skipAuth);

    List<FolderEntity> listByParentIds(List<Long> ids);

    List<Long> folderIdsByParentIds(List<Long> ids);


    boolean removeByIds(IdsParam<Long> ids, Long userId);

    List<Long> folderIds(Long userId);

    boolean removeFolders(List<Long> folders, List<ResourceEntity> resList, boolean isMy);

    String reName(Long folderParentId, String folderName, int index);

    boolean isDuplicateName(Long folderParentId, String folderName);


    List<FolderEntity> folderList(List<Long> folderIds, Long parentFolderId);

    Long createOrSkipFolder(FolderAddParam addParam);

    List<FolderEntity> folderList(Long userId);


    FolderEntity getFolderIdByFlag(FolderQueryParam queryParam);
}
