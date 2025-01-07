package com.soflyit.chattask.dx.modular.folder.organization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.dx.modular.folder.organization.domain.entity.FolderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文档库存储目录Mapper接口
 *
 * @author soflyit
 * @date 2023-11-08 14:54:35
 */
@Mapper
@Repository
public interface FolderMapper extends BaseMapper<FolderEntity> {



    List<FolderEntity> getFoldersByParentFolder(Long folderId);
}
