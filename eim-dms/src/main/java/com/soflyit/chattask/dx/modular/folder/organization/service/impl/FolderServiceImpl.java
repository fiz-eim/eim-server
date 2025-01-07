package com.soflyit.chattask.dx.modular.folder.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.dx.common.base.IdsParam;
import com.soflyit.chattask.dx.common.enums.AppLevelEnum;
import com.soflyit.chattask.dx.common.tree.TreeUtils;
import com.soflyit.chattask.dx.modular.file.service.OriginalFileService;
import com.soflyit.chattask.dx.modular.folder.domain.FolderAddParam;
import com.soflyit.chattask.dx.modular.folder.domain.vo.FolderVO;
import com.soflyit.chattask.dx.modular.folder.domain.vo.TreeVO;
import com.soflyit.chattask.dx.modular.folder.my.domain.entity.MyFolderEntity;
import com.soflyit.chattask.dx.modular.folder.my.domain.vo.AddFolderVO;
import com.soflyit.chattask.dx.modular.folder.my.service.MyFolderService;
import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import com.soflyit.chattask.dx.modular.folder.organization.domain.param.FolderQueryParam;
import com.soflyit.chattask.dx.modular.folder.organization.mapper.FolderMapper;
import com.soflyit.chattask.dx.modular.folder.organization.service.FolderService;
import com.soflyit.chattask.dx.modular.menu.service.DxMenuService;
import com.soflyit.chattask.dx.modular.operate.domain.entity.OperateRecordEntity;
import com.soflyit.chattask.dx.modular.operate.domain.vo.AddOpDataVO;
import com.soflyit.chattask.dx.modular.operate.domain.vo.OpTypeVO;
import com.soflyit.chattask.dx.modular.operate.domain.vo.ReMoveOpBodyVO;
import com.soflyit.chattask.dx.modular.operate.service.OperateRecordService;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.domain.vo.PublicZoneVo;
import com.soflyit.chattask.dx.modular.resource.resource.service.ResourceService;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.soflyit.chattask.dx.modular.operate.domain.enums.OpTypeEnum.*;
import static com.soflyit.common.core.utils.SpringUtils.getAopProxy;


/**
 * 文档库存储目录Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-08 14:54:35
 */
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, FolderEntity> implements FolderService {
    @Resource
    OperateRecordService operateRecordService;
    @Resource
    FolderMapper folderMapper;
    final Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    @Resource
    MyFolderService myFolderService;
    @Resource
    DxMenuService dxMenuService;
    @Resource
    ResourceService resourceService;
    @Resource
    OriginalFileService originalFileService;



    @Override
    public String reName(Long folderParentId, String folderName, int index) {

        String newName = folderName;
        if (isDuplicateName(folderParentId, folderName)) {
            index++;
            String[] split = folderName.split("\\.", 2);
            newName = split[0] + "(" + index + ")" + split[1];
            reName(folderParentId, folderName, index);
        }

        return newName;
    }

    @Override
    public Long createOrSkipFolder(FolderAddParam addParam) {
        LambdaQueryWrapper<FolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FolderEntity::getFolderParentId, addParam.getFolderParentId());
        queryWrapper.eq(FolderEntity::getFolderName, addParam.getFolderName());
        List<FolderEntity> list = this.list(queryWrapper);
        if (list.size() == 1) {
            return list.get(0).getFolderId();
        }
        if (CollectionUtils.isEmpty(list)) {

            FolderVO folderVO = getAopProxy(this).addMy(addParam);
            return folderVO.getFolderId();
        }

        return addParam.getFolderParentId();
    }

    @Override
    public boolean isDuplicateName(Long folderParentId, String folderName) {

        LambdaQueryWrapper<FolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        folderParentId = ObjectUtil.isEmpty(folderParentId) ? 0 : folderParentId;
        queryWrapper.eq(FolderEntity::getFolderParentId, folderParentId);
        queryWrapper.eq(FolderEntity::getFolderName, folderName);

        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<FolderEntity> list(FolderQueryParam queryParam) {
        LambdaQueryWrapper<FolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        Long folderParentId = ObjectUtil.isEmpty(queryParam.getFolderParentId()) ? 0 : queryParam.getFolderParentId();
        queryWrapper.eq(FolderEntity::getFolderParentId, folderParentId);
        return this.list(queryWrapper);
    }

    @Override
    public PublicZoneVo isPublicZone(Long folderId) {
        PublicZoneVo res = new PublicZoneVo();
        FolderEntity folderEntity = folderMapper.selectById(folderId);
        if (folderEntity == null) {
            throw new RuntimeException("文件夹不存在");
        }
        res.setPublicZone(isPublicZone(folderEntity.getFolderLevel()));
        res.setFolder(folderEntity);
        return res;
    }

    @Override
    public Boolean isPublicZone(String folderLevel) {
        JSONArray jsonArray = JSONUtil.parseArray(folderLevel);
        String secendLevel = jsonArray.getStr(1);
        String[] publicZones = dxMenuService.getPublicZoneFolderId().split(",");
        return ArrayUtil.contains(publicZones, secendLevel);
    }

    @Override
    public Boolean isSystemZone(String folderLevel) {
        JSONArray jsonArray = JSONUtil.parseArray(folderLevel);
        String secendLevel = jsonArray.getStr(1);
        String[] publicZones = dxMenuService.getNoValidateFolderId().split(",");
        return ArrayUtil.contains(publicZones, secendLevel);
    }

    @Override
    @Transactional
    public FolderVO addMy(FolderAddParam addParam) {
        boolean publicZone = isPublicZone(addParam.getFolderParentId()).isPublicZone();
        if (publicZone) {
            return BeanUtil.toBean(orgAdd(addParam, addParam.getSkipAuth()), FolderVO.class);
        }

        long folderId = snowflake.nextId();
        FolderEntity folderEntity = BeanUtils.convertBean(addParam, FolderEntity.class);
        folderEntity.setFolderFlag(AppLevelEnum.FOLDER.getLevel());
        folderEntity.setFolderId(folderId);


        MyFolderEntity myFolder = new MyFolderEntity();
        myFolder.setUserId(String.valueOf(SecurityUtils.getUserId()));
        myFolder.setFolderId(folderId);
        AddFolderVO ret = this.add(folderEntity);
        myFolderService.save(myFolder);

        return BeanUtils.convertBean(folderEntity, FolderVO.class);
    }


    @Override
    public List<FolderEntity> listByTop2This(Long folderId) {
        List<FolderEntity> result = new ArrayList<>();

        FolderEntity folderEntity = getById(folderId);
        String folderLevel = folderEntity.getFolderLevel();
        JSONArray jsonArray = JSONUtil.parseArray(folderLevel);
        for (int i = 0; i < jsonArray.size() - 1; i++) {
            Long folder = jsonArray.getLong(i);
            if (folder == 0) {
                continue;
            }
            FolderEntity byId = getById(folder);
            result.add(byId);
        }
        result.add(folderEntity);
        return result;
    }

    @Override
    public AddFolderVO add(FolderEntity folderEntity) {
        AddFolderVO ret = new AddFolderVO();
        List<String> level = new ArrayList<>();



        if (ObjectUtils.isEmpty(folderEntity.getFolderParentId()) || "0".equals(String.valueOf(folderEntity.getFolderParentId()))) {

            folderEntity.setFolderParentId(0L);
            level.add("0");
            folderEntity.setFolderLevel(level.toString());
        } else {

            FolderEntity parentFolder = this.baseMapper.selectById(folderEntity.getFolderParentId());
            level = JSON.parseArray(parentFolder.getFolderLevel(), String.class);
            level.add(String.valueOf(folderEntity.getFolderId()));
            folderEntity.setFolderLevel(level.toString());


            ResourceEntity resource = new ResourceEntity();
            resource.setResourceId(snowflake.nextId());
            resource.setFolderParentId(folderEntity.getFolderParentId());
            resource.setResourceName(folderEntity.getFolderName());
            String resourceType = StrUtil.isEmpty(folderEntity.getFolderFlag()) ? AppLevelEnum.FOLDER.getLevel() : folderEntity.getFolderFlag();
            resource.setResourceType(resourceType);
            resource.setFolderId(folderEntity.getFolderId());
            resourceService.save(resource);
            ret.setResource(resource);
        }
        this.save(folderEntity);
        ret.setFolder(folderEntity);



        List<FolderEntity> folders = this.listByIds(JSON.parseArray(folderEntity.getFolderLevel(), Long.class));
        folders.remove(folders.size() - 1);
        AddOpDataVO addOpBodyVO = new AddOpDataVO();
        addOpBodyVO.setPathList(BeanUtils.convertList(folders, TreeVO.class));
        addOpBodyVO.setOpTypeVO(OpTypeVO.instance(FOLDER_ADD));
        operateRecordService.saveByData(addOpBodyVO, ret.getResource(), null);
        return ret;
    }


    public Long reassignFolderParent(String relativePath, Long folderParentId, Boolean skipAuth) {
        if (StrUtil.isEmpty(relativePath)) {
            return folderParentId;
        }

        String[] paths = relativePath.split("/");

        for (String path : paths) {
            if (StrUtil.isBlank(path)) {
                continue;
            }
            FolderAddParam addParam = new FolderAddParam();

            addParam.setFolderParentId(folderParentId);
            addParam.setFolderName(path);
            addParam.setSkipAuth(skipAuth);
            folderParentId = createOrSkipFolder(addParam);
        }
        return folderParentId;

    }


    @Override
    public FolderEntity orgAdd(FolderAddParam addParam, Boolean skipAuth) {
        Long userId = SecurityUtils.getUserId();
        long folderId = snowflake.nextId();


        FolderEntity folder = BeanUtils.convertBean(addParam, FolderEntity.class);

        folder.setFolderParentId(reassignFolderParent(addParam.getRelativePath(), addParam.getFolderParentId(), addParam.getSkipAuth()));
        String folderFlag = addParam.getFolderFlag() == null ? AppLevelEnum.FOLDER.getLevel() : addParam.getFolderFlag();
        folder.setFolderFlag(folderFlag);
        folder.setFolderId(folderId);


        AddFolderVO add = this.add(folder);



        return folder;
    }


    @Override
    public List<FolderEntity> listByParentIds(List<Long> ids) {
        List<FolderEntity> list = new ArrayList<>();
        List<FolderEntity> parents = this.listByIds(ids);
        list.addAll(parents);
        parents.forEach(item -> getChildren(item.getFolderId(), list));

        return list;
    }

    private void getChildren(Long id, List<FolderEntity> list) {
        LambdaQueryWrapper<FolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FolderEntity::getFolderParentId, id);
        List<FolderEntity> parents = this.list(queryWrapper);

        list.addAll(parents);

        parents.forEach(item -> getChildren(item.getFolderId(), list));

    }

    @Override
    public List<Long> folderIdsByParentIds(List<Long> ids) {

        return this.listByParentIds(ids).stream().map(FolderEntity::getFolderId).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean removeByIds(IdsParam<Long> idsParam, Long userId) {
        List<OperateRecordEntity> opList = new ArrayList<>();


        Set<Long> folderIds = new HashSet<>(this.folderIds(userId));

        List<ResourceEntity> resList = resourceService.listByIds(idsParam.getIds());
        List<Long> folders = resList.stream()
                .filter(item -> item.getResourceType().equals(AppLevelEnum.FOLDER.getLevel()))
                .map(ResourceEntity::getFolderId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(folders)) {
            if (!folderIds.containsAll(folders)) {

                throw new RuntimeException("文件中存在非个人文件，请刷新重试");
            }

            this.removeFolders(folders, resList, false);
        }


        List<Long> oIds = resList.stream()
                .filter(item -> !item.getResourceType().equals(AppLevelEnum.FOLDER.getLevel()))
                .map(ResourceEntity::getOriginalId).collect(Collectors.toList());
        originalFileService.removeBatchByIds(oIds);


        List<Long> resourceIds = resList.stream()
                .filter(item -> !item.getResourceType().equals(AppLevelEnum.FOLDER.getLevel()))
                .map(ResourceEntity::getResourceId).collect(Collectors.toList());

        resourceService.removeByIds(resourceIds);


        List<ResourceEntity> collect = resList.stream()
                .filter(item -> !item.getResourceType().equals(AppLevelEnum.FOLDER.getLevel())).collect(Collectors.toList());
        List<Long> fIds = collect.stream().map(ResourceEntity::getFolderParentId).distinct().collect(Collectors.toList());
        List<FolderEntity> folderEntityList = this.listByIds(fIds);


        collect.forEach(item -> {


            List<FolderEntity> fs = this.listByIds(JSON.parseArray(folderEntityList.stream().filter(f -> f.getFolderId().equals(item.getFolderParentId())).findFirst().get().getFolderLevel(), Long.class));
            ReMoveOpBodyVO opBodyVO = new ReMoveOpBodyVO();
            opBodyVO.setPathTree(TreeUtils.toTree(fs, 0L));
            opBodyVO.setOpTypeVO(OpTypeVO.instance(FILE_REMOVE));
            ResourceEntity resource = resList.stream().filter(res -> res.getFolderId().equals(item.getFolderId())).findFirst().get();
            operateRecordService.saveByData(opBodyVO, resource, null);
        });
        operateRecordService.saveBatch(opList);
        return true;
    }


    public List<FolderEntity> folderList(Long userId, Long parentFolderId) {
        List<FolderEntity> top2This = listByTop2This(parentFolderId);
        FolderQueryParam queryParam = new FolderQueryParam();
        queryParam.setFolderParentId(parentFolderId);
        List<FolderEntity> childs = list(queryParam);
        top2This.addAll(childs);
        return top2This;
    }

    @Override
    public List<FolderEntity> folderList(List<Long> folderIds, Long parentFolderId) {
        List<FolderEntity> top2This = listByTop2This(parentFolderId);
        LambdaQueryWrapper<FolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FolderEntity::getFolderParentId, parentFolderId);
        queryWrapper.in(FolderEntity::getFolderId, folderIds);
        top2This.addAll(this.list(queryWrapper));
        return top2This;
    }




    @Override
    public List<Long> folderIds(Long userId) {

        LambdaQueryWrapper<MyFolderEntity> myLambdaQueryWrapper = new LambdaQueryWrapper<>();
        myLambdaQueryWrapper.eq(MyFolderEntity::getUserId, userId);
        List<MyFolderEntity> myFolders = myFolderService.list(myLambdaQueryWrapper);
        List<Long> myFolderIds = myFolders.stream().map(MyFolderEntity::getFolderId).collect(Collectors.toList());

        List<Long> folderIds = new ArrayList<>();
        folderIds.addAll(myFolderIds);
        folderIds.add(1L);
        folderIds.add(2L);
        folderIds.add(3L);
        folderIds.add(4L);
        folderIds.add(5L);

        return folderIds;
    }


    @Override
    public List<FolderEntity> folderList(Long userId) {
        return this.listByIds(this.folderIds(userId));
    }


    @Override
    public FolderEntity getFolderIdByFlag(FolderQueryParam queryParam) {
        if (queryParam.getFolderParentId() == null || queryParam.getFolderFlag() == null) {
            throw new RuntimeException("参数错误");
        }
        LambdaQueryWrapper<FolderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FolderEntity::getFolderParentId, queryParam.getFolderParentId());
        wrapper.eq(FolderEntity::getFolderFlag, queryParam.getFolderFlag());
        return folderMapper.selectOne(wrapper);
    }

    @Override
    public boolean removeFolders(List<Long> folderIds, List<ResourceEntity> resList, boolean isMy) {

        List<Long> fIds = this.folderIdsByParentIds(folderIds);


        LambdaQueryWrapper<ResourceEntity> resourceQueryWrapper = new LambdaQueryWrapper<>();
        resourceQueryWrapper.in(ResourceEntity::getFolderId, fIds);
        List<Long> fileIds = resourceService.list(resourceQueryWrapper).stream().filter(item -> ObjectUtil.isNotEmpty(item.getOriginalId()))
                .map(ResourceEntity::getOriginalId).collect(Collectors.toList());

        originalFileService.removeByIds(fileIds);

        resourceService.remove(resourceQueryWrapper);

        getAopProxy(this).removeByIds(fIds);

        LambdaQueryWrapper<MyFolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MyFolderEntity::getFolderId, fIds);
        myFolderService.remove(queryWrapper);




        List<FolderEntity> folderEntityList = this.listByIds(folderIds);
        folderEntityList.forEach(item -> {

            List<FolderEntity> folders = this.listByIds(JSON.parseArray(item.getFolderLevel(), Long.class));
            folders.remove(folders.size() - 1);
            ReMoveOpBodyVO opBodyVO = new ReMoveOpBodyVO();
            opBodyVO.setPathTree(TreeUtils.toTree(folders, 0L));
            opBodyVO.setOpTypeVO(OpTypeVO.instance(FOLDER_REMOVE));
            operateRecordService.saveByData(opBodyVO, resList.stream().filter(res -> res.getFolderId().equals(item.getFolderId())).findFirst().get(), null);
        });


        return true;

    }

}
