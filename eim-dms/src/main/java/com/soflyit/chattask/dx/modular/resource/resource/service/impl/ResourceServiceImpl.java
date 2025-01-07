package com.soflyit.chattask.dx.modular.resource.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.dx.common.base.OrderByParam;
import com.soflyit.chattask.dx.common.base.RangeParam;
import com.soflyit.chattask.dx.common.enums.AppLevelEnum;
import com.soflyit.chattask.dx.common.enums.DxConstant;
import com.soflyit.chattask.dx.common.remoteApi.RemoteUserUtil;
import com.soflyit.chattask.dx.common.tree.TreeUtils;
import com.soflyit.chattask.dx.common.utils.FileUtils;
import com.soflyit.chattask.dx.common.utils.ResourceUtils;
import com.soflyit.chattask.dx.modular.file.domain.entity.OriginalFileEntity;
import com.soflyit.chattask.dx.modular.file.domain.param.UploadFileParam;
import com.soflyit.chattask.dx.modular.file.service.FileTypeService;
import com.soflyit.chattask.dx.modular.file.service.OriginalFileService;
import com.soflyit.chattask.dx.modular.folder.domain.vo.FolderTree;
import com.soflyit.chattask.dx.modular.folder.my.domain.entity.MyFolderEntity;
import com.soflyit.chattask.dx.modular.folder.my.domain.vo.AddFolderVO;
import com.soflyit.chattask.dx.modular.folder.my.service.MyFolderService;
import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import com.soflyit.chattask.dx.modular.folder.organization.domain.param.FolderQueryParam;
import com.soflyit.chattask.dx.modular.folder.organization.service.FolderService;
import com.soflyit.chattask.dx.modular.menu.domain.entity.DxMenuEntity;
import com.soflyit.chattask.dx.modular.menu.service.DxMenuService;
import com.soflyit.chattask.dx.modular.operate.domain.enums.OpTypeEnum;
import com.soflyit.chattask.dx.modular.operate.domain.vo.AddOpDataVO;
import com.soflyit.chattask.dx.modular.operate.domain.vo.EditOpDataVO;
import com.soflyit.chattask.dx.modular.operate.domain.vo.OpTypeVO;
import com.soflyit.chattask.dx.modular.operate.domain.vo.ReMoveOpBodyVO;
import com.soflyit.chattask.dx.modular.operate.service.OperateRecordService;
import com.soflyit.chattask.dx.modular.resource.recycle.service.ResourceRecycleService;
import com.soflyit.chattask.dx.modular.resource.resource.domain.ResourceAddParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceDetailParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceEditParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceQueryParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.vo.PublicZoneVo;
import com.soflyit.chattask.dx.modular.resource.resource.domain.vo.ResourceDetailVo;
import com.soflyit.chattask.dx.modular.resource.resource.domain.vo.ResourceVO;
import com.soflyit.chattask.dx.modular.resource.resource.mapper.ResourceMapper;
import com.soflyit.chattask.dx.modular.resource.resource.service.ResourceService;
import com.soflyit.chattask.dx.modular.storage.StorageService;
import com.soflyit.common.core.utils.SpringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.soflyit.chattask.dx.modular.operate.domain.enums.OpTypeEnum.*;


/**
 * 系统资源文档库Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-07 16:56:57
 */
@Slf4j
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, ResourceEntity> implements ResourceService {

    final Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    @Resource
    OriginalFileService originalFileService;
    @Resource
    private DxMenuService dxMenuService;
    @Resource
    FolderService folderService;
    @Resource
    private FileTypeService fileTypeService;
    @Resource
    MyFolderService myFolderService;
    @Resource
    private ResourceMapper resourceMapper;
    @Resource
    private RemoteUserUtil remoteUserUtil;
    @Resource
    OperateRecordService operateRecordService;
    public static Map<Long, FolderEntity> FOLDER_MAP = null;
    @Resource
    private ResourceRecycleService resourceRecycleService;

    private StorageService storageService;


    @Override
    public List<ResourceEntity> getParentAndSelf(Long folderId) {
        return resourceMapper.getParentAndSelf(folderId);
    }

    @Override
    public List<ResourceEntity> getChildAndSelf(Long folderId) {
        if (folderId == null) {
            return new ArrayList<>();
        }
        return resourceMapper.getChildAndSelf(folderId);
    }


    public void init(List<FolderEntity> all) {
        FOLDER_MAP = all.stream().collect(Collectors.toMap(FolderEntity::getFolderId, item -> item));
    }


    private String path(Long id) {
        if (ObjectUtil.equals(id, 0L) || FOLDER_MAP == null) {
            return "/";
        }
        FolderEntity folder = FOLDER_MAP.get(id);
        if (folder == null) {
            return "/";
        }

        String folderLevel = folder.getFolderLevel();
        List<Long> levelList = JSON.parseArray(folderLevel, Long.class);
        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < levelList.size(); i++) {
            if (0 != levelList.get(i)) {
                ret.append(FOLDER_MAP.get(levelList.get(i)) != null ? FOLDER_MAP.get(levelList.get(i)).getFolderName() + "/" : "");
            }
        }

        return ret.toString();
    }


    @Override
    public ResourceEntity getByFolderId(Long folderId) {
        LambdaQueryWrapper<ResourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ResourceEntity::getFolderId, folderId);
        return resourceMapper.selectOne(wrapper);
    }

    @Override
    public LambdaQueryWrapper<ResourceEntity> queryWrapper(ResourceQueryParam queryParam) {
        LambdaQueryWrapper<ResourceEntity> queryWrapper = new LambdaQueryWrapper<>();
        OrderByParam<String[]> mimeType = queryParam.getMimeType();
        if (ObjectUtil.isNotEmpty(mimeType)) {
            Integer orderType = mimeType.getOrderType();
            String[] fieldValue = mimeType.getFieldValue();
            if (ObjectUtil.isNotEmpty(fieldValue)) {
                queryWrapper.in(ResourceEntity::getMimeType, Arrays.asList(fieldValue));
            }
            if (ObjectUtil.isNotEmpty(orderType) && 1 == orderType) {
                queryWrapper.orderByAsc(ResourceEntity::getMimeType);
            } else if (ObjectUtil.isNotEmpty(orderType) && 0 == orderType) {
                queryWrapper.orderByDesc(ResourceEntity::getMimeType);
            }
        }
        OrderByParam<String[]> resourceExt = queryParam.getResourceExt();
        if (ObjectUtil.isNotEmpty(resourceExt)) {
            Integer orderType = resourceExt.getOrderType();
            String[] fieldValue = resourceExt.getFieldValue();
            if (ObjectUtil.isNotEmpty(fieldValue)) {
                queryWrapper.in(ResourceEntity::getResourceExt, Arrays.asList(fieldValue));
            }
            if (ObjectUtil.isNotEmpty(orderType) && 1 == orderType) {
                queryWrapper.orderByAsc(ResourceEntity::getResourceExt);
            } else if (ObjectUtil.isNotEmpty(orderType) && 0 == orderType) {
                queryWrapper.orderByDesc(ResourceEntity::getResourceExt);
            }
        }

        OrderByParam<String> resourceName = queryParam.getResourceName();
        if (ObjectUtil.isNotEmpty(resourceName)) {
            Integer orderType = resourceName.getOrderType();
            String fieldValue = resourceName.getFieldValue();
            if (ObjectUtil.isNotEmpty(fieldValue)) {
                queryWrapper.like(ResourceEntity::getResourceName, fieldValue);
            }
            if (ObjectUtil.isNotEmpty(orderType) && 1 == orderType) {
                queryWrapper.orderByAsc(ResourceEntity::getResourceName);
            } else if (ObjectUtil.isNotEmpty(orderType) && 0 == orderType) {
                queryWrapper.orderByDesc(ResourceEntity::getResourceName);
            }
        }

        OrderByParam<RangeParam<String>> resourceSize = queryParam.getResourceSize();
        if (ObjectUtil.isNotEmpty(resourceSize)) {
            Integer orderType = resourceSize.getOrderType();
            RangeParam<String> fieldValue = resourceSize.getFieldValue();
            if (ObjectUtil.isNotEmpty(fieldValue.getRangeStart())) {
                queryWrapper.gt(ResourceEntity::getResourceSize, fieldValue.getRangeStart());
            }
            if (ObjectUtil.isNotEmpty(fieldValue.getRangeEnd())) {
                queryWrapper.lt(ResourceEntity::getResourceSize, fieldValue.getRangeEnd());
            }
            if (ObjectUtil.isNotEmpty(orderType) && 1 == orderType) {
                queryWrapper.orderByAsc(ResourceEntity::getResourceSize);
            } else if (ObjectUtil.isNotEmpty(orderType) && 0 == orderType) {
                queryWrapper.orderByDesc(ResourceEntity::getResourceSize);
            }
        }

        OrderByParam<String[]> resourceType = queryParam.getResourceType();
        if (ObjectUtil.isNotEmpty(resourceType)) {
            Integer orderType = resourceType.getOrderType();
            String[] fieldValue = resourceType.getFieldValue();
            if (ObjectUtil.isNotEmpty(fieldValue)) {
                List<String> allByFileType = fileTypeService.getAllByFileType(fieldValue);
                queryWrapper.in(ResourceEntity::getResourceType, allByFileType);
            }
            if (ObjectUtil.isNotEmpty(orderType) && 1 == orderType) {
                queryWrapper.orderByAsc(ResourceEntity::getResourceType);
            } else if (ObjectUtil.isNotEmpty(orderType) && 0 == orderType) {
                queryWrapper.orderByDesc(ResourceEntity::getResourceType);
            }
        }

        OrderByParam<RangeParam<String>> createTime = queryParam.getResourceSize();
        if (ObjectUtil.isNotEmpty(createTime)) {
            Integer orderType = createTime.getOrderType();
            RangeParam<String> fieldValue = createTime.getFieldValue();
            if (ObjectUtil.isNotEmpty(fieldValue.getRangeStart())) {
                queryWrapper.gt(ResourceEntity::getCreateTime, fieldValue.getRangeStart());
            }
            if (ObjectUtil.isNotEmpty(fieldValue.getRangeEnd())) {
                queryWrapper.lt(ResourceEntity::getCreateTime, fieldValue.getRangeEnd());
            }
            if (ObjectUtil.isNotEmpty(orderType) && 1 == orderType) {
                queryWrapper.orderByAsc(ResourceEntity::getCreateTime);
            } else if (ObjectUtil.isNotEmpty(orderType) && 0 == orderType) {
                queryWrapper.orderByDesc(ResourceEntity::getCreateTime);
            }
        }

        OrderByParam<RangeParam<String>> updateTime = queryParam.getResourceSize();
        if (ObjectUtil.isNotEmpty(updateTime)) {
            Integer orderType = updateTime.getOrderType();
            RangeParam<String> fieldValue = updateTime.getFieldValue();
            if (ObjectUtil.isNotEmpty(fieldValue.getRangeStart())) {
                queryWrapper.gt(ResourceEntity::getUpdateTime, fieldValue.getRangeStart());
            }
            if (ObjectUtil.isNotEmpty(fieldValue.getRangeEnd())) {
                queryWrapper.lt(ResourceEntity::getUpdateTime, fieldValue.getRangeEnd());
            }
            if (ObjectUtil.isNotEmpty(orderType) && 1 == orderType) {
                queryWrapper.orderByAsc(ResourceEntity::getUpdateTime);
            } else if (ObjectUtil.isNotEmpty(orderType) && 0 == orderType) {
                queryWrapper.orderByDesc(ResourceEntity::getUpdateTime);
            }
        }
        return queryWrapper;
    }



    @Override
    public List<ResourceVO> list(ResourceQueryParam queryParam) {
        LambdaQueryWrapper<ResourceEntity> queryWrapper = queryWrapper(queryParam);

        return this.listDetail(queryWrapper);

    }

    @Override
    public List<ResourceVO> recent(FolderQueryParam queryParam) {
        return new ArrayList<>();
    }



    public List<ResourceVO> getResourceVOByType(Integer deleteFlag, List<Long> resourceIds, Long folderParentId) {
        List<ResourceVO> result = new ArrayList<>();
        List<FolderEntity> all = folderService.folderList(SecurityUtils.getUserId());
        init(all);

        List<ResourceEntity> parentAndSelf = this.getParentAndSelf(folderParentId);
        List<ResourceVO> parents = convertEntity(parentAndSelf, all);
        result.addAll(parents);

        List<ResourceEntity> resources = resourceMapper.getResourceByDelete(resourceIds, deleteFlag);
        List<ResourceVO> childs = convertEntity(resources, all);
        for (Long resourceId : resourceIds) {
            childs.stream().filter(i -> ObjectUtil.equals(resourceId, i.getResourceId())).findFirst().ifPresent(result::add);
        }

        return result;
    }

    @Override
    public List<ResourceVO> recycle(FolderQueryParam queryParam) {
        List<ResourceVO> result = new ArrayList<>();
        List<Long> collect = resourceRecycleService.getResourceIds(SecurityUtils.getUserId());
        if (CollUtil.isEmpty(collect)) {
            return result;
        }
        return getResourceVOByType(Integer.parseInt(DxConstant.STATUS_IS_NO), collect, queryParam.getFolderId());
    }

    @Override
    public ResourceDetailVo getResourceInfo(ResourceDetailParam resourceDetailParam) {
        ResourceEntity resource = resourceMapper.selectById(resourceDetailParam.getResourceId());

        if (resource == null) {
            throw new RuntimeException("未查询到此目录信息");
        }
        if (resourceDetailParam.getPageSize() == null) {
            resourceDetailParam.setPageSize(10);
        }
        if (resourceDetailParam.getPageNum() == null) {
            resourceDetailParam.setPageNum(1);
        }
        ResourceDetailVo result = new ResourceDetailVo();
        new ResourceVO();
        ResourceVO resourceVO;
        FolderEntity parentFolder = folderService.getById(resource.getFolderParentId());
        if (JSON.parseArray(parentFolder.getFolderLevel(), Long.class).size() == 2
                && JSON.parseArray(parentFolder.getFolderLevel(), Long.class).get(0) == 0) {
            resourceVO = convertEntity(ListUtil.toList(resource), ListUtil.toList(parentFolder)).get(0);

        } else {

            List<FolderEntity> all = folderService.list();
            resourceVO = convertEntity(ListUtil.toList(resource), all).get(0);
        }

        if (resourceVO != null) {
            setVoCreate(resourceVO);
            result.setBaseInfo(resourceVO);
        }
        return result;
    }


    public void setVoCreate(ResourceVO resourceVO) {
        String createName = remoteUserUtil.getUserNickNameByUserId(resourceVO.getCreateBy());
        resourceVO.setCreateNickName(createName);

        String updateName = remoteUserUtil.getUserNickNameByUserId(resourceVO.getUpdatedBy());
        resourceVO.setUpdateNickName(updateName);
    }


    @Override
    public List<ResourceVO> listDetail(LambdaQueryWrapper<ResourceEntity> queryWrapper) {
        List<FolderEntity> all = folderService.folderList(SecurityUtils.getUserId());


        return convertEntity(this.baseMapper.selectList(queryWrapper), all);
    }


    @Override
    public List<ResourceVO> list(LambdaQueryWrapper<ResourceEntity> queryWrapper, Long parentFolderId) {
        FOLDER_MAP = null;
        List<Long> folderIds = new ArrayList<>();
        folderIds.add(parentFolderId);
        List<FolderEntity> all = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(folderIds)) {
            all = folderService.folderList(folderIds, parentFolderId);
        }
        init(all);
        PublicZoneVo publicZoneVo = folderService.isPublicZone(parentFolderId);
        if (!publicZoneVo.isPublicZone()) {
            queryWrapper.eq(ResourceEntity::getCreateBy, SecurityUtils.getUserId());
        }
        List<ResourceEntity> parentAndSelf = this.getParentAndSelf(parentFolderId);
        List<ResourceEntity> childList = this.baseMapper.selectList(queryWrapper);
        List<ResourceEntity> childListPermission = new ArrayList<>();
        JSONArray jsonArray = JSONUtil.parseArray(publicZoneVo.getFolder().getFolderLevel());
        String secendLevel = jsonArray.getStr(1);
        DxMenuEntity byId = dxMenuService.getById(secendLevel);
        if (byId.getPermissionType() != 1) {

            LambdaQueryWrapper<ResourceEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ResourceEntity::getFolderParentId, parentFolderId);
            List<ResourceEntity> resourceByUser = this.baseMapper.selectList(wrapper);

            for (ResourceEntity resource : resourceByUser) {
                if (resource.getFolderId() != null) {
                    if (folderIds.contains(resource.getFolderId())) {
                        childListPermission.add(resource);
                    }
                }
            }

            List<ResourceEntity> collect = resourceByUser.stream().filter(i -> i.getFolderId() == null).collect(Collectors.toList());
            childListPermission.addAll(collect);
        } else {
            childListPermission = this.baseMapper.selectList(queryWrapper);
        }

        List<ResourceVO> childVos = convertEntity(childListPermission, all);
        List<ResourceVO> parentAndSelfVos = convertEntity(parentAndSelf, all);
        parentAndSelfVos.addAll(childVos);
        return parentAndSelfVos;
    }

    private List<ResourceVO> convertEntity(List<ResourceEntity> list, List<FolderEntity> all) {
        return list.stream().map(
                item -> {
                    ResourceVO resourceVO = BeanUtils.convertBean(item, ResourceVO.class);
                    resourceVO.setResourceSize(Convert.toStr(item.getResourceSize()));
                    resourceVO.setLevelPath(path(item.getFolderParentId()));
                    String permissions = "";
                    resourceVO.setPermContent(permissions);

                    if (!ObjectUtil.equals(item.getFolderParentId(), 0L)) {

                        List<FolderEntity> collect = all.stream().filter(folder -> folder.getFolderId().equals(item.getFolderParentId())).collect(Collectors.toList());
                        if (CollUtil.isNotEmpty(collect)) {
                            FolderEntity folder = collect.get(0);
                            FolderTree tree = TreeUtils.toTree(all, JSON.parseArray(folder.getFolderLevel(), Long.class));
                            resourceVO.setLevelTree(tree);
                            return resourceVO;
                        }


                    } else {
                        return resourceVO;
                    }

                    return null;
                }
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<ResourceVO> add(MultipartFile[] files, UploadFileParam uploadFileParam, Boolean skipAuth) {

        Long reassignFolderId = folderService.reassignFolderParent(uploadFileParam.getRelativePath(), uploadFileParam.getFolderParentId(), skipAuth);
        uploadFileParam.setFolderParentId(reassignFolderId);
        List<OriginalFileEntity> originalFiles = new ArrayList<>();
        List<ResourceEntity> resources = new ArrayList<>();
        List<ResourceVO> result = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile multipartFile = files[i];
            String fileName = FileNameUtil.getName(multipartFile.getOriginalFilename());
            if (this.isDuplicateName(uploadFileParam.getFolderParentId(), fileName)) {
                fileName = this.reName(uploadFileParam.getFolderParentId(), fileName);
            }

            long id = snowflake.nextId();
            OriginalFileEntity originalFile = storageService.multipartFileToEntry(multipartFile);
            String path = storageService.saveFile(fileName, multipartFile);
            log.info("文件转换后的实体:{}", JSON.toJSONString(originalFile));
            originalFile.setOriginalId(id);
            originalFile.setOriginalName(fileName);
            originalFile.setOriginalPath(path);
            originalFile.setOriginalType(uploadFileParam.getType());
            originalFiles.add(originalFile);


            ResourceEntity resource = new ResourceEntity();
            resource.setResourceId(snowflake.nextId());
            resource.setFolderParentId(uploadFileParam.getFolderParentId());
            resource.setOriginalId(id);
            resource.setMd5(snowflake.nextIdStr());
            resource.setResourceName(fileName);
            resource.setMimeType(originalFile.getMimeType());
            resource.setResourceType(originalFile.getFileType());
            resource.setResourceExt(originalFile.getFileExt());
            resource.setResourceSize(originalFile.getOriginalSize());
            resources.add(resource);
        }
        originalFileService.saveBatch(originalFiles);
        SpringUtils.getAopProxy(this).saveBatch(resources);



        resources.forEach(item -> {

            ReMoveOpBodyVO opBodyVO = new ReMoveOpBodyVO();
            opBodyVO.setOpTypeVO(OpTypeVO.instance(FILE_UPLOAD));
            operateRecordService.saveByData(opBodyVO, item, uploadFileParam.getFolderParentId());
            ResourceVO resourceVO = BeanUtil.toBean(item, ResourceVO.class);
            result.add(resourceVO);
        });
        log.debug("添加 resource 完成");
        return result;
    }

    @Transactional
    @Override
    public boolean uploadFolder(String sourceFolderPath, UploadFileParam uploadFileParam, boolean isMy) {
        File sources = new File(sourceFolderPath);

        AddFolderVO addFolderVO = createFolder(sources, uploadFileParam, isMy);


        AddOpDataVO opBodyVO = new AddOpDataVO();
        opBodyVO.setOpTypeVO(OpTypeVO.instance(FOLDER_UPLOAD));
        operateRecordService.saveByData(opBodyVO, addFolderVO.getResource(), uploadFileParam.getFolderParentId());

        return true;
    }

    @Override
    public ResourceVO update(ResourceEditParam editParam) {

        OpTypeEnum opTypeEnum = FILE_RENAME;

        ResourceEntity resource = this.getById(editParam.getResourceId());
        if (resource.getResourceType().equals(AppLevelEnum.FOLDER.getLevel())) {



            FolderEntity folder = new FolderEntity();
            folder.setFolderId(resource.getFolderId());
            folder.setFolderName(editParam.getResourceName());
            folderService.updateById(folder);
            opTypeEnum = FOLDER_RENAME;
        }


        ResourceEntity edit = BeanUtils.convertBean(editParam, ResourceEntity.class);


        EditOpDataVO opBodyVO = new EditOpDataVO();
        opBodyVO.setOpTypeVO(OpTypeVO.instance(opTypeEnum));
        opBodyVO.setReName(editParam.getResourceName());
        opBodyVO.setOldName(resource.getResourceName());
        operateRecordService.saveByData(opBodyVO, edit, resource.getFolderParentId());
        this.updateById(edit);
        return BeanUtils.convertBean(edit, ResourceVO.class);
    }

    @Override
    public String reName(Long folderParentId, String resourceName) {
        String newName = resourceName;
        if (isDuplicateName(folderParentId, newName)) {
            String idx = getDuplicateFileNameIndex(folderParentId, resourceName);
            String[] split = resourceName.split("\\.", 2);
            newName = split[0] + "(" + idx + ")." + split[1];
        }

        return newName;
    }

    @Override
    public boolean isDuplicateName(Long folderParentId, String resourceName) {
        LambdaQueryWrapper<ResourceEntity> queryWrapper = new LambdaQueryWrapper<>();
        folderParentId = ObjectUtil.isEmpty(folderParentId) ? 0 : folderParentId;
        queryWrapper.eq(ResourceEntity::getFolderParentId, folderParentId);
        queryWrapper.eq(ResourceEntity::getResourceName, resourceName);

        return this.count(queryWrapper) > 0;
    }


    private String getDuplicateFileNameIndex(Long folderParentId, String resourceName) {
        LambdaQueryWrapper<ResourceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ResourceEntity::getResourceId, ResourceEntity::getResourceName);
        folderParentId = ObjectUtil.isEmpty(folderParentId) ? 0 : folderParentId;
        queryWrapper.eq(ResourceEntity::getFolderParentId, folderParentId);
        String resouceNameParam = resourceName;
        if (resouceNameParam.matches("(.*?)\\(\\d+\\)(\\..*)?")) {
            resouceNameParam = resourceName.substring(0, resourceName.lastIndexOf('('));
        }
        queryWrapper.likeRight(ResourceEntity::getResourceName, resouceNameParam);
        List<ResourceEntity> resourceEntities = getBaseMapper().selectList(queryWrapper);
        Pattern pattern = Pattern.compile(".*?\\((\\d+)\\)");
        List<Integer> indexList = new ArrayList<>();
        for (ResourceEntity resourceEntity : resourceEntities) {
            String itemName = resourceEntity.getResourceName();
            Matcher matcher = pattern.matcher(itemName);
            if (matcher.find()) {
                indexList.add(Integer.valueOf(matcher.group(1)));
            }
        }
        indexList.sort(Integer::compare);
        int index = indexList.size() + 1;
        for (int i = 0; i < indexList.size(); i++) {
            if (i + 1 < indexList.get(i)) {
                index = i + 1;
                break;
            }
        }

        return String.valueOf(index);
    }


    @Override
    public void saveFile(Long resourceId, HttpServletRequest request, HttpServletResponse response) {
        ResourceEntity resource = this.getById(resourceId);
        if (resource.getFolderId() != null) {
            throw new RuntimeException("参数错误: 不是文件");
        }

        Long folderParentId = resource.getFolderParentId();


        long id = snowflake.nextId();
    }


    @Override
    public void getImageScale(Long originId, String scale, HttpServletResponse resp) {
        OriginalFileEntity byId = originalFileService.getById(originId);
        float sc = scale == null ? 0.5f : Float.parseFloat(scale);
        if (sc < 0) {
            sc = 0.5f;
        }
        sc = Math.min(sc, 3f);
        try {
            InputStream is = getImgInputStream(originId);
            resp.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImgUtil.scale(is, resp.getOutputStream(), sc);
        } catch (Exception e) {
            log.error("生成缩略图错误:" + e.getMessage(), e);
        }
    }


    private InputStream getImgInputStream(Long originId) {
        OriginalFileEntity byId = originalFileService.getById(originId);
        InputStream is;
        if (byId == null) {
            is = ResourceUtil.getStream("./defaul-thumbnail.png");
        } else {
            is = storageService.getInputStream(byId);
        }
        return is;
    }


    @Override
    public void getImageScale(Long originId, String w, String h, HttpServletResponse resp) {
        OriginalFileEntity byId = originalFileService.getById(originId);
        w = Optional.ofNullable(w).orElse("200");
        h = Optional.ofNullable(h).orElse("100");

        int width = Math.min(Integer.parseInt(w), 4096);
        int height = Math.min(Integer.parseInt(h), 2160);
        try {
            InputStream is = getImgInputStream(originId);
            resp.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImgUtil.scale(is, resp.getOutputStream(), width, height, null);
        } catch (Exception e) {
            log.error("生成缩略图错误:" + e.getMessage(), e);
        }
    }


    @Override
    public List<ResourceVO> addResource(ResourceAddParam addParam) {
        UploadFileParam uploadFileParam = new UploadFileParam();
        BeanUtils.copyBeanProp(uploadFileParam, addParam);
        MultipartFile[] files = null;
        if ("word".equalsIgnoreCase(addParam.getType())) {
            files = new MultipartFile[]{ResourceUtils.createWord(addParam.getResourceName())};
        } else if ("excel".equalsIgnoreCase(addParam.getType())) {
            files = new MultipartFile[]{ResourceUtils.createExcel(addParam.getResourceName())};
        } else if ("ppt".equalsIgnoreCase(addParam.getType())) {
            files = new MultipartFile[]{ResourceUtils.createPowerPoint(addParam.getResourceName())};
        } else if ("mind".equalsIgnoreCase(addParam.getType())) {
            files = new MultipartFile[]{ResourceUtils.createMind(addParam.getResourceName().concat(".").concat(addParam.getType()), "{}")};
        } else if ("drawio".equalsIgnoreCase(addParam.getType())) {
            files = new MultipartFile[]{ResourceUtils.createDrawio(addParam.getResourceName().concat(".").concat(addParam.getType()), "")};
        }
        if (ArrayUtils.isNotEmpty(files)) {
            return add(files, uploadFileParam, false);
        } else {
            log.warn("添加资源失败：文件不能为空");
        }
        return new ArrayList<>();
    }


    @Override
    public void downloadFileOrFolder(Long resourceId, HttpServletResponse response, Boolean skipAuth) {
        ResourceEntity resourceEntity = this.getById(resourceId);
        if (resourceEntity.getResourceType().equals(DxConstant.RESOURCE_FOLDER_TYPE)) {

            String tmp = FileUtils.mkdir(FileUtils.getFilePath(), "tmp");
            tmp = FileUtils.mkdir(tmp, SecurityUtils.getUserId() + "_" + RandomUtil.randomString(4));
            createTmpZipFile(resourceEntity.getFolderId(), tmp);
            File file = ZipUtil.zip(tmp + resourceEntity.getResourceName());
            FileUtils.downLoadFile(file, response, resourceEntity.getResourceName() + ".zip");
            FileUtil.del(file);
            FileUtil.del(tmp);
            return;
        }

        downloadFile(resourceId, response);
    }


    public void createTmpZipFile(Long folderId, String baseDir) {
        LambdaQueryWrapper<ResourceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ResourceEntity::getFolderParentId, folderId);
        List<ResourceVO> list = list(queryWrapper, folderId);


        ResourceVO entity = list.stream().filter(i -> ObjectUtil.equals(folderId, i.getFolderId())).collect(Collectors.toList()).get(0);
        baseDir = FileUtils.mkdir(baseDir, entity.getResourceName());

        List<ResourceVO> childs = list.stream().filter(i -> ObjectUtil.equals(folderId, i.getFolderParentId())).collect(Collectors.toList());
        for (ResourceVO child : childs) {
            if (DxConstant.RESOURCE_FOLDER_TYPE.equals(child.getResourceType())) {
                createTmpZipFile(child.getFolderId(), baseDir);
            } else {
                copyFile(child, baseDir);
            }

        }
    }



    public void copyFile(ResourceVO child, String baseDir) {
        OriginalFileEntity byId = originalFileService.getById(child.getOriginalId());
        if (byId != null) {
            InputStream is = storageService.getInputStream(byId);
            if (is != null) {
                File dest = new File(baseDir + child.getResourceName());
                FileUtil.writeFromStream(is, dest);
            }
        }
    }

    public void folder(ResourceEntity entity, String baseDir) {
        FileUtils.mkdir(baseDir, entity.getResourceName());
        LambdaQueryWrapper<ResourceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ResourceEntity::getCreateBy, SecurityUtils.getUserId());

    }

    @Override
    public void downloadFile(Long resourceId, HttpServletResponse response) {
        ResourceEntity resourceEntity = this.getById(resourceId);
        if (resourceEntity == null) {
            throw new RuntimeException("资源不存在");
        }

        Long originalId = resourceEntity.getOriginalId();

        OriginalFileEntity file = originalFileService.getById(originalId);
        if (file == null) {
            throw new RuntimeException("文件不存在");
        }
        FileUtils.downLoadFile(storageService.getInputStream(file), response, resourceEntity.getResourceName(), file);
    }

    @Override
    public ResourceEntity createFile(ResourceAddParam addParam) throws IOException {
        File file = null;
        String suffixType = "";
        FileOutputStream out;
        long id = snowflake.nextId();
        switch (addParam.getType()) {
            case "word":
                suffixType = ".docx";
                XWPFDocument document = new XWPFDocument();

                file = new File(FileUtils.getFilePath() + id + suffixType);
                out = new FileOutputStream(file);
                document.write(out);
                out.close();
                break;
            case "excel":
                suffixType = ".xlsx";
                Workbook workbook = new XSSFWorkbook();
                workbook.createSheet("Sheet1");
                file = new File(FileUtils.getFilePath() + id + suffixType);

                out = new FileOutputStream(file);

                workbook.write(new FileOutputStream(file));
                out.close();

                break;
            case "ppt":
                suffixType = ".pptx";
                XMLSlideShow ppt = new XMLSlideShow();
                file = new File(FileUtils.getFilePath() + id + suffixType);

                out = new FileOutputStream(file);
                ppt.write(out);
                out.close();
                break;
            default:
        }

        if (file == null) {
            log.error("创建文件失败:{}", JSON.toJSONString(addParam));
            return null;
        }

        MultipartFile multipartFile = FileUtils.getMultipartFile(file);
        OriginalFileEntity originalFile = storageService.multipartFileToEntry(multipartFile);
        String path = storageService.saveFile(file.getName(), multipartFile);
        originalFile.setOriginalId(id);
        originalFile.setOriginalName(addParam.getResourceName());
        log.info("文件转换后的实体:{}", JSON.toJSONString(originalFile));
        originalFile.setOriginalPath(path);
        originalFile.setOriginalType(FileUtils.getFileType(file));
        originalFileService.save(originalFile);


        ResourceEntity resource = new ResourceEntity();
        resource.setResourceId(snowflake.nextId());
        resource.setFolderParentId(addParam.getFolderParentId());
        resource.setOriginalId(id);
        resource.setMd5(snowflake.nextIdStr());
        resource.setResourceName(addParam.getResourceName() + suffixType);
        resource.setMimeType(originalFile.getMimeType());
        resource.setResourceType(originalFile.getFileType());
        resource.setResourceExt(originalFile.getFileExt());
        resource.setResourceSize(originalFile.getOriginalSize());
        resource.setDeleteFlag(Integer.parseInt(DxConstant.STATUS_IS_NO));
        this.save(resource);

        return resource;
    }

    private AddFolderVO createFolder(File source, UploadFileParam uploadFileParam, boolean isMy) {
        AddFolderVO ret = new AddFolderVO();

        if (source.isDirectory()) {


            long folderId = snowflake.nextId();
            FolderEntity folderEntity = new FolderEntity();
            folderEntity.setFolderName(source.getName());
            folderEntity.setFolderId(folderId);
            folderEntity.setFolderParentId(uploadFileParam.getFolderParentId());
            folderService.add(folderEntity);
            ret.setFolder(folderEntity);


            ResourceEntity resource = new ResourceEntity();
            resource.setResourceId(snowflake.nextId());
            resource.setFolderParentId(folderId);
            resource.setOriginalId(snowflake.nextId());
            resource.setMd5(snowflake.nextIdStr());
            resource.setResourceName(source.getName());
            resource.setMimeType(FileUtils.getFileType(source));
            resource.setResourceType(FileUtils.getFileType(source));
            resource.setResourceExt(FileUtils.getFileType(source));
            resource.setResourceSize(source.length());
            this.save(resource);
            ret.setResource(resource);
            if (isMy) {

                MyFolderEntity myFolder = new MyFolderEntity();
                myFolder.setUserId(String.valueOf(SecurityUtils.getUserId()));
                myFolder.setFolderId(folderId);
                myFolderService.save(myFolder);
            }

            uploadFileParam.setFolderParentId(folderEntity.getFolderId());
            createFiles(source.listFiles(), uploadFileParam, isMy);
        } else {

            throw new RuntimeException("请传入文件夹");
        }
        return ret;
    }

    private void createFiles(File[] files, UploadFileParam uploadFileParam, boolean isMy) {
        if (ArrayUtils.isEmpty(files)) {
            return;
        }


        List<OriginalFileEntity> originalFiles = new ArrayList<>();
        List<ResourceEntity> resources = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                createFolder(files[i], uploadFileParam, isMy);
            } else {

                Long id = snowflake.nextId();
                File file = FileUtils.copyFile(files[i], FileUtils.getFilePath(), String.valueOf(id));

                OriginalFileEntity originalFile = originalFileService.convert(files[i]);
                originalFile.setOriginalId(id);
                if (ObjectUtil.isEmpty(originalFile.getMimeType())) {
                    originalFile.setMimeType(originalFile.getFileType());
                }
                originalFile.setOriginalName(files[i].getName());
                originalFile.setOriginalPath(files[i].getAbsolutePath());
                originalFile.setOriginalType(uploadFileParam.getType());
                originalFiles.add(originalFile);


                ResourceEntity resource = new ResourceEntity();
                resource.setResourceId(snowflake.nextId());
                resource.setFolderParentId(uploadFileParam.getFolderParentId());
                resource.setOriginalId(id);
                resource.setMd5(snowflake.nextIdStr());
                resource.setResourceName(files[i].getName());
                resource.setMimeType(originalFile.getMimeType());
                resource.setResourceType(originalFile.getFileType());
                resource.setResourceExt(originalFile.getFileExt());
                resource.setResourceSize(originalFile.getOriginalSize());
                resources.add(resource);
            }
        }
        if (CollectionUtils.isNotEmpty(resources)) {
            SpringUtils.getAopProxy(this).saveBatch(resources);
        }
        if (CollectionUtils.isNotEmpty(originalFiles)) {
            originalFileService.saveBatch(originalFiles);
        }
    }

    @Autowired
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }
}
