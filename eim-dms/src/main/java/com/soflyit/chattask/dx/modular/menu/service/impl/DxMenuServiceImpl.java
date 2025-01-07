package com.soflyit.chattask.dx.modular.menu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.dx.common.enums.DxConstant;
import com.soflyit.chattask.dx.modular.folder.my.domain.entity.MyFolderEntity;
import com.soflyit.chattask.dx.modular.folder.my.service.MyFolderService;
import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import com.soflyit.chattask.dx.modular.folder.organization.service.FolderService;
import com.soflyit.chattask.dx.modular.menu.domain.entity.DxMenuEntity;
import com.soflyit.chattask.dx.modular.menu.domain.param.DxMenuAddParam;
import com.soflyit.chattask.dx.modular.menu.domain.param.DxMenuUpdateParam;
import com.soflyit.chattask.dx.modular.menu.mapper.DxMenuMapper;
import com.soflyit.chattask.dx.modular.menu.service.DxMenuService;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.service.ResourceService;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Package: com.soflyit.chattask.dx.modular.dx.service.impl
 *
 * @Description:
 * @date: 2023/12/18 9:58
 * @author: dddgoal@163.com
 */
@Service
@Slf4j
public class DxMenuServiceImpl extends ServiceImpl<DxMenuMapper, DxMenuEntity> implements DxMenuService {
    @Resource
    private DxMenuMapper dxMenuMapper;
    @Resource
    private MyFolderService myFolderService;
    @Resource
    private FolderService folderService;
    @Resource
    private ResourceService resourceService;

    final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    @Override
    public DxMenuEntity getInfo(Long id) {
        return dxMenuMapper.selectById(id);
    }


    public String getFolderIdByPerType(Integer type){
        LambdaQueryWrapper<DxMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DxMenuEntity::getPermissionType,type);
        wrapper.select(DxMenuEntity::getDxmId);

        List<DxMenuEntity> dxMenuEntities = dxMenuMapper.selectList(wrapper);
        List<Long> collect = dxMenuEntities.stream().map(DxMenuEntity::getDxmId).collect(Collectors.toList());
        return CollUtil.join(collect, ",");
    }

    @Override
    public String getPublicZoneFolderId() {
        return getFolderIdByPerType(2);
    }

    @Override
    public String getPrivateZoneFolderId() {
        return getFolderIdByPerType(1);
    }

    @Override
    public String getNoValidateFolderId() {
        return getFolderIdByPerType(3);
    }

    @Override
    @Transactional
    public Boolean deleteTopMenu(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)){
            return true;
        }
        this.removeByIds(ids);

        folderService.removeByIds(ids);

        for (Long id : ids) {
            ResourceEntity resource = resourceService.getByFolderId(id);
            if (resource != null){
                resourceService.removeById(resource);
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean updateTopMenu(DxMenuUpdateParam editParam) {
        if (editParam == null || editParam.getDxmId() == null ){
            throw new RuntimeException("参数错误");
        }
        DxMenuEntity dxMenuEntity = BeanUtil.toBean(editParam, DxMenuEntity.class);
        this.updateById(dxMenuEntity);

        FolderEntity folder = folderService.getById(editParam.getDxmId());
        if (folder != null){
            folder.setFolderFlag(dxMenuEntity.getDxmType());
            folder.setFolderLevel("[0," + editParam.getDxmId() + "]");
            folder.setFolderName(dxMenuEntity.getDxmName());
            folder.setUpdateTime(new Date());
            folder.setUpdateBy(SecurityUtils.getUserId());
            folderService.updateById(folder);
        }


        ResourceEntity resource = resourceService.getByFolderId(editParam.getDxmId());
        if (resource != null){
            resource.setResourceName(dxMenuEntity.getDxmName());
            resource.setResourceType(dxMenuEntity.getDxmType());
            resource.setIsSys(dxMenuEntity.getIsSys());
            resource.setUpdateTime(new Date());
            resource.setUpdateBy(SecurityUtils.getUserId());
            resourceService.updateById(resource);
        }


        return true;
    }

    @Override
    @Transactional
    public Boolean addTopMenu(DxMenuAddParam addParam) {
        if (addParam == null) {
            throw new RuntimeException("参数错误");
        }

        long folderId = snowflake.nextId();
        if (addParam.getDxmId() != null){
            folderId = addParam.getDxmId();
        }

        Long userId = SecurityUtils.getUserId();
        DxMenuEntity dxMenuEntity = BeanUtil.copyProperties(addParam, DxMenuEntity.class);
        dxMenuEntity.setDxmId(folderId);
        dxMenuEntity.setCreateBy(userId);
        dxMenuEntity.setCreateTime(new Date());
        this.save(dxMenuEntity);

        FolderEntity folder = new FolderEntity();
        folder.setFolderId(folderId);
        folder.setFolderFlag(dxMenuEntity.getDxmType());
        folder.setFolderLevel("[0," + folderId + "]");
        folder.setFolderName(dxMenuEntity.getDxmName());
        folder.setFolderParentId(0L);
        folder.setDeleteFlag(Integer.parseInt(DxConstant.STATUS_IS_NO));
        folder.setCreateTime(new Date());
        folder.setCreateBy(userId);
        folderService.save(folder);

        ResourceEntity resource = new ResourceEntity();
        resource.setFolderParentId(0L);
        resource.setFolderId(folderId);
        resource.setResourceId(snowflake.nextId());
        resource.setResourceName(dxMenuEntity.getDxmName());
        resource.setResourceType(dxMenuEntity.getDxmType());
        resource.setIsSys(dxMenuEntity.getIsSys());
        resource.setDeleteFlag(Integer.parseInt(DxConstant.STATUS_IS_NO));
        resource.setCreateTime(new Date());
        resource.setCreateBy(userId);
        resourceService.save(resource);

        addMyFolder(folderId);
        return true;

    }

    public void addMyFolder(Long folderId){
        MyFolderEntity myFolderEntity = new MyFolderEntity();
        myFolderEntity.setId(SnowflakeIdUtil.nextId());
        myFolderEntity.setFolderId(folderId);
        myFolderEntity.setUserId(SecurityUtils.getUserId() + "");
        myFolderEntity.setDeleteFlag(Integer.parseInt(DxConstant.STATUS_IS_NO));
        myFolderEntity.setCreateTime(new Date());
        myFolderEntity.setCreateBy(SecurityUtils.getUserId());
        myFolderService.save(myFolderEntity);
    }
}
