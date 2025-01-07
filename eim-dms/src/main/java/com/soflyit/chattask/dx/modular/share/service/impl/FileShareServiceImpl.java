package com.soflyit.chattask.dx.modular.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.dx.modular.share.domain.entity.FileShareEntity;
import com.soflyit.chattask.dx.modular.share.domain.param.FileShareQueryParam;
import com.soflyit.chattask.dx.modular.share.domain.vo.FileShareVO;
import com.soflyit.chattask.dx.modular.share.mapper.FileShareMapper;
import com.soflyit.chattask.dx.modular.share.service.FileShareService;
import com.soflyit.common.core.utils.bean.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 文件分享Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-07 16:50:36
 */
@Service
public class FileShareServiceImpl extends ServiceImpl<FileShareMapper, FileShareEntity> implements FileShareService {

    private LambdaQueryWrapper<FileShareEntity> lambdaQueryWrapper(FileShareQueryParam queryParam) {

        LambdaQueryWrapper<FileShareEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(queryParam.getDeleteFlag())) {
            queryWrapper.eq(FileShareEntity::getDeleteFlag, queryParam.getDeleteFlag());
        }
        if (ObjectUtils.isNotEmpty(queryParam.getResourceId())) {
            queryWrapper.eq(FileShareEntity::getResourceId, queryParam.getResourceId());
        }
        if (ObjectUtils.isNotEmpty(queryParam.getRevision())) {
            queryWrapper.eq(FileShareEntity::getRevision, queryParam.getRevision());
        }
        if (ObjectUtils.isNotEmpty(queryParam.getShareId())) {
            queryWrapper.eq(FileShareEntity::getShareId, queryParam.getShareId());
        }
        if (ObjectUtils.isNotEmpty(queryParam.getSharePass())) {
            queryWrapper.eq(FileShareEntity::getSharePass, queryParam.getSharePass());
        }
        if (ObjectUtils.isNotEmpty(queryParam.getShareStatus())) {
            queryWrapper.eq(FileShareEntity::getShareStatus, queryParam.getShareStatus());
        }
        if (ObjectUtils.isNotEmpty(queryParam.getShareTitle())
                && ObjectUtils.isNotEmpty(queryParam.getShareTitle().getFieldValue())) {
            queryWrapper.like(FileShareEntity::getShareTitle, queryParam.getShareTitle().getFieldValue());
        }
        if (ObjectUtils.isNotEmpty(queryParam.getShareTitle())
                && ObjectUtils.isNotEmpty(queryParam.getShareTitle().getOrderType())) {
            if (queryParam.getShareTitle().getOrderType() == 0) {
                queryWrapper.orderByAsc(FileShareEntity::getShareTitle);
            } else if (queryParam.getShareTitle().getOrderType() == 1) {
                queryWrapper.orderByDesc(FileShareEntity::getShareTitle);
            }
        }
        return queryWrapper;
    }

    @Override
    public List<FileShareVO> list(FileShareQueryParam queryParam) {

        LambdaQueryWrapper<FileShareEntity> queryWrapper = lambdaQueryWrapper(queryParam);


        List<FileShareEntity> list = this.list(queryWrapper);


        return BeanUtils.convertList(list, FileShareVO.class);
    }
}
