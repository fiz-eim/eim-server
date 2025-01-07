package com.soflyit.system.api;

import com.soflyit.common.core.constant.ServiceNameConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.domain.SysFile;
import com.soflyit.system.api.factory.RemoteFileFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务
 *
 * @author soflyit
 */
@FeignClient(contextId = "remoteOssService", value = ServiceNameConstants.FILE_SERVICE, path = "/oss", fallbackFactory = RemoteFileFallbackFactory.class)
public interface RemoteOssService {


    @PostMapping(value = "/uploadObject", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<String> uploadObject(
            @RequestParam("bucketName") String bucketName,
            @RequestPart(value = "file") MultipartFile file,
            @RequestParam("genRandomKey") Boolean genRandomKey
    );


    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<String> uploadFile(
            @RequestParam(required = false, value = "bucketName") String bucketName,
            @RequestPart(value = "file") MultipartFile file,
            @RequestParam(required = false, value = "genRandomKey") Boolean genRandomKey
    );


    @GetMapping(value = "/presignedGetObject")
    R<String> presignedGetObject(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("objectName") String objectName
    );


    @GetMapping(value = "/presignedPutObject")
    R<SysFile> presignedPutObject(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("objectName") String objectName,
            @RequestParam("genRandomKey") Boolean genRandomKey
    );

    @DeleteMapping(value = "/removeObject")
    R<Boolean> removeObject(@RequestParam("bucketName") String bucketName, @RequestParam("objectName") String objectName);

}
