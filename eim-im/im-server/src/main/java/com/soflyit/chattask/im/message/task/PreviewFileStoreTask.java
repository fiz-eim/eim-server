package com.soflyit.chattask.im.message.task;

import com.soflyit.chattask.im.event.domain.MessageFileUploadedEvent;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import com.soflyit.chattask.im.message.domain.vo.MessageFileVo;
import com.soflyit.chattask.im.message.mapper.MessageMapper;
import com.soflyit.chattask.im.message.service.impl.ImageFileService;
import com.soflyit.chattask.im.message.service.impl.MinioStorageService;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 类描述<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-12 16:50
 */
@Slf4j
public class PreviewFileStoreTask implements Runnable {

    private ImageFileService imageFileService;

    private MinioStorageService fileStorageService;

    private File file;

    private MessageMapper messageMapper;

    private MsgFile msgFile;

    private ChatEventService chatEventService;

    public PreviewFileStoreTask(ImageFileService imageFileService, MinioStorageService fileStorageService, ChatEventService chatEventService, File file, MsgFile msgFile) {
        this.imageFileService = imageFileService;
        this.fileStorageService = fileStorageService;
        this.chatEventService = chatEventService;
        this.file = file;
        this.msgFile = msgFile;
    }

    @Override
    public void run() {
        log.debug("开始执行保存文件任务");
        if (file != null && msgFile != null && fileStorageService != null && imageFileService != null) {
            boolean saveResult = imageFileService.processImageFile(file, msgFile, fileStorageService);
            if (saveResult) {

                String uuid = msgFile.getUuid();
                processFileUploadEvent(msgFile, uuid);
            }
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

    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }
}
