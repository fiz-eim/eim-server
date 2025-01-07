package com.soflyit.chattask.dx.modular.folder.my.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.dx.modular.folder.my.domain.entity.MyFolderEntity;

import java.util.List;

/**
 * 我的文档Service接口
 *
 * @author jiaozhishang
 * @date 2023-11-10 14:06:13
 */
public interface MyFolderService extends IService<MyFolderEntity> {


    boolean removeByIds(List<String> ids, Long userId, Boolean skipAuth);

    List<Long> folderIds(Long userId);
}
