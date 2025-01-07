package com.soflyit.chattask.dx.modular.folder.my.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.dx.modular.folder.my.domain.entity.MyFolderEntity;
import com.soflyit.chattask.dx.modular.folder.my.mapper.MyFolderMapper;
import com.soflyit.chattask.dx.modular.folder.my.service.MyFolderService;
import com.soflyit.chattask.dx.modular.folder.organization.service.FolderService;
import com.soflyit.chattask.dx.modular.operate.domain.vo.CommOpDataVO;
import com.soflyit.chattask.dx.modular.operate.domain.vo.OpTypeVO;
import com.soflyit.chattask.dx.modular.operate.service.OperateRecordService;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.soflyit.chattask.dx.modular.operate.domain.enums.OpTypeEnum.FILE_REMOVE;
import static com.soflyit.chattask.dx.modular.operate.domain.enums.OpTypeEnum.FOLDER_REMOVE;

/**
 * 我的文档Service业务层处理
 *
 * @author jiaozhishang
 * @date 2023-11-10 14:06:13
 */
@Service
public class MyFolderServiceImpl extends ServiceImpl<MyFolderMapper, MyFolderEntity> implements MyFolderService {

    @Resource
    ResourceService resourceService;
    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    FolderService folderService;

    @Transactional
    @Override
    public boolean removeByIds(List<String> ids, Long userId, Boolean skipAuth) {
        if (CollUtil.isEmpty(ids)){
            return true;
        }

        String resourceIdCheck = ids.get(0);
        ResourceEntity resourceByChek = resourceService.getById(resourceIdCheck);



        for (String resourceId : ids) {

            ResourceEntity resource = resourceService.getById(resourceId);


            resourceService.removeById(resource);

            CommOpDataVO commOpDataVO = new CommOpDataVO();
            if (resource.getFolderId() != null){
                folderService.removeById(resource.getFolderId());
                commOpDataVO.setOpTypeVO(OpTypeVO.instance(FOLDER_REMOVE));
            }else {
                commOpDataVO.setOpTypeVO(OpTypeVO.instance(FILE_REMOVE));
            }
            operateRecordService.saveByData(commOpDataVO, resource, resource.getFolderParentId());
        }



        return true;
    }

    @Override
    public List<Long> folderIds(Long userId) {
        LambdaQueryWrapper<MyFolderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyFolderEntity::getUserId, userId);
        List<MyFolderEntity> myFolders = this.baseMapper.selectList(queryWrapper);
        return myFolders.stream().map(MyFolderEntity::getFolderId).collect(Collectors.toList());
    }
}
