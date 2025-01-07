package com.soflyit.chattask.dx.modular.resource.recycle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.dx.modular.resource.recycle.entity.ResourceRecycleEntity;

import java.util.List;

/**
 * (ResourceRecycle)表服务接口
 *
 * @author JiangNing.G
 * @since 2023-11-18 11:48:09
 */
public interface ResourceRecycleService extends IService<ResourceRecycleEntity> {


    Boolean resourceRestore(Long resourceId);


    List<Long> getResourceIds(Long userId);


    Boolean removeForever(Long resourceId);

}
