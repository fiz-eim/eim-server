package com.soflyit.system.api.factory;

import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.RemoteFileService;
import com.soflyit.system.api.domain.SysFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务降级处理
 *
 * @author soflyit
 */
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteFileService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteFileFallbackFactory.class);

    @Override
    public RemoteFileService create(Throwable throwable) {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new RemoteFileService() {
            @Override
            public R<String> getFileRootUrl() {
                return R.fail("获得文件根路径失败:" + throwable.getMessage());
            }

            @Override
            public R<SysFile> upload(MultipartFile file) {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public R<SysFile> uploadWithRelativePath(MultipartFile file) {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysFile>> uploadFiles(String bucketName, MultipartFile[] file) {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }

            @Override
            public R<String> deleteFile(String path) {
                return R.fail("删除文件失败:" + throwable.getMessage());
            }

            @Override
            public R<String> removeFile(String bucketName, String path) {
                return R.fail("删除文件失败:" + throwable.getMessage());
            }
        };
    }
}
