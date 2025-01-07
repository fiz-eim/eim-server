package com.soflyit.chattask.dx.modular.resource.path.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.dx.modular.resource.path.domain.entity.ResourcePathEntity;
import com.soflyit.chattask.dx.modular.resource.path.domain.param.ResourcePathQueryParam;
import com.soflyit.chattask.dx.modular.resource.path.domain.vo.ResourcePathVO;

import java.util.List;


/**
 * 文档目录安全映射Service接口
 *
 * @author soflyit
 * @date 2023-11-07 16:56:53
 */
public interface ResourcePathService extends IService<ResourcePathEntity> {

    List<ResourcePathVO> list(ResourcePathQueryParam resourcePathParam);
}
