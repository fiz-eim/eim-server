package com.soflyit.system.api;

import com.soflyit.common.core.constant.ServiceNameConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.domain.SysFile;
import com.soflyit.system.api.factory.RemoteFileFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务
 *
 * @author soflyit
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.FILE_SERVICE, fallbackFactory = RemoteFileFallbackFactory.class)
public interface RemoteFileService {


    @GetMapping(value = "/getFileRootUrl")
    R<String> getFileRootUrl();


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<SysFile> upload(@RequestPart(value = "file") MultipartFile file);


    @PostMapping(value = "/uploadWithRelativePath", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<SysFile> uploadWithRelativePath(@RequestPart(value = "file") MultipartFile file);


    @PostMapping(value = "/uploadFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<List<SysFile>> uploadFiles(@RequestPart(value = "bucketName") String bucketName,
                                 @RequestPart(value = "file") MultipartFile[] file);


    @DeleteMapping(value = "/deleteFile")
    R<String> deleteFile(@RequestParam("path") String path);


    @PostMapping(value = "/removeFile")
    R<String> removeFile(@RequestParam(value = "bucketName") String bucketName, @RequestParam(value = "path") String path);
}
