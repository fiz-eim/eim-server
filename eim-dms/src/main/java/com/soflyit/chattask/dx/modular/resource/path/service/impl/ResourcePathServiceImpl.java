package com.soflyit.chattask.dx.modular.resource.path.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.dx.modular.resource.path.domain.entity.ResourcePathEntity;
import com.soflyit.chattask.dx.modular.resource.path.domain.param.ResourcePathQueryParam;
import com.soflyit.chattask.dx.modular.resource.path.domain.vo.ResourcePathVO;
import com.soflyit.chattask.dx.modular.resource.path.mapper.ResourcePathMapper;
import com.soflyit.chattask.dx.modular.resource.path.service.ResourcePathService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 文档目录安全映射Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-07 16:56:53
 */
@Service
public class ResourcePathServiceImpl extends ServiceImpl<ResourcePathMapper, ResourcePathEntity> implements ResourcePathService {

    @Override
    public List<ResourcePathVO> list(ResourcePathQueryParam queryParam) {

        return null;
    }
}
