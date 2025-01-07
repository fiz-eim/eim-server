package com.soflyit.chattask.im.message.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import com.soflyit.chattask.im.message.service.IMsgFailedFileService;
import com.soflyit.chattask.im.message.service.IMsgFileService;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

import static com.soflyit.chattask.im.common.constant.ImFileConstant.*;

/**
 * 图片文件处理服务：生成预览图片及缩略图，并更新到数据库<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-12 16:20
 */
@Component
@Slf4j
public class ImageFileService {

    private IMsgFileService msgFileService;

    private IMsgFailedFileService failedFileService;

    public boolean processImageFile(File file, MsgFile msgFile, MinioStorageService storageService) {

        short filedType = FILE_TYPE_ORIGINAL;
        short fileTypeCache = Short.valueOf("0");

        try {

            String path = storageService.saveFile(file, (String) msgFile.getParams().get("originalFileName"), msgFile);
            msgFile.setPath(path);
            log.debug("保存原始文件完成");

            if (StringUtils.isNotEmpty(path)) {
                filedType -= FILE_TYPE_ORIGINAL;
                fileTypeCache += FILE_TYPE_ORIGINAL;
            }

            if (msgFile.getWidth() != null) {
                filedType += (FILE_TYPE_PREVIEW + FILE_TYPE_THUMBNAIL);
                fileTypeCache += (FILE_TYPE_PREVIEW + FILE_TYPE_THUMBNAIL);
                msgFile.setHasPreviewImage(PREVIEW_FLAG_TRUE);
                Long fileId = msgFile.getId();
                if (fileId == null) {
                    fileId = (Long) msgFile.getParams().get("fileId");
                }
                if (fileId == null) {
                    fileId = SnowflakeIdUtil.nextId();
                }
                String fileName = String.valueOf(fileId);
                String previewName = fileName + PREVIEW_SUFFIX + "." + msgFile.getExtension();

                byte[] previewData = generatePreview(file, msgFile);
                path = storageService.savePreviewFile(previewData, msgFile, previewName);
                msgFile.setPreviewPath(path);
                if (StringUtils.isNotEmpty(path)) {
                    filedType -= FILE_TYPE_PREVIEW;
                }

                byte[] thumbnailData = generateThumbnail(file, msgFile);
                String thumbnailName = fileName + THUMBNAIL_SUFFIX + "." + msgFile.getExtension();
                path = storageService.savePreviewFile(thumbnailData, msgFile, thumbnailName);
                msgFile.setThumbnailPath(path);
                if (StringUtils.isNotEmpty(path)) {
                    filedType -= FILE_TYPE_THUMBNAIL;
                }

                generateMiniPreview(thumbnailData, msgFile);
            }
            int effectCount = msgFileService.updateMsgFile(msgFile);

            if (filedType > 0 || effectCount < 1) {
                if (filedType < 1) {
                    filedType = fileTypeCache;
                }
                saveFailedFileData(file, msgFile, filedType);
            } else {
                FileUtil.del(file);
            }
            return filedType < 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            if (filedType > 0) {
                saveFailedFileData(file, msgFile, filedType);
            }
            return Boolean.FALSE;
        }
    }

    private void saveFailedFileData(File file, MsgFile msgFile, Short fileType) {
        failedFileService.saveFailedFileData(file, msgFile, fileType);
    }


    private void generateMiniPreview(byte[] data, MsgFile msgFile) {
        StringBuffer miniPreView = new StringBuffer(2048);
        miniPreView.append("data:").append(msgFile.getMimeType()).append(";base64,");
        miniPreView.append(Base64.encode(data));
        msgFile.setMiniPreview(miniPreView.toString());
    }


    private byte[] generatePreview(File file, MsgFile msgFile) {
        log.debug("开始处理预览文件");
        int width = msgFile.getWidth().intValue();
        int height = msgFile.getHeight().intValue();
        try (InputStream is = new FileInputStream(file)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100 * 1024);
            int previewWidth = NumberUtil.min(width, MAX_PREVIEW_WIDTH);
            int previewHeight = NumberUtil.min(height, MAX_PREVIEW_HEIGHT);
            Thumbnails.of(is).size(previewWidth, previewHeight).outputQuality(0.5).toOutputStream(baos);
            log.debug("生成预览图完成====>{}", msgFile.getName());
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    private byte[] generateThumbnail(File file, MsgFile msgFile) {
        log.debug("开始处理缩略图文件");
        int width = msgFile.getWidth().intValue();
        int height = msgFile.getHeight().intValue();
        try (InputStream is = new FileInputStream(file)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(Long.valueOf(3 * 1024).intValue());
            int thumbnailWidth = NumberUtil.min(width, MAX_THUMBNAIL_WIDTH);
            int thumbnailHeight = NumberUtil.min(height, MAX_THUMBNAIL_HEIGHT);
            Thumbnails.of(is).size(thumbnailWidth, thumbnailHeight).outputQuality(0.7).toOutputStream(baos);
            log.debug("存储缩略图文件====>{}", msgFile.getId());
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public void setMsgFileService(IMsgFileService msgFileService) {
        this.msgFileService = msgFileService;
    }

    @Autowired
    public void setFailedFileService(IMsgFailedFileService failedFileService) {
        this.failedFileService = failedFileService;
    }
}
