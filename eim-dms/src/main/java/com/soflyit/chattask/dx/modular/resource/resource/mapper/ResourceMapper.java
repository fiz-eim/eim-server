package com.soflyit.chattask.dx.modular.resource.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统资源文档库Mapper接口
 *
 * @author soflyit
 * @date 2023-11-07 16:56:57
 */
@Repository
public interface ResourceMapper extends BaseMapper<ResourceEntity> {

    List<ResourceEntity> getParentAndSelf(Long folderId);


    List<ResourceEntity> getResourceByDelete(@Param("resources") List<Long> resources, @Param("deleteFlag") Integer deleteFlag);


    List<ResourceEntity> getChildAndSelf(Long folderId);


    void cancelDelete(Long resourceId);
}
