package com.soflyit.chattask.dx.modular.storage.impl;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import com.soflyit.chattask.dx.common.utils.FileUtils;
import com.soflyit.chattask.dx.modular.file.domain.entity.OriginalFileEntity;
import com.soflyit.chattask.dx.modular.storage.StorageService;
import com.soflyit.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 文件存储抽象服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-22 15:17
 */
@Slf4j
public abstract class AbstractStorageService implements StorageService {


    @Override
    public OriginalFileEntity multipartFileToEntry(MultipartFile file) {
        OriginalFileEntity entity = new OriginalFileEntity();
        entity.setOriginalSize(file.getSize());
        entity.setMimeType(file.getContentType());
        try {
            String originalFilename = file.getOriginalFilename();
            String fileType = FileTypeUtil.getType(file.getInputStream(), originalFilename);
            if (fileType == null){
                fileType = FileUtils.getFileType(originalFilename);
            }
            entity.setFileType(fileType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        entity.setFileExt(FileUtil.extName(file.getOriginalFilename()));
        entity.setOriginalName(file.getOriginalFilename());
        return entity;
    }


    public String generateStoragePath(String fileName) {
        return generateDateBaseStoragePath(fileName);
    }


    protected String generateDateBaseStoragePath(String fileName) {
        String path = DateFormatUtils.format(new Date(), "yyyy/MM".replace("/", File.separator));
        if (StringUtils.isNotEmpty(fileName)) {
            path = path + File.separator + fileName;
        }
        return path;
    }


}
