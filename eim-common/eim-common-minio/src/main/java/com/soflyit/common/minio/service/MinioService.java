package com.soflyit.common.minio.service;

import cn.hutool.core.io.FileUtil;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.minio.config.MinioConfig;
import com.soflyit.common.minio.processbar.CommonProgressBar;
import com.soflyit.common.minio.processbar.builder.CommonProgressBarBuilder;
import com.soflyit.common.minio.processbar.io.ProcessInputStream;
import com.soflyit.common.minio.processbar.listener.ProgressListener;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static io.minio.ObjectWriteArgs.MIN_MULTIPART_SIZE;

/**
 * minio 服务<br>
 * 详细说明<br>
 * 提供 桶创建、保存对象、下载对象、获取临时对象链接、删除对象功能
 *
 * @author Toney
 * @date 2023-12-22 15:39
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class MinioService {

    private MinioClient minioClient;

    private MinioConfig minioConfig;

    public String saveObject(String bucketName, String objectName, String mineType, InputStream inputStream) {
        try {
            bucketName = StringUtils.isEmpty(bucketName) ? minioConfig.getBucketName() : bucketName;
            createBucket(bucketName);
            int objectSize = inputStream.available();
            int partSize = -1;
            if (objectSize < 1) {
                partSize = MIN_MULTIPART_SIZE;
            }
            PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(inputStream, objectSize, partSize).contentType(mineType).build();
            minioClient.putObject(putObjectArgs);
            return objectName;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件存储失败：" + e.getMessage());
        }
    }

    public String saveObject(String bucketName, String objectName, String mineType, InputStream inputStream, ProgressListener listener) {
        if (minioConfig.getProgress() == null || !minioConfig.getProgress()) {
            return saveObject(bucketName, objectName, mineType, inputStream);
        }

        try (ProgressBar progressBar = buildProgressBar(listener, Long.valueOf(inputStream.available()));
             ProcessInputStream progressInputStream = new ProcessInputStream(inputStream, progressBar)) {
            bucketName = StringUtils.isEmpty(bucketName) ? minioConfig.getBucketName() : bucketName;
            createBucket(bucketName);
            int objectSize = inputStream.available();
            int partSize = -1;
            if (objectSize < 1) {
                partSize = MIN_MULTIPART_SIZE;
            }
            PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(progressInputStream, objectSize, partSize).contentType(mineType).build();
            minioClient.putObject(putObjectArgs);
            return objectName;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件存储失败：" + e.getMessage());
        }
    }


    public String saveObject(String bucketName, String objectName, File file) {
        bucketName = StringUtils.isEmpty(bucketName) ? minioConfig.getBucketName() : bucketName;
        try (InputStream inputStream = new FileInputStream(file)) {
            String mineType = FileUtil.getMimeType(file.getPath());
            return saveObject(bucketName, objectName, mineType, inputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件存储失败：" + e.getMessage());
        }
    }


    public String saveObject(String bucketName, String objectName, File file, ProgressListener listener) {
        if (minioConfig.getProgress() == null || !minioConfig.getProgress()) {
            return saveObject(bucketName, objectName, file);
        }
        bucketName = StringUtils.isEmpty(bucketName) ? minioConfig.getBucketName() : bucketName;

        try (InputStream inputStream = new FileInputStream(file);
             ProgressBar progressBar = buildProgressBar(listener, Long.valueOf(inputStream.available()));
             ProcessInputStream progressInputStream = new ProcessInputStream(inputStream, progressBar)) {
            String mineType = FileUtil.getMimeType(file.getPath());
            return saveObject(bucketName, objectName, mineType, progressInputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件存储失败：" + e.getMessage());
        }
    }

    private ProgressBar buildProgressBar(ProgressListener listener, Long max) {
        CommonProgressBarBuilder builder = CommonProgressBar.builder();
        builder.setInitialMax(max);
        builder.setTaskName("uploading...");
        builder.setProgressListener(listener);
        builder.setUpdateIntervalMillis(minioConfig.getUpdateIntervalMillis());
        return builder.build();
    }


    public InputStream getObject(String bucketName, String objectName) {
        if (StringUtils.isEmpty(bucketName)) {
            throw new BaseException("获取对象失败：桶名称不能为空");
        }
        bucketName = StringUtils.isEmpty(bucketName) ? minioConfig.getBucketName() : bucketName;
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
        try {
            return minioClient.getObject(getObjectArgs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件存储失败：" + e.getMessage());
        }
    }


    public void deleteObject(String bucketName, String objectName) {
        if (StringUtils.isEmpty(bucketName)) {

            throw new BaseException("删除对象失败：桶名称不能为空");
        }
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build();
        try {
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new BaseException("文件删除失败：" + e.getMessage());
        }
    }


    public String getObjectTempURL(String bucketName, String objectName, int expiry, TimeUnit unit) {
        if (StringUtils.isEmpty(bucketName)) {

            throw new BaseException("获取访问链接失败：桶名称不能为空");
        }
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expiry, unit)
                            .build()
            );

            return replaceUrl(url);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("获取下载链接失败：" + e.getMessage());
        }
    }

    private void createBucket(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs
                            .builder()
                            .bucket(bucketName)
                            .build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private String replaceUrl(String url) {

        if (org.apache.commons.lang3.StringUtils.isNotBlank(minioConfig.getAccUrl())) {
            url = minioConfig.getAccUrl() + UriComponentsBuilder.fromHttpUrl(url).build().getPath();
        }
        return url;
    }

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Autowired
    public void setMinioConfig(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }
}
