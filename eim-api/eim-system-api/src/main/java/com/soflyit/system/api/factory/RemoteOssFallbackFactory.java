package com.soflyit.system.api.factory;

import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.RemoteOssService;
import com.soflyit.system.api.domain.SysFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务降级处理
 *
 * @author soflyit
 */
@Component
public class RemoteOssFallbackFactory implements FallbackFactory<RemoteOssService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteOssFallbackFactory.class);

    @Override
    public RemoteOssService create(Throwable throwable) {
        log.error("oss文件服务调用失败:" + throwable.getMessage(), throwable);
        return new RemoteOssService() {
            @Override
            public R<String> uploadObject(String bucketName, MultipartFile file, Boolean genRandomKey) {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public R<String> uploadFile(String bucketName, MultipartFile file, Boolean genRandomKey) {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }

            @GetMapping("/presignedGetObject")
            @Override
            public R<String> presignedGetObject(String bucketName, String objectName) {
                return R.fail("获得PRESIGNED GET URL失败:" + throwable.getMessage());
            }

            @Override
            public R<SysFile> presignedPutObject(String bucketName, String objectName, Boolean genRandomKey) {
                return R.fail("获得PRESIGNED PUT URL失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> removeObject(String bucketName, String objectName) {
                return R.fail("删除对象信息失败:" + throwable.getMessage());
            }
        };
    }
}
