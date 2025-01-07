package com.soflyit.chattask.dx.modular.operate.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.soflyit.chattask.dx.common.enums.AppLevelEnum;
import com.soflyit.chattask.dx.common.remoteApi.RemoteUserUtil;
import com.soflyit.chattask.dx.modular.folder.domain.vo.TreeVO;
import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import com.soflyit.chattask.dx.modular.folder.organization.service.FolderService;
import com.soflyit.chattask.dx.modular.operate.domain.entity.OperateRecordEntity;
import com.soflyit.chattask.dx.modular.operate.domain.enums.OpTypeEnum;
import com.soflyit.chattask.dx.modular.operate.domain.vo.BaseOpDataVO;
import com.soflyit.chattask.dx.modular.operate.domain.vo.CommOpDataVO;
import com.soflyit.chattask.dx.modular.operate.domain.vo.OpTypeVO;
import com.soflyit.chattask.dx.modular.operate.mapper.OperateRecordMapper;
import com.soflyit.chattask.dx.modular.operate.service.OperateRecordService;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceDetailParam;
import com.soflyit.chattask.dx.modular.resource.resource.service.ResourceService;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 文档库的变更记录Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-06 14:53:26
 */
@Service
@Slf4j
public class OperateRecordServiceImpl extends ServiceImpl<OperateRecordMapper, OperateRecordEntity> implements OperateRecordService {
    @Resource
    private OperateRecordMapper operateRecordMapper;
    @Resource
    private FolderService folderService;
    @Resource
    private RemoteUserUtil remoteUserUtil;
    @Resource
    private ResourceService resourceService;

    final Snowflake snowflake = IdUtil.getSnowflake(1, 1);



    public void recordOperateLog(ResourceEntity selfResource, OpTypeEnum opTypeEnum, Long folderParentId, String additionText) {

        CommOpDataVO commOpDataVO = new CommOpDataVO();
        commOpDataVO.setOpTypeVO(OpTypeVO.instance(opTypeEnum));
        commOpDataVO.setAdditionText(additionText);
        saveByData(commOpDataVO, selfResource, folderParentId);
    }


    @Override
    public List<Long> getLastUpdateByUser(Integer pageSize) {
        List<Long> list = operateRecordMapper.getLastUpdateByUser(pageSize, SecurityUtils.getUserId());
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public void saveByData(BaseOpDataVO baseOpDataVO, ResourceEntity resource, Long folderParentId) {
        baseOpDataVO.setUserName(SecurityUtils.getUsername());
        baseOpDataVO.setNickName(SecurityUtils.getUsername());
        baseOpDataVO.setUserId(SecurityUtils.getUserId());
        SysUser byUserId = remoteUserUtil.getByUserId(baseOpDataVO.getUserId());
        if (byUserId != null) {
            baseOpDataVO.setNickName(byUserId.getNickName());
            baseOpDataVO.setUserName(byUserId.getUserName());
        }

        if (baseOpDataVO.getPathList() == null) {
            FolderEntity parent = folderService.getById(folderParentId);
            List<FolderEntity> folders = folderService.listByIds(JSON.parseArray(parent.getFolderLevel(), Long.class));

            baseOpDataVO.setPathList(BeanUtils.convertList(folders, TreeVO.class));

        }

        OperateRecordEntity entity = new OperateRecordEntity();
        entity.setOperateId(snowflake.nextId());
        entity.setOperateBody(baseOpDataVO.getOpTypeVO().getOpType().split("_")[1]);
        entity.setOperateData(JSON.toJSONString(baseOpDataVO));
        entity.setOperateType(baseOpDataVO.getOpTypeVO().getOpType());
        entity.setFolderId(resource != null ? resource.getFolderParentId() : null);
        entity.setResourceId(resource != null ? resource.getResourceId() : null);
        entity.setOperateTarget(resource != null ?resource.getResourceName():null);

        this.save(entity);

    }


    @Override
    public List<OperateRecordEntity> getListByResource(ResourceEntity resource, ResourceDetailParam param) {
        List<OperateRecordEntity> result = new ArrayList<>();


        List<Long> resourceIds = new ArrayList<>();
        if (AppLevelEnum.FOLDER.getLevel().equals(resource.getResourceType()) && resource.getFolderId() != null) {

            resourceIds = resourceService.getChildAndSelf(resource.getFolderId()).stream().map(ResourceEntity::getResourceId).collect(Collectors.toList());
        } else {

            resourceIds.add(resource.getResourceId());
        }
        LambdaQueryWrapper<OperateRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OperateRecordEntity::getResourceId, resourceIds);
        wrapper.orderByDesc(OperateRecordEntity::getCreateTime);
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return operateRecordMapper.selectList(wrapper);
    }

}
