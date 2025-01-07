package com.soflyit.chattask.im.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.soflyit.chattask.im.config.SoflyImFileConfig;
import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import com.soflyit.chattask.im.message.domain.vo.MsgFileExtDataVo;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.minio.service.MinioService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import static com.soflyit.chattask.im.common.constant.ImFileConstant.*;

/**
 * 类描述<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-12 13:50
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
@Slf4j
public class MinioStorageService {

    private MinioClient minioClient;

    private MinioService minioService;

    private SoflyImFileConfig imFileConfig;


    public String saveFile(File file, String fileName, MsgFile msgFile) {

        String bucketName = imFileConfig.getMinioBucket();
        String objectName = getDateBasedDir(msgFile.getChannelId()) + File.separator + generateFileName(msgFile);
        String extData = StringUtils.defaultString(msgFile.getExtData(), "{}");
        MsgFileExtDataVo extDataVo = JSON.parseObject(extData, MsgFileExtDataVo.class);
        extDataVo.setBucketName(bucketName);
        msgFile.setExtData(JSON.toJSONString(extDataVo));

        objectName = objectName.replace(File.separator, "/");

        try (FileInputStream fis = new FileInputStream(file)) {
            return minioService.saveObject(bucketName, objectName, msgFile.getMimeType(), fis);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件存储失败：" + e.getMessage());
        }
    }


    public String savePreviewFile(byte[] data, MsgFile msgFile, String fileName) {
        String objectName = getDateBasedDir(msgFile.getChannelId()) + File.separator + fileName;
        objectName = objectName.replace(File.separator, "/");
        String bucketName = imFileConfig.getMinioBucket();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            return minioService.saveObject(bucketName, objectName, msgFile.getMimeType(), bais);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件存储失败：" + e.getMessage());
        }
    }


    public void deleteMsgFile(MsgFile msgFile) {
        log.warn("暂不支持删除原始文件");
    }


    private String getDateBasedDir(Long channelId) {
        String path = DateFormatUtils.format(new Date(), "yyyy/MM".replace("/", File.separator));

        if (channelId != null) {
            path += (File.separator + channelId);
        }
        return path;
    }

    private String generateFileName(MsgFile msgFile) {
        String extName = msgFile.getExtension();

        Long fileId = msgFile.getId();
        if (fileId == null) {
            fileId = (Long) msgFile.getParams().get("fileId");
        }
        if (fileId == null) {
            fileId = SnowflakeIdUtil.nextId();
        }

        String fileName = fileId + "_" + SnowflakeIdUtil.nextId();
        if (StringUtils.isNotBlank(extName)) {
            fileName = fileName + "." + extName;
        }
        return fileName;
    }


    public InputStream getFileInputStream(MsgFile msgFile, Short fileType) {

        String extData = StringUtils.defaultString(msgFile.getExtData(), "{}");
        MsgFileExtDataVo extDataVo = JSON.parseObject(extData, MsgFileExtDataVo.class);
        String bucketName = imFileConfig.getMinioBucket();
        if (extDataVo != null && StringUtils.isNotEmpty(extDataVo.getBucketName())) {
            bucketName = extDataVo.getBucketName();
        }

        String objectName = null;
        if (FILE_TYPE_ORIGINAL.equals(fileType)) {
            objectName = msgFile.getPath();
        } else if (FILE_TYPE_PREVIEW.equals(fileType)) {
            objectName = msgFile.getPreviewPath();
        } else if (FILE_TYPE_THUMBNAIL.equals(fileType)) {
            objectName = msgFile.getThumbnailPath();
        }
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
        try {
            return minioClient.getObject(getObjectArgs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件存储失败：" + e.getMessage());
        }
    }

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Autowired
    public void setImFileConfig(SoflyImFileConfig imFileConfig) {
        this.imFileConfig = imFileConfig;
    }

    @Autowired
    public void setMinioService(MinioService minioService) {
        this.minioService = minioService;
    }
}
