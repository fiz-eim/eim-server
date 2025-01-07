package com.soflyit.chattask.dx.modular.storage.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.soflyit.chattask.dx.common.utils.FileUtils;
import com.soflyit.chattask.dx.modular.file.domain.entity.OriginalFileEntity;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 本地存储服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-21 16:10
 */
@Slf4j
public class LocalStorageService extends AbstractStorageService {

    @Override
    public String saveFile(String fileName, MultipartFile multipartFile) {

        String realName = fileName;
        if (StringUtils.isNotEmpty(fileName)) {
            String originalFileName = multipartFile.getOriginalFilename();
            realName = SnowflakeIdUtil.nextId() + FileNameUtil.getSuffix(originalFileName);
        }
        String path = generateStoragePath(realName);
        String fullPath = FileUtils.getFilePath() + File.separator + path;
        path = path.replace(File.separator, "/");

        try (InputStream inputStream = multipartFile.getInputStream()) {

            File file = new File(fullPath);
            File parentFile = file.getParentFile();
            FileUtil.mkdir(parentFile);
            FileUtil.writeFromStream(inputStream, file);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
        return path;
    }

    @Override
    public InputStream getInputStream(OriginalFileEntity fileEntity) {

        if (fileEntity == null) {
            throw new BaseException("获取文件失败：参数不能为空");
        }

        String path = fileEntity.getOriginalPath();
        if (StringUtils.isEmpty(path)) {
            throw new BaseException("获取文件失败：文件路径为空");
        }
        path = path.replace("/", File.separator);
        String fullPath = FileUtils.getFilePath() + File.separator + path;
        File file = new File(fullPath);
        if (!FileUtil.exist(file)) {
            throw new BaseException("获取文件失败：文件不存在");
        }
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
    }


}
