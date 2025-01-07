package com.soflyit.chattask.dx.modular.resource.resource.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.dx.modular.file.domain.param.UploadFileParam;
import com.soflyit.chattask.dx.modular.folder.organization.domain.param.FolderQueryParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.ResourceAddParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceDetailParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceEditParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceQueryParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.vo.ResourceDetailVo;
import com.soflyit.chattask.dx.modular.resource.resource.domain.vo.ResourceVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 系统资源文档库Service接口
 *
 * @author soflyit
 * @date 2023-11-07 16:56:57
 */
public interface ResourceService extends IService<ResourceEntity> {
    LambdaQueryWrapper<ResourceEntity> queryWrapper(ResourceQueryParam queryParam);



    ResourceEntity getByFolderId(Long folderId);


    List<ResourceEntity> getParentAndSelf(Long folderId);


    List<ResourceEntity> getChildAndSelf(Long folderId);

    List<ResourceVO> list(ResourceQueryParam queryParam);


    List<ResourceVO> recent(FolderQueryParam queryParam);


    List<ResourceVO> recycle(FolderQueryParam queryParam);


    ResourceDetailVo getResourceInfo(ResourceDetailParam resourceDetailParam);

    List<ResourceVO> listDetail(LambdaQueryWrapper<ResourceEntity> queryWrapper);


    List<ResourceVO> list(LambdaQueryWrapper<ResourceEntity> queryWrapper, Long parentFolderId);

    List<ResourceVO>  add(MultipartFile[] file, UploadFileParam uploadFileParam, Boolean skipAuth);

    boolean uploadFolder(String sourceFolderPath, UploadFileParam uploadFileParam,boolean isMy);

    ResourceVO update(ResourceEditParam editParam);

    String reName(Long folderParentId, String resourceName);

    boolean isDuplicateName(Long folderParentId, String resourceName);

    ResourceEntity createFile(ResourceAddParam addParam) throws IOException;


    void downloadFile(Long resourceId, HttpServletResponse response);


    void downloadFileOrFolder(Long resourceId, HttpServletResponse response, Boolean skipAuth);


    void saveFile(Long resourceId, HttpServletRequest request, HttpServletResponse response);


    void getImageScale(Long originId, String scale, HttpServletResponse resp) ;


    void getImageScale(Long originId, String w, String h , HttpServletResponse resp);

    List<ResourceVO> addResource(ResourceAddParam addParam);

}
