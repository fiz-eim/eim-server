package com.soflyit.chattask.dx.modular.folder.my.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.dx.common.CheckVO;
import com.soflyit.chattask.dx.common.base.IdsParam;
import com.soflyit.chattask.dx.common.tree.TreeUtils;
import com.soflyit.chattask.dx.modular.file.domain.param.UploadFileParam;
import com.soflyit.chattask.dx.modular.folder.domain.FolderAddParam;
import com.soflyit.chattask.dx.modular.folder.my.domain.entity.MyFolderEntity;
import com.soflyit.chattask.dx.modular.folder.my.service.MyFolderService;
import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import com.soflyit.chattask.dx.modular.folder.organization.domain.param.FolderQueryParam;
import com.soflyit.chattask.dx.modular.folder.organization.service.FolderService;
import com.soflyit.chattask.dx.modular.resource.resource.domain.ResourceAddParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceEditParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceInfoParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.vo.ResourceVO;
import com.soflyit.chattask.dx.modular.resource.resource.service.ResourceService;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.security.auth.AuthUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 我的文档
 *
 * @author soflyit
 * @date 2023-11-08 14:54:35
 */
@RestController
@RequestMapping("/myfolder")
@Slf4j
@Api(tags = {"我的文档"})
public class MyFolderController extends BaseController {
    @Resource
    private FolderService folderService;
    @Resource
    private MyFolderService myFolderService;
    @Resource
    private ResourceService resourceService;


    @GetMapping("/tree")
    @ApiOperation("树形结构")
    public AjaxResult tree(Long code) {
        if (ObjectUtil.isEmpty(code)) {
            code = 2L;
        }

        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<MyFolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyFolderEntity::getUserId, userId);

        List<MyFolderEntity> myFolderEntities = myFolderService.list(queryWrapper);

        List<Long> folderIds = myFolderEntities.stream().map(MyFolderEntity::getFolderId).collect(Collectors.toList());
        List<FolderEntity> all = folderService.listByIds(folderIds);


        return AjaxResult.success(TreeUtils.toTree(all, 2L));
    }


    @GetMapping(value = "/{folderId}")
    @ApiOperation("我的文档-文件夹详情")
    @ApiImplicitParam(name = "folderId", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("folderId") Long folderId) {
        LambdaQueryWrapper<MyFolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyFolderEntity::getFolderId, folderId);
        MyFolderEntity myfolder = myFolderService.getOne(queryWrapper);
        if (ObjectUtil.isEmpty(myfolder)) {
            return AjaxResult.error("未查询到此目录信息");
        }

        return AjaxResult.success(folderService.getById(myfolder.getFolderId()));
    }


    @PostMapping(value = "/resourceInfo")
    @ApiOperation("我的文档-资源详情")
    @ApiImplicitParam(name = "infoParam", value = "主键", dataType = "ResourceInfoParam", paramType = "path")
    public AjaxResult getResourceInfo(@RequestBody ResourceInfoParam infoParam) {

        CheckVO check = check(infoParam.getFolderId());
        if (!check.isSuccess()) {
            return AjaxResult.error(check.getMessage());
        }
        return AjaxResult.success(resourceService.getById(infoParam.getResourceId()));
    }


    @PostMapping(value = "/resources")
    @ApiOperation("我的文档-获取目录下资源")
    @ApiImplicitParam(name = "queryParam", value = "查询实体", dataType = "FolderQueryParam", paramType = "path")
    public AjaxResult resource(@RequestBody FolderQueryParam queryParam) {
        String token = SecurityUtils.getToken();
        LoginUser loginUser;
        if (StringUtils.isNotEmpty(token)) {
            loginUser = AuthUtil.getLoginUser(token);
            if (loginUser == null) {
                return AjaxResult.error("请先登录操作");
            }
        } else {
            return AjaxResult.error("请先登录操作");
        }

        CheckVO check = check(queryParam.getFolderId());
        if (!check.isSuccess()) {
            return AjaxResult.error(check.getMessage());
        }
        if (String.valueOf(queryParam.getFolderId()).equals("1")) {
            return AjaxResult.success(resourceService.recent(queryParam));
        }
        LambdaQueryWrapper<ResourceEntity> queryWrapper = resourceService.queryWrapper(queryParam);
        queryWrapper.eq(ResourceEntity::getFolderParentId, queryParam.getFolderId());
        return AjaxResult.success(resourceService.list(queryWrapper, queryParam.getFolderId()));
    }


    @PostMapping("/addFolder")
    @ApiOperation("我的文档-新增文件夹")
    @ApiImplicitParam(name = "addParam", value = "详细信息", dataType = "FolderAddParam")
    @Transactional
    public AjaxResult addFolder(@RequestBody ResourceAddParam addParam) {

        CheckVO check = check(addParam.getFolderParentId());
        if (!check.isSuccess()) {
            return AjaxResult.error(check.getMessage());
        }

        if (folderService.isDuplicateName(addParam.getFolderParentId(), addParam.getResourceName())) {
            return AjaxResult.error(addParam.getResourceName() + " 已存在");

        }



        FolderAddParam folderAddParam = new FolderAddParam();
        folderAddParam.setFolderParentId(addParam.getFolderParentId());
        folderAddParam.setFolderName(addParam.getResourceName());
        folderAddParam.setSkipAuth(false);


        return AjaxResult.success(folderService.addMy(folderAddParam));
    }

    @PostMapping("/folderExist")
    @ApiOperation("我的文档-验证文件夹是否存在")
    @ApiImplicitParam(name = "addParam", value = "详细信息", dataType = "FolderAddParam")
    public AjaxResult checkFolder(@RequestBody ResourceAddParam addParam) {

        CheckVO check = check(addParam.getFolderParentId());
        if (!check.isSuccess()) {
            return AjaxResult.error(check.getMessage());
        }

        return AjaxResult.success(folderService.isDuplicateName(addParam.getFolderParentId(), addParam.getResourceName()));

    }



    @PostMapping("/update")
    @ApiOperation("修改文档库存储目录")
    @ApiImplicitParam(name = "dxFolder", value = "根据ID修改详情", dataType = "DxFolder")
    public AjaxResult edit(@RequestBody ResourceEditParam editParam) {

        CheckVO check = check(editParam.getFolderParentId());
        if (!check.isSuccess()) {
            return AjaxResult.error(check.getMessage());
        }

        if (ObjectUtil.isEmpty(editParam.getResourceId())) {

            AjaxResult.error("资源ID必填");
        }


        ResourceEntity resource = resourceService.getById(editParam.getResourceId());
        if (!(StrUtil.equals(resource.getResourceName(), editParam.getResourceName())
                && ObjectUtil.equals(resource.getFolderParentId(), editParam.getFolderParentId()))) {

            if (resourceService.isDuplicateName(editParam.getFolderParentId(), editParam.getResourceName())) {
                return AjaxResult.error(editParam.getResourceName() + " 已存在");
            }
        }

        return AjaxResult.success(resourceService.update(editParam));
    }


    @PostMapping("/createFile")
    @ApiOperation("新建文件")
    @ApiImplicitParam(name = "addParam", value = "新建文件", dataType = "ResourceAddParam")
    public AjaxResult createFile(@RequestBody ResourceAddParam addParam) throws IOException {

        CheckVO check = check(addParam.getFolderParentId());
        if (!check.isSuccess()) {
            return AjaxResult.error(check.getMessage());
        }

        if (resourceService.isDuplicateName(addParam.getFolderParentId(), addParam.getResourceName())) {
            return AjaxResult.error(addParam.getResourceName() + " 已存在");
        }


        return AjaxResult.success(resourceService.createFile(addParam));
    }



    @PostMapping("/remove")
    @ApiOperation("删除资源")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应资源", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@RequestBody IdsParam<String> ids) {
        List<String> idsList = ids.getIds();

        return toAjax(myFolderService.removeByIds(ids.getIds(), SecurityUtils.getUserId(), false));
    }




    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("我的文档-文件上传")
    @ApiImplicitParam(name = "file", value = "上传文件", dataType = "MultipartFile", paramType = "path", allowMultiple = true)
    public AjaxResult upload(UploadFileParam uploadFileParam, @RequestParam(value = "files", required = false) MultipartFile[] files)  {

        CheckVO check = check(uploadFileParam.getFolderParentId());
        if (!check.isSuccess()) {
            return AjaxResult.error(check.getMessage());
        }
        if (ArrayUtil.isNotEmpty(files)) {
            uploadFileParam.setFiles(files);
        } else {
            return AjaxResult.success("无文件处理");
        }
        List<ResourceVO> add = resourceService.add(uploadFileParam.getFiles(), uploadFileParam, false);
        return AjaxResult.success(add);
    }


    @PostMapping(value = "/uploadFolder")
    @ApiOperation("我的文档-文件夹上传")
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

        return toAjax(resourceService.uploadFolder(sourceFolderPath, uploadFileParam, true));
    }



    private CheckVO check(Long folderId) {

        FolderEntity folder = folderService.getById(folderId);
        if (ObjectUtil.isEmpty(folder)) {
            return CheckVO.error("未查询到目录信息");
        }
        Boolean systemZone = folderService.isSystemZone(folder.getFolderLevel());
        if (systemZone) {
            return CheckVO.success("校验成功");
        }
        boolean publicZone = folderService.isPublicZone(folder.getFolderLevel());
        long count = 0;
        String msg = "未查询到目录信息";
        if (publicZone) {
            if (JSON.parseArray(folder.getFolderLevel(), Long.class).size() == 2
                    && JSON.parseArray(folder.getFolderLevel(), Long.class).get(0) == 0) {
                count = 1;
            }
        } else {


            if (JSON.parseArray(folder.getFolderLevel(), Long.class).size() == 2
                    && JSON.parseArray(folder.getFolderLevel(), Long.class).get(0) == 0) {
                count = 1;
            } else {
                LambdaQueryWrapper<MyFolderEntity> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(MyFolderEntity::getUserId, SecurityUtils.getUserId());
                queryWrapper.eq(MyFolderEntity::getFolderId, folderId);
                count = myFolderService.count(queryWrapper);
                msg = "您没有该目录的权限";
            }
        }
        if (!(count > 0)) {
            return CheckVO.error(msg);
        }
        return CheckVO.success("校验成功");
    }

}
