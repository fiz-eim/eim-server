package com.soflyit.chattask.dx.modular.operate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.dx.modular.operate.domain.entity.OperateRecordEntity;
import com.soflyit.chattask.dx.modular.operate.domain.enums.OpTypeEnum;
import com.soflyit.chattask.dx.modular.operate.domain.vo.BaseOpDataVO;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceDetailParam;

import java.util.List;


/**
 * 文档库的变更记录Service接口
 *
 * @author soflyit
 * @date 2023-11-06 14:53:26
 */
public interface OperateRecordService extends IService<OperateRecordEntity> {


    void saveByData(BaseOpDataVO opBodyVO, ResourceEntity resource,Long folderParentId);



    List<Long> getLastUpdateByUser(Integer pageSize);

    @Deprecated
    void recordOperateLog(ResourceEntity selfResource, OpTypeEnum opTypeEnum, Long folderParentId, String additionText);


    List<OperateRecordEntity> getListByResource(ResourceEntity resource, ResourceDetailParam param);
}
