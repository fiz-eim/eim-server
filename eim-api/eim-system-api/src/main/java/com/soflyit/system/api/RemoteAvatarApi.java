package com.soflyit.system.api;

import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.constant.ServiceNameConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.factory.RemoteAvatarFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 用户服务
 *
 * @author soflyit
 */
@FeignClient(contextId = "remoteAvatarApi", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteAvatarFallbackFactory.class)
public interface RemoteAvatarApi {


    @GetMapping("/avatar/user/{userId}")
    R<String> getUserAvatarByUserId(@PathVariable("userId") String userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @GetMapping("/avatar/icon/fullPaths")
    R<Map<String, String>> getAvatarFullPaths(@RequestParam("paths") List<String> paths, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @GetMapping("/avatar/user/avatars")
    R<Map<Long, String>> getUserAvatarByUserIds(@RequestParam("userIds") List<Long> userIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @PostMapping(value = "/avatar/icon/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<String> uploadIcon(@RequestPart("file") MultipartFile file);


}
