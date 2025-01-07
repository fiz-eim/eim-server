package com.soflyit.chattask.im.message.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.soflyit.chattask.im.config.SoflyImFileConfig;
import com.soflyit.chattask.im.event.domain.MessageFileUploadedEvent;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgFailedFile;
import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import com.soflyit.chattask.im.message.domain.vo.MessageFileVo;
import com.soflyit.chattask.im.message.mapper.MessageMapper;
import com.soflyit.chattask.im.message.mapper.MsgFailedFileMapper;
import com.soflyit.chattask.im.message.service.IMsgFailedFileService;
import com.soflyit.chattask.im.message.service.IMsgFileService;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.soflyit.chattask.im.common.constant.ImFileConstant.*;

/**
 * 失败文件Service业务层处理
 *
 * @author soflyit
 * @date 2024-01-10 13:52:41
 */
@Service
@Slf4j
public class MsgFailedFileServiceImpl implements IMsgFailedFileService {
    private MsgFailedFileMapper msgFailedFileMapper;

    private IMsgFileService msgFileService;

    private SoflyImFileConfig imFileConfig;

    private MinioStorageService fileStorageService;

    private MessageMapper messageMapper;

    private ChatEventService chatEventService;



    @Override
    public MsgFailedFile selectMsgFailedFileById(Long id) {
        return msgFailedFileMapper.selectMsgFailedFileById(id);
    }


    @Override
    public List<MsgFailedFile> selectMsgFailedFileList(MsgFailedFile msgFailedFile) {
        return msgFailedFileMapper.selectMsgFailedFileList(msgFailedFile);
    }


    @Override
    public int insertMsgFailedFile(MsgFailedFile msgFailedFile) {
        msgFailedFile.setCreateTime(DateUtils.getNowDate());
        return msgFailedFileMapper.insertMsgFailedFile(msgFailedFile);
    }


    @Override
    public int updateMsgFailedFile(MsgFailedFile msgFailedFile) {
        msgFailedFile.setUpdateTime(DateUtils.getNowDate());
        return msgFailedFileMapper.updateMsgFailedFile(msgFailedFile);
    }


    @Override
    public int deleteMsgFailedFileByIds(Long[] ids) {
        return msgFailedFileMapper.deleteMsgFailedFileByIds(ids);
    }


    @Override
    public int deleteMsgFailedFileById(Long id) {
        return msgFailedFileMapper.deleteMsgFailedFileById(id);
    }


    @Override
    public void saveFailedFileData(File file, MsgFile msgFile, Short fileType) {

        File parent = file.getParentFile();
        File failedDir = new File(parent, "failed");
        failedDir.mkdirs();
        log.debug("创建失败目录: {}", failedDir.getAbsolutePath());
        File failedFile = new File(failedDir, file.getName());
        FileUtil.move(file, failedFile, true);
        log.debug("失败文件: {}", failedFile.getAbsolutePath());

        MsgFailedFile msgFailedFile = new MsgFailedFile();
        msgFailedFile.setMsgId(msgFile.getMsgId());
        msgFailedFile.setFilePath(file.getName());
        msgFailedFile.setFailedCount(0);
        msgFailedFile.setFileId(msgFile.getId());
        msgFailedFile.setId(SnowflakeIdUtil.nextId());
        msgFailedFile.setUuid(msgFile.getUuid());
        msgFailedFile.setStatus(FAILED_FILE_STATUS_WAIT);
        msgFailedFile.setFileType(fileType == null ? FILE_TYPE_ORIGINAL : fileType);
        msgFailedFileMapper.insertMsgFailedFile(msgFailedFile);
    }


    @Override
    public void processFiledFile() {
        log.debug("开始执行失败文件重试");
        File templateFile = new File(imFileConfig.getMsgFileTmpDir());
        File failedFileDir = null;
        if (FileUtil.isDirectory(templateFile)) {
            failedFileDir = new File(templateFile, "failed");
        } else {
            log.error("失败文件重试失败，目录不存在：{}", templateFile.getAbsolutePath());
        }
        if (FileUtil.isDirectory(failedFileDir)) {
            String[] failedFiles = failedFileDir.list();
            log.debug("失败文件列表：{}， {}", failedFileDir.getAbsolutePath(), Arrays.toString(failedFiles));
            if (ArrayUtils.isNotEmpty(failedFiles)) {
                processFiledFiles(failedFiles, failedFileDir);
            }
        }
    }

    private void processFiledFiles(String[] failedFiles, File failedFileDir) {

        LambdaQueryWrapper<MsgFailedFile> msgFailedFileQueryWrapper = new LambdaQueryWrapper<>();
        msgFailedFileQueryWrapper.select(MsgFailedFile::getId, MsgFailedFile::getFilePath, MsgFailedFile::getFileType,
                MsgFailedFile::getStatus, MsgFailedFile::getFailedCount, MsgFailedFile::getMsgId, MsgFailedFile::getFileId);
        msgFailedFileQueryWrapper.in(MsgFailedFile::getFilePath, failedFiles);
        msgFailedFileQueryWrapper.in(MsgFailedFile::getStatus, new Short[]{FAILED_FILE_STATUS_WAIT, FAILED_FILE_STATUS_FAILED});
        msgFailedFileQueryWrapper.or((wrapper) -> {
            wrapper.eq(MsgFailedFile::getStatus, FAILED_FILE_STATUS_RETRYING).lt(MsgFailedFile::getUpdateTime, DateUtil.offsetDay(new Date(), -1));
        });


        List<MsgFailedFile> failedMsgFiles = msgFailedFileMapper.selectList(msgFailedFileQueryWrapper);

        if (CollectionUtils.isNotEmpty(failedMsgFiles)) {

            LambdaUpdateWrapper<MsgFailedFile> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(MsgFailedFile::getStatus, FAILED_FILE_STATUS_RETRYING);
            updateWrapper.in(MsgFailedFile::getId, failedMsgFiles.stream().map(MsgFailedFile::getId).collect(Collectors.toList()));
            msgFailedFileMapper.update(null, updateWrapper);

            failedMsgFiles.forEach(failedFile -> {
                processFiledFile(failedFile, failedFileDir);
            });
        } else {
            if (imFileConfig.getEnableClearUnMatchTempFile() != null && imFileConfig.getEnableClearUnMatchTempFile()) {

                for (String failedFile : failedFiles) {
                    FileUtil.del(new File(failedFileDir, failedFile));
                }
            }

        }
    }


    private void processFiledFile(MsgFailedFile failedFile, File failedFileDir) {
        Short fileType = failedFile.getFileType();
        if (fileType == null) {
            log.error("处理上传失败文件失败，文件类型不能为空, id:{}, path:{}", failedFile.getId(), failedFile.getFilePath());
            return;
        }


        MsgFile msgFile = msgFileService.selectMsgFileById(failedFile.getFileId());
        File tempFile = new File(failedFileDir, failedFile.getFilePath());
        if (msgFile == null) {
            log.warn("文件信息不存在，删除临时文件");
            FileUtil.del(tempFile);
            msgFailedFileMapper.deleteMsgFailedFileById(failedFile.getFileId());
            return;
        }
        Long channelId = msgFile.getChannelId();


        Short currentType = Integer.valueOf(FILE_TYPE_ORIGINAL & fileType).shortValue();

        if (FILE_TYPE_ORIGINAL.equals(currentType)) {
            processOriginalFile(failedFile, tempFile, msgFile);
        } else {
            processFileByType(failedFile, tempFile, msgFile, fileType);
        }


        if (!FileUtil.exist(tempFile)) {

            String uuid = msgFile.getUuid();
            processFileUploadEvent(msgFile, uuid);
        }

    }


    private void processFileUploadEvent(MsgFile msgFile, String uuid) {

        MessageFileUploadedEvent event = new MessageFileUploadedEvent();
        MessageFileVo fileVo = new MessageFileVo();
        BeanUtils.copyBeanProp(fileVo, msgFile);
        fileVo.setPath(null);
        fileVo.setPreviewPath(null);
        fileVo.setThumbnailPath(null);
        fileVo.setHashMd5(null);
        fileVo.setHashSha1(null);
        fileVo.setHashSha256(null);
        fileVo.setUuid(uuid);
        fileVo.getParams().clear();
        if (StringUtils.isNotEmpty(msgFile.getExtension())) {
            fileVo.setName(msgFile.getName() + "." + msgFile.getExtension());
        }
        Message messageData = messageMapper.selectMessageById(msgFile.getMsgId());
        fileVo.setMessage(messageData);

        event.setData(fileVo);

        Object token = msgFile.getParams().get("token");
        if (token != null) {
            event.setToken(String.valueOf(token));
        }
        event.setUserId(msgFile.getCreateBy());

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        broadcast.setChannelId(msgFile.getChannelId());
        event.setBroadcast(broadcast);
        chatEventService.doProcessEvent(event);

    }


    private void processFileByType(MsgFailedFile failedFile, File tempFile, MsgFile msgFile, Short fileType) {
        short currentType = fileType;
        Short previewType = (short) (fileType & FILE_TYPE_PREVIEW);
        Short thumbnailType = (short) (fileType & FILE_TYPE_THUMBNAIL);

        if (previewType.equals(FILE_TYPE_PREVIEW)) {
            boolean result = processPreviewFile(tempFile, msgFile);
            if (result) {
                currentType -= FILE_TYPE_PREVIEW;
            }
        }

        if (thumbnailType.equals(FILE_TYPE_THUMBNAIL)) {
            boolean result = processThumbnailFile(tempFile, msgFile);
            if (result) {
                currentType -= FILE_TYPE_PREVIEW;
            }
        }

        if ((currentType & FILE_TYPE_PREVIEW) == 0 && ((currentType & FILE_TYPE_THUMBNAIL) == 0)) {
            msgFailedFileMapper.deleteMsgFailedFileById(failedFile.getFileId());
            FileUtil.del(tempFile);
        } else {
            failedFile.setFileType(currentType);
            failedFile.setFailedCount(failedFile.getFailedCount() + 1);
            failedFile.setUpdateTime(null);
            failedFile.setStatus(FAILED_FILE_STATUS_FAILED);
            msgFailedFileMapper.updateMsgFailedFile(failedFile);
        }
    }


    private boolean processThumbnailFile(File tempFile, MsgFile msgFile) {

        String fileName = String.valueOf(msgFile.getId());
        String previewName = fileName + THUMBNAIL_SUFFIX + "." + msgFile.getExtension();
        String path = null;
        try {
            byte[] thumbnailData = generateThumbnail(tempFile, msgFile);

            path = fileStorageService.savePreviewFile(thumbnailData, msgFile, previewName);
            if (path != null) {
                msgFile.setThumbnailPath(path);
                msgFileService.updateMsgFile(msgFile);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

        return path != null;
    }


    private boolean processPreviewFile(File tempFile, MsgFile msgFile) {

        String fileName = String.valueOf(msgFile.getId());
        String previewName = fileName + PREVIEW_SUFFIX + "." + msgFile.getExtension();

        String path = null;

        try {
            byte[] previewData = generatePreview(tempFile, msgFile);

            path = fileStorageService.savePreviewFile(previewData, msgFile, previewName);
            if (path != null) {
                msgFile.setPreviewPath(path);
                msgFileService.updateMsgFile(msgFile);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

        return path != null;
    }


    private void processOriginalFile(MsgFailedFile failedFile, File tempFile, MsgFile msgFile) {

        short filedType = FILE_TYPE_ORIGINAL;
        try {
            String originalName = msgFile.getName();
            if (StringUtils.isNotEmpty(msgFile.getExtension())) {
                originalName = originalName + "." + msgFile.getExtension();
            }

            String path = fileStorageService.saveFile(tempFile, originalName, msgFile);

            if (path == null) {
                failedFile.setFailedCount(failedFile.getFailedCount() + 1);
                failedFile.setStatus(FAILED_FILE_STATUS_FAILED);
                failedFile.setUpdateTime(null);
                msgFailedFileMapper.updateMsgFailedFile(failedFile);
                return;
            }
            filedType -= FILE_TYPE_ORIGINAL;
            msgFile.setPath(path);
            log.debug("保存原始文件完成");

            if (msgFile.getWidth() != null) {
                filedType += (FILE_TYPE_PREVIEW + FILE_TYPE_THUMBNAIL);
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

                byte[] previewData = generatePreview(tempFile, msgFile);
                path = fileStorageService.savePreviewFile(previewData, msgFile, previewName);
                msgFile.setPreviewPath(path);
                if (path != null) {
                    filedType -= FILE_TYPE_PREVIEW;
                }

                byte[] thumbnailData = generateThumbnail(tempFile, msgFile);
                String thumbnailName = fileName + THUMBNAIL_SUFFIX + "." + msgFile.getExtension();
                path = fileStorageService.savePreviewFile(thumbnailData, msgFile, thumbnailName);
                msgFile.setThumbnailPath(path);
                if (path != null) {
                    filedType -= FILE_TYPE_THUMBNAIL;
                }
            }
            msgFileService.updateMsgFile(msgFile);

            if (filedType > 0) {
                failedFile.setFailedCount(failedFile.getFailedCount() + 1);
                failedFile.setStatus(FAILED_FILE_STATUS_FAILED);
                failedFile.setUpdateTime(null);
                msgFailedFileMapper.updateMsgFailedFile(failedFile);
            } else {
                msgFailedFileMapper.deleteMsgFailedFileById(failedFile.getId());
                FileUtil.del(tempFile);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            if (filedType > 0) {
                failedFile.setFailedCount(failedFile.getFailedCount() + 1);
                failedFile.setStatus(FAILED_FILE_STATUS_FAILED);
                failedFile.setUpdateTime(null);
                msgFailedFileMapper.updateMsgFailedFile(failedFile);

            }
        }
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
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Autowired
    public void setImFileConfig(SoflyImFileConfig imFileConfig) {
        this.imFileConfig = imFileConfig;
    }

    @Autowired
    public void setMsgFailedFileMapper(MsgFailedFileMapper msgFailedFileMapper) {
        this.msgFailedFileMapper = msgFailedFileMapper;
    }

    @Autowired
    public void setMsgFileService(IMsgFileService msgFileService) {
        this.msgFileService = msgFileService;
    }

    @Autowired(required = false)
    public void setFileStorageService(MinioStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Autowired
    public void setChatEventService(ChatEventService chatEventService) {
        this.chatEventService = chatEventService;
    }
}
