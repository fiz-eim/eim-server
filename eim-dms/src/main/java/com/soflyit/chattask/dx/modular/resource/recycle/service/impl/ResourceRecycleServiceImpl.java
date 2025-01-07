package com.soflyit.chattask.dx.modular.resource.recycle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.dx.common.enums.DxConstant;
import com.soflyit.chattask.dx.modular.operate.domain.vo.CommOpDataVO;
import com.soflyit.chattask.dx.modular.operate.domain.vo.OpTypeVO;
import com.soflyit.chattask.dx.modular.operate.service.OperateRecordService;
import com.soflyit.chattask.dx.modular.resource.recycle.entity.ResourceRecycleEntity;
import com.soflyit.chattask.dx.modular.resource.recycle.mapper.ResourceRecycleMapper;
import com.soflyit.chattask.dx.modular.resource.recycle.service.ResourceRecycleService;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.service.ResourceService;
import com.soflyit.common.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.soflyit.chattask.dx.modular.operate.domain.enums.OpTypeEnum.FILE_RECYCLE;
import static com.soflyit.chattask.dx.modular.operate.domain.enums.OpTypeEnum.FOLDER_RECYCLE;

/**
 * (ResourceRecycle)表服务实现类
 *
 * @author JiangNing.G
 * @since 2023-11-18 11:48:09
 */
@Service
public class ResourceRecycleServiceImpl extends ServiceImpl<ResourceRecycleMapper, ResourceRecycleEntity> implements ResourceRecycleService {

    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    private ResourceService resourceService;

    @Override
    public Boolean removeForever(Long resourceId) {
        ResourceRecycleEntity byResourceId = getByResourceId(resourceId);
        if (byResourceId == null){
            throw new RuntimeException("资源不存在");
        }
        resourceService.removeById(resourceId);
        this.removeById(byResourceId);
        return true;
    }

    @Override
    public List<Long> getResourceIds(Long userId) {
        if (userId == null){
            userId = SecurityUtils.getUserId();
        }
        LambdaQueryWrapper<ResourceRecycleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ResourceRecycleEntity::getCreateBy, userId);
        wrapper.orderByDesc(ResourceRecycleEntity::getCreateTime);
        return list(wrapper).stream().map(ResourceRecycleEntity::getResourceId).collect(Collectors.toList());
    }

    @Override
    public Boolean resourceRestore(Long resourceId) {
        ResourceRecycleEntity byResourceId = getByResourceId(resourceId);
        if (byResourceId == null){
            throw new RuntimeException("资源不存在");
        }
        ResourceEntity resourceEntity = resourceService.getById(resourceId);
        checkParentFolder(byResourceId.getFolderParentId(),resourceEntity.getResourceName());
        resourceEntity.setFolderParentId(byResourceId.getFolderParentId());
        this.removeById(byResourceId);
        resourceService.updateById(resourceEntity);

        CommOpDataVO commOpDataVO = new CommOpDataVO();
        if (resourceEntity.getFolderId() != null){
            commOpDataVO.setOpTypeVO(OpTypeVO.instance(FOLDER_RECYCLE));
        }else {
            commOpDataVO.setOpTypeVO(OpTypeVO.instance(FILE_RECYCLE));
        }
        operateRecordService.saveByData(commOpDataVO, resourceEntity, resourceEntity.getFolderParentId());
        return true;
    }



    public void checkParentFolder(Long oldFolderParentId, String resourceName){

        ResourceEntity byFolderId = resourceService.getByFolderId(oldFolderParentId);
        if (byFolderId == null || Objects.equals(DxConstant.RECYCLE_FOLDER_ID, byFolderId.getFolderParentId())){
            throw new RuntimeException("原始路径不存在,无法还原!");
        }

        boolean duplicateName = resourceService.isDuplicateName(oldFolderParentId, resourceName);
        if (duplicateName){
            throw new RuntimeException("原路径下有同名数据,请处理!");
        }
    }



    public ResourceRecycleEntity getByResourceId(Long resourceId){
        LambdaQueryWrapper<ResourceRecycleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ResourceRecycleEntity::getResourceId, resourceId);
        return this.getOne(wrapper);
    }
}
