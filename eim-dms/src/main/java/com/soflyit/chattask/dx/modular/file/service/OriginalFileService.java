package com.soflyit.chattask.dx.modular.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.dx.modular.file.domain.entity.OriginalFileEntity;

import java.io.File;


/**
 * 原始资源文件Service接口
 *
 * @author jiaozhishang
 * @date 2023-11-10 14:43:36
 */
public interface OriginalFileService extends IService<OriginalFileEntity> {
    OriginalFileEntity convert(File file);

}
