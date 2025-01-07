package com.soflyit.chattask.dx.modular.storage.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.soflyit.chattask.dx.config.StorageConfig;
import com.soflyit.chattask.dx.modular.file.domain.entity.OriginalFileEntity;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.minio.service.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * minIo存储服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-21 16:29
 */
@Slf4j
public class MinIoStorageService extends AbstractStorageService {

    private MinioService minioService;

    private StorageConfig storageConfig;

    @Override
    public String saveFile(String fileName, MultipartFile file) {
        String realName = fileName;
        if (StringUtils.isNotEmpty(fileName)) {
            String originalFileName = file.getOriginalFilename();
            realName = SnowflakeIdUtil.nextId() + FileNameUtil.getSuffix(originalFileName);
        }
        String path = generateStoragePath(realName);
        path = path.replace(File.separator, "/");

        try (InputStream inputStream = file.getInputStream()) {
            minioService.saveObject(storageConfig.getMinioBucket(), path, file.getContentType(), inputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
        return path;
    }


    @Override
    public InputStream getInputStream(OriginalFileEntity fileEntity) {
        String bucketName = storageConfig.getMinioBucket();
        String filePath = fileEntity.getOriginalPath();
        InputStream inputStream = minioService.getObject(bucketName, filePath);
        try {
            log.debug("获取文件流：{}", inputStream.available());
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        return inputStream;
    }

    @Autowired(required = false)
    public void setMinioService(MinioService minioService) {
        this.minioService = minioService;
    }

    @Autowired
    public void setStorageConfig(StorageConfig storageConfig) {
        this.storageConfig = storageConfig;
    }

}
