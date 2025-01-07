package com.soflyit.system.controller;

import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.ServletUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.system.service.impl.AvatarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 头像接口<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-02 09:38
 */
@Slf4j
@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private AvatarService avatarService;


    @PostMapping(value = {"/user/{userId}/upload", "/{userId}/upload"})
    public AjaxResult uploadAvatar(@PathVariable("userId") Long userId, MultipartFile avatarFile) {
        if (avatarFile == null || avatarFile.isEmpty()) {
            return AjaxResult.error("头像文件不能为空");
        }
        avatarService.uploadUserAvatar(userId, avatarFile);
        return AjaxResult.success();
    }


    @PostMapping(value = "/icon/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult uploadIcon(@RequestPart("file") MultipartFile avatarFile) {
        if (avatarFile == null || avatarFile.isEmpty()) {
            return AjaxResult.error("图标文件不能为空");
        }
        String path = avatarService.uploadIcon(avatarFile);
        return AjaxResult.success("上传成功", path);
    }

    @GetMapping(value = {"/user/{userId}", "/{userId}"})
    public AjaxResult<String> getAvatarUrl(@PathVariable("userId") Long userId) {
        String avatarUrl = avatarService.getUserAvatarUrl(userId);
        if (StringUtils.isNotEmpty(avatarUrl)) {
            return AjaxResult.success("获取头像链接成功", avatarUrl);
        } else {
            return AjaxResult.error("获取头像链接失败");
        }
    }


    @GetMapping(value = {"/user/{userId}/image", "/{userId}/image"})
    public void getAvatarImage(@PathVariable("userId") Long userId) {
        String avatarUrl = avatarService.getUserAvatarUrl(userId);
        HttpServletResponse response = ServletUtils.getResponse();
        try {
            response.sendRedirect(avatarUrl);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
    }

    @GetMapping("/icon/fullPath")
    public AjaxResult<String> getAvatarFullUrl(@RequestParam("path") String path) {
        String avatarUrl = avatarService.getIconUrl(path);
        if (StringUtils.isNotEmpty(avatarUrl)) {
            return AjaxResult.success("获取图标链接成功", avatarUrl);
        } else {
            return AjaxResult.error("获取图标链接失败");
        }
    }

    @GetMapping("/icon/fullPaths")
    public AjaxResult<Map<String, String>> getAvatarFullUrls(@RequestParam("paths") List<String> paths) {
        Map<String, String> avatarUrlMap = avatarService.getIconUrls(paths);
        if (MapUtils.isNotEmpty(avatarUrlMap)) {
            return AjaxResult.success("获取图标链接成功", avatarUrlMap);
        } else {
            return AjaxResult.error("获取图标链接失败");
        }
    }

    @GetMapping(value = {"/user/avatars", "/users"})
    public AjaxResult<Map<Long, String>> getAvatarUrl(@RequestParam List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return AjaxResult.success(new HashMap<>());
        }
        return avatarService.getUserAvatarUrl(userIds);
    }

    @Autowired
    public void setAvatarService(AvatarService avatarService) {
        this.avatarService = avatarService;
    }
}
