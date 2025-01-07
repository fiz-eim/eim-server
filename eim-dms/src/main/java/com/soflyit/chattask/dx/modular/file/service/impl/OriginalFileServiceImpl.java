package com.soflyit.chattask.dx.modular.file.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.dx.modular.file.domain.entity.OriginalFileEntity;
import com.soflyit.chattask.dx.modular.file.mapper.OriginalFileMapper;
import com.soflyit.chattask.dx.modular.file.service.OriginalFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;


/**
 * 原始资源文件Service业务层处理
 *
 * @author jiaozhishang
 * @date 2023-11-10 14:43:36
 */
@Service
@Slf4j
public class OriginalFileServiceImpl extends ServiceImpl<OriginalFileMapper, OriginalFileEntity> implements OriginalFileService {

    public OriginalFileEntity convert(File file) {
        OriginalFileEntity ret = new OriginalFileEntity();
        ret.setOriginalSize(FileUtil.size(file));
        log.info("获取文件的MimeType {}", file.getAbsolutePath());
        ret.setMimeType(FileUtil.getMimeType(file.getPath()));
        ret.setFileType(FileUtil.getType(file));
        ret.setFileExt(FileUtil.extName(file));
        return ret;
    }

}
