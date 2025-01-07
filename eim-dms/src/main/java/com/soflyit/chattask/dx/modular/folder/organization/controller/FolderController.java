package com.soflyit.chattask.dx.modular.folder.organization.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.dx.common.CheckVO;
import com.soflyit.chattask.dx.common.base.IdsParam;
import com.soflyit.chattask.dx.common.tree.TreeUtils;
import com.soflyit.chattask.dx.modular.file.domain.param.FolderIMEditParam;
import com.soflyit.chattask.dx.modular.file.domain.param.UploadFileParam;
import com.soflyit.chattask.dx.modular.folder.domain.FolderAddParam;
import com.soflyit.chattask.dx.modular.folder.domain.vo.FolderVO;
import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import com.soflyit.chattask.dx.modular.folder.organization.domain.param.FolderQueryParam;
import com.soflyit.chattask.dx.modular.folder.organization.service.FolderService;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceEditParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceQueryParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.vo.ResourceVO;
import com.soflyit.chattask.dx.modular.resource.resource.service.ResourceService;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档库存储目录Controller
 *
 * @author soflyit
 * @date 2023-11-08 14:54:35
 */
@RestController
@RequestMapping("/folder")
@Slf4j
@Api(tags = {"文档库"})
public class FolderController extends BaseController {
    @Resource
    private FolderService folderService;
    @Resource
    private ResourceService resourceService;



    @GetMapping("/tree")
    @ApiOperation("查询文档库存储目录列表")
    public AjaxResult tree(Long code, boolean isTree) {
        if (ObjectUtil.isEmpty(code)) {
            code = 3L;
        }
        List<FolderEntity> all = folderService.list();

        return AjaxResult.success(isTree ? TreeUtils.toTree(all, code) : TreeUtils.toList(all, code));
    }


    @PostMapping(value = "/getFolderIdByFlag")
    @ApiOperation("查询文件夹id")
    @ApiImplicitParam(name = "queryParam", value = "查询实体", dataType = "FolderQueryParam", paramType = "path")
    public AjaxResult getFolderIdByFlag(@RequestBody FolderQueryParam queryParam) {

        return AjaxResult.success(folderService.getFolderIdByFlag(queryParam));
    }


    @GetMapping("/list")
    @ApiOperation("查询文档库存储目录列表")
    public AjaxResult list() {
        LambdaQueryWrapper<FolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FolderEntity::getFolderParentId, 0);

        return AjaxResult.success(this.folderService.list(queryWrapper));

    }


    @GetMapping(value = "/{id}")
    @ApiOperation("获取文档库存储目录详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
          return AjaxResult.success(folderService.getById(id));
    }


    @PostMapping(value = "/resource")
    @ApiOperation("获取文档库存储目录详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult resource(@RequestBody FolderQueryParam queryParam) {

        CheckVO check = check(queryParam.getFolderId());
        if (!check.isSuccess()) {
            return AjaxResult.error(check.getMessage());
        }
        LambdaQueryWrapper<ResourceEntity> queryWrapper = resourceService.queryWrapper(queryParam);
        queryWrapper.eq(ResourceEntity::getFolderParentId, queryParam.getFolderId());

        return AjaxResult.success(resourceService.listDetail(queryWrapper));
    }


    @GetMapping("/selectByFolderName")
    @ApiOperation("根据文件夹名-查询")
    @ApiImplicitParam(name = "queryParam", value = "详细信息", dataType = "ResourceQueryParam")
    public AjaxResult selectByFolderName(@RequestParam("pFolderId") Long pFolderId, @RequestParam("folderName") String folderName) {
        LambdaQueryWrapper<FolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FolderEntity::getFolderParentId, pFolderId);
        queryWrapper.eq(FolderEntity::getFolderName, folderName);
        FolderEntity one = folderService.getOne(queryWrapper);


        return AjaxResult.success(one);
    }

    @PostMapping("/recent")
    @ApiOperation("最近使用-查询")
    @ApiImplicitParam(name = "queryParam", value = "详细信息", dataType = "ResourceQueryParam")
    public AjaxResult recent(@RequestBody ResourceQueryParam queryParam) {
        FolderQueryParam folderQueryParam = new FolderQueryParam();
        folderQueryParam.setFolderId(1L);

        return AjaxResult.success(resourceService.recent(folderQueryParam));
    }


    @PostMapping("/addFolder/noAuth")
    @ApiOperation("文档库-新增文件夹")
    @ApiImplicitParam(name = "addParam", value = "新增文件夹", dataType = "FolderAddParam")
    public AjaxResult addNoAuth(@RequestBody FolderAddParam addParam) {


        if (folderService.isDuplicateName(addParam.getFolderParentId(), addParam.getFolderName())) {
            return AjaxResult.error(addParam.getFolderName() + " 已存在");
        }
        addParam.setSkipAuth(true);

        return AjaxResult.success(BeanUtil.toBean(folderService.orgAdd(addParam, true), FolderVO.class));
    }


    @PostMapping("/addFolder")
    @ApiOperation("文档库-新增文件夹")
    @ApiImplicitParam(name = "addParam", value = "新增文件夹", dataType = "FolderAddParam")
    public AjaxResult add(@RequestBody FolderAddParam addParam) {

        if (folderService.isDuplicateName(addParam.getFolderParentId(), addParam.getFolderName())) {
            return AjaxResult.error(addParam.getFolderName() + " 已存在");
        }

        return AjaxResult.success(BeanUtil.toBean(folderService.orgAdd(addParam, false), FolderVO.class));
    }


    private CheckVO check(Long folderId) {

        FolderEntity folder = folderService.getById(folderId);
        if (ObjectUtil.isEmpty(folder)) {
            return CheckVO.error("未查询到目录信息");
        }

        return CheckVO.success("校验成功");
    }





    @PostMapping("/update")
    @ApiOperation("修改文档库存储目录")
    @ApiImplicitParam(name = "dxFolder", value = "根据ID修改详情", dataType = "DxFolder")
    public AjaxResult edit(@RequestBody ResourceEditParam editParam) {


        if (folderService.isDuplicateName(editParam.getFolderParentId(), editParam.getResourceName())) {
            return AjaxResult.error(editParam.getResourceName() + " 已存在");
        }


        return AjaxResult.success(resourceService.update(editParam));
    }

    @PostMapping("/updateIMFolder")
    @ApiOperation("修改聊天文件夹名称")
    @ApiImplicitParam(name = "param", value = "修改聊天文件夹名称", dataType = "FolderEditParam")
    public AjaxResult updateIMFolder(@RequestBody FolderIMEditParam param) {
        ResourceEditParam editParam = new ResourceEditParam();
        ResourceEntity resource = resourceService.getByFolderId(param.getFolderId());
        editParam.setOldName(resource.getResourceName());
        editParam.setResourceName(param.getFolderName());
        editParam.setResourceId(resource.getResourceId());
        editParam.setFolderParentId(resource.getFolderParentId());

        return AjaxResult.success(resourceService.update(editParam));
    }



    @PostMapping("/remove")
    @ApiOperation("删除文档库存储目录")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@RequestBody IdsParam<Long> ids) {


        return toAjax(folderService.removeByIds(ids, SecurityUtils.getUserId()));
    }

    @PostMapping(value = "/upload/noAuth", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("文档库-文件上传")
    @ApiImplicitParam(name = "file", value = "上传文件", dataType = "MultipartFile", paramType = "path", allowMultiple = true)
    public AjaxResult uploadNoAuth(UploadFileParam uploadFileParam) {
        Long[] folderParentIds = uploadFileParam.getFolderParentIds();
        if (folderParentIds == null) {
            return AjaxResult.error("父目录不能为空");
        }
        List<ResourceVO> result = new ArrayList<>();
        for (Long folderParentId : folderParentIds) {
            uploadFileParam.setFolderParentId(folderParentId);
            List<ResourceVO> resourceVOList = resourceService.add(uploadFileParam.getFiles(), uploadFileParam, true);
            result.addAll(resourceVOList);
        }
        return AjaxResult.success(result);
    }



    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("文档库-文件上传")
    @ApiImplicitParam(name = "file", value = "上传文件", dataType = "MultipartFile", paramType = "path", allowMultiple = true)
    public AjaxResult upload(UploadFileParam uploadFileParam) {
        Long[] folderParentIds = uploadFileParam.getFolderParentIds();
        if (folderParentIds == null) {
            return AjaxResult.error("父目录不能为空");
        }
        List<ResourceVO> result = new ArrayList<>();
        for (Long folderParentId : folderParentIds) {
            uploadFileParam.setFolderParentId(folderParentId);

            CheckVO check = check(uploadFileParam.getFolderParentId());
            if (!check.isSuccess()) {
                return AjaxResult.error(check.getMessage());
            }
            List<ResourceVO> resourceVOList = resourceService.add(uploadFileParam.getFiles(), uploadFileParam, false);
            result.addAll(resourceVOList);
        }
        return AjaxResult.success(result);
    }



    @PostMapping(value = "/uploadFolder")
    @ApiOperation("文档库-文件夹上传")
    @ApiImplicitParam(name = "file", value = "上传文件夹", dataType = "MultipartFile", paramType = "path", allowMultiple = true)
    public AjaxResult uploadFolder(UploadFileParam uploadFileParam, String sourceFolderPath) {

        CheckVO check = check(uploadFileParam.getFolderParentId());
        if (!check.isSuccess()) {
            return AjaxResult.error(check.getMessage());
        }



        File sourceFolder = new File(sourceFolderPath);

        if (folderService.isDuplicateName(uploadFileParam.getFolderParentId(), sourceFolder.getName())) {
            return AjaxResult.error(sourceFolder.getName() + " 已存在");
        }

        return toAjax(resourceService.uploadFolder(sourceFolderPath, uploadFileParam, false));
    }


}
