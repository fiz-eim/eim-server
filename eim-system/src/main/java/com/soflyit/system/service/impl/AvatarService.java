package com.soflyit.system.service.impl;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.minio.service.MinioService;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.config.AvatarConfig;
import com.soflyit.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 头像服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-02 09:50
 */
@Service
@Slf4j
public class AvatarService {

    private MinioService minioService;

    private SysUserMapper sysUserMapper;

    private AvatarConfig avatarConfig;

    private final List<String> imageTypes = new ArrayList<>();


    public AjaxResult<SysUser> uploadUserAvatar(Long userId, MultipartFile file) {
        if (minioService == null) {
            return AjaxResult.error("头像存储服务未配置");
        }

        LambdaQueryWrapper<SysUser> userQueryWrapper = new LambdaQueryWrapper<>(SysUser.class);
        userQueryWrapper.eq(SysUser::getUserId, userId);
        Long count = sysUserMapper.selectCount(userQueryWrapper);
        if (count == null || count < 1) {
            return AjaxResult.error("用户不存在");
        }

        try {
            String fileType = FileTypeUtil.getType(file.getInputStream(), file.getOriginalFilename());
            if (isNotImage(fileType)) {
                return AjaxResult.error("文件格式不正确, 请上传png、jpg、jpeg、bpm、gif格式的图片");
            }

            String fileName = UUID.fastUUID().toString(Boolean.TRUE);
            String suffix = FileNameUtil.getSuffix(file.getOriginalFilename());
            if (StringUtils.isNotEmpty(suffix)) {
                fileName += ("." + suffix);
            }
            String path = getDateBasedDir(fileName);
            path = path.replace(File.separator, "/");
            minioService.saveObject(avatarConfig.getStorageBucket(), path, file.getContentType(), file.getInputStream());

            SysUser userData = new SysUser();
            userData.setAvatar(avatarConfig.getStorageBucket() + "/" + path);
            userData.setUserId(userId);
            sysUserMapper.updateUser(userData);
            return AjaxResult.success(userData);
        } catch (Exception e) {
            return AjaxResult.error("上传头像失败");
        }
    }


    public String getUserAvatarUrl(Long userId) {
        LambdaQueryWrapper<SysUser> userQueryWrapper = new LambdaQueryWrapper<>(SysUser.class);
        userQueryWrapper.select(SysUser::getAvatar, SysUser::getNickName, SysUser::getUserId);
        userQueryWrapper.eq(SysUser::getUserId, userId);
        SysUser sysUser = sysUserMapper.selectOne(userQueryWrapper);
        String avatar = getUserAvatar(sysUser);
        return avatar;
    }


    public AjaxResult<Map<Long, String>> getUserAvatarUrl(List<Long> userIds) {

        LambdaQueryWrapper<SysUser> userQueryWrapper = new LambdaQueryWrapper<>(SysUser.class);
        userQueryWrapper.select(SysUser::getAvatar, SysUser::getNickName, SysUser::getUserId);
        userQueryWrapper.in(SysUser::getUserId, userIds);
        List<SysUser> sysUsers = sysUserMapper.selectList(userQueryWrapper);
        Map<Long, String> result;
        if (CollectionUtils.isNotEmpty(sysUsers)) {
            result = new HashMap<>(sysUsers.size() * 2);
            sysUsers.forEach(sysUser -> result.put(sysUser.getUserId(), getUserAvatar(sysUser)));
        } else {
            result = new HashMap<>();
        }
        return AjaxResult.success(result);
    }


    public String getIconUrl(String path) {
        StringBuffer avatar = new StringBuffer(200);
        if (StringUtils.isNotEmpty(path)) {
            avatar.append(avatarConfig.getAccessUrl()).append(path);
        } else {
            return null;
        }
        return avatar.toString();

    }

    public Map<String, String> getIconUrls(List<String> paths) {
        if (CollectionUtils.isEmpty(paths)) {
            return null;
        }
        Set<String> pathSet = new HashSet<>(paths);
        Map<String, String> result = new HashMap<>(pathSet.size() * 2);
        pathSet.forEach(path -> {
            String fullPath = getIconUrl(path);
            if (StringUtils.isNotEmpty(fullPath)) {
                result.put(path, fullPath);
            }
        });
        return result;
    }


    public String uploadIcon(MultipartFile file) {
        try {
            String fileType = FileTypeUtil.getType(file.getInputStream(), file.getOriginalFilename());
            if (isNotImage(fileType)) {
                throw new BaseException("文件格式不正确, 请上传png、jpg、jpeg、bpm、gif格式的图片");
            }

            String fileName = UUID.fastUUID().toString(Boolean.TRUE);
            String suffix = FileNameUtil.getSuffix(file.getOriginalFilename());
            if (StringUtils.isNotEmpty(suffix)) {
                fileName += ("." + suffix);
            }
            String path = getDateBasedDir(fileName);
            path = path.replace(File.separator, "/");
            minioService.saveObject(avatarConfig.getStorageBucket(), path, file.getContentType(), file.getInputStream());

            return avatarConfig.getStorageBucket() + "/" + path;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("图标上传失败: " + e.getMessage());
        }
    }

    protected String getUserAvatar(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        StringBuffer avatar = new StringBuffer(200);
        String avatarUrl = null;
        if (sysUser != null) {
            avatarUrl = sysUser.getAvatar();
        }
        if (StringUtils.isNotEmpty(avatarUrl)) {
            avatar.append(avatarConfig.getAccessUrl()).append(avatarUrl);
        } else {
            avatar.append(avatarConfig.getAvatarServer());
            avatar.append(sysUser.getUserId()).append(".svg?size=120");

            String avatarText = getAvatarText(sysUser.getNickName());
            if (com.soflyit.common.core.utils.StringUtils.isNotEmpty(avatarText)) {
                avatar.append("&text=").append(URLUtil.encode(avatarText, StandardCharsets.UTF_8));
            }
            avatar.append("&id=").append(sysUser.getUserId()).append("&version=1.2");

        }
        return avatar.toString();
    }

    private String getAvatarText(String name) {
        if (com.soflyit.common.core.utils.StringUtils.isNotEmpty(name)) {
            if (name.length() > 2) {
                return name.substring(name.length() - 2, name.length());
            } else {
                return name;
            }
        }
        return null;
    }

    private boolean isNotImage(String fileType) {
        return !imageTypes.contains(fileType);

    }

    private String getDateBasedDir(String fileName) {
        String path = DateFormatUtils.format(new Date(), "yyyy/MM".replace("/", File.separator));
        if (StringUtils.isNotEmpty(fileName)) {
            path = path + File.separator + fileName;
        }
        return path;
    }


    @Autowired(required = false)
    public void setMinioService(MinioService minioService) {
        this.minioService = minioService;
    }

    @Autowired
    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Autowired
    public void setAvatarConfig(AvatarConfig avatarConfig) {
        this.avatarConfig = avatarConfig;
    }

    @PostConstruct
    private void init() {
        imageTypes.add("jpg");
        imageTypes.add("jpeg");
        imageTypes.add("png");
        imageTypes.add("bmp");
        imageTypes.add("gif");
    }

}
