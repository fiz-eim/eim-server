package com.soflyit.chattask.im.message.service.impl;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.config.SoflyImFileConfig;
import com.soflyit.chattask.im.event.domain.MessageFileUploadedEvent;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import com.soflyit.chattask.im.message.domain.vo.MessageFileVo;
import com.soflyit.chattask.im.message.mapper.MessageMapper;
import com.soflyit.chattask.im.message.mapper.MsgFileMapper;
import com.soflyit.chattask.im.message.service.IMsgFileService;
import com.soflyit.chattask.im.message.task.PreviewFileStoreTask;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.common.core.utils.PageUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.utils.file.ImageUtils;
import com.soflyit.common.core.utils.file.image.FastImageInfo;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.security.utils.SecurityUtils;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static com.soflyit.chattask.im.common.constant.ImFileConstant.*;
import static com.soflyit.common.core.utils.PageUtils.startPage;

/**
 * 消息文件Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:18
 */
@Service
@Slf4j
public class MsgFileServiceImpl implements IMsgFileService {

    private static final List<String> imageType = new ArrayList<>();

    private SoflyImFileConfig soflyImFileConfig;

    private MinioStorageService fileStorageService;

    private MsgFileMapper msgFileMapper;

    private ImageFileService imageFileService;

    private ThreadPoolTaskExecutor taskExecutor;

    private ChannelMemberMapper channelMemberMapper;

    private ChatEventService chatEventService;

    private MessageMapper messageMapper;

    @Autowired
    private UserNickNameService nickNameService;


    @Override
    public MsgFile selectMsgFileById(Long id) {
        return msgFileMapper.selectMsgFileById(id);
    }


    @Override
    public List<MsgFile> selectMsgFileList(MsgFile msgFile) {
        return msgFileMapper.selectMsgFileList(msgFile);
    }


    @Override
    public int insertMsgFile(MsgFile msgFile) {
        msgFile.setCreateTime(DateUtils.getNowDate());
        return msgFileMapper.insertMsgFile(msgFile);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult<MsgFile> uploadFile(MultipartFile file, Long channelId, Long messageId, String uuid) {
        log.debug("开始处理文件。。。。");
        MsgFile msgFile = new MsgFile();
        msgFile.setMsgId(messageId);
        msgFile.setUuid(uuid);
        msgFile.setCreateBy(SecurityUtils.getUserId());
        msgFile.setChannelId(channelId);

        msgFile.setName(FileNameUtil.mainName(file.getOriginalFilename()));
        msgFile.setExtension(FileNameUtil.extName(file.getOriginalFilename()));
        msgFile.setMimeType(file.getContentType());
        msgFile.setSize(file.getSize());
        msgFile.setStorageType(soflyImFileConfig.getStorageType());


        processFile(file, msgFile);

        LambdaQueryWrapper<MsgFile> queryWrapper = new LambdaQueryWrapper<>(MsgFile.class);
        queryWrapper.eq(MsgFile::getHashMd5, msgFile.getHashMd5());
        queryWrapper.eq(MsgFile::getHashSha1, msgFile.getHashSha1());
        queryWrapper.eq(MsgFile::getHashSha256, msgFile.getHashSha256());
        queryWrapper.eq(MsgFile::getStorageType, soflyImFileConfig.getStorageType());
        queryWrapper.isNotNull(MsgFile::getPath);
        queryWrapper.last(" limit 1");
        List<MsgFile> msgFileInDBs = msgFileMapper.selectList(queryWrapper);
        log.debug("开始保存文件信息。。。。");
        insertMsgFile(msgFile);
        log.debug("文件信息保存完成。。。。");
        MsgFile msgFileCopy = null;

        if (CollectionUtils.isNotEmpty(msgFileInDBs) && msgFileInDBs.get(0) != null) {
            MsgFile msgFileInDB = msgFileInDBs.get(0);
            msgFile.setPath(msgFileInDB.getPath());
            msgFile.setPreviewPath(msgFileInDB.getPreviewPath());
            msgFile.setThumbnailPath(msgFileInDB.getThumbnailPath());
            msgFile.setMiniPreview(msgFileInDB.getMiniPreview());
            msgFile.setHasPreviewImage(msgFileInDB.getHasPreviewImage());
            msgFile.setExternalId(msgFileInDB.getExternalId());
            msgFile.setRevision(1L);
            msgFile.setExtData(msgFileInDB.getExtData());
            msgFileMapper.updateMsgFile(msgFile);

            processFileUploadEvent(msgFile, uuid);
        } else {
            msgFileCopy = JSON.parseObject(JSON.toJSONString(msgFile), MsgFile.class);
        }
        if (messageId != null) {
            Message message = messageMapper.selectMessageById(messageId);
            if (message != null) {
                String msgFiles = message.getMsgFiles();
                List<Long> fileIds = JSON.parseArray(StringUtils.defaultString(msgFiles), Long.class);
                fileIds.add(msgFile.getId());
                message.setMsgFiles(JSON.toJSONString(fileIds));
            }
            messageMapper.updateMessage(message);
        }
        if (msgFileCopy != null) {

            saveFile(file, msgFileCopy);
        }
        msgFile.setPath(null);
        msgFile.getParams().clear();

        return AjaxResult.success(msgFile);
    }


    private void processFileUploadEvent(MsgFile msgFile, String uuid) {

        MessageFileVo fileVo = new MessageFileVo();
        BeanUtils.copyBeanProp(fileVo, msgFile);
        fileVo.setUuid(uuid);

        fileVo.setPath(null);
        fileVo.setPreviewPath(null);
        fileVo.setThumbnailPath(null);
        fileVo.setHashMd5(null);
        fileVo.setHashSha1(null);
        fileVo.setHashSha256(null);
        fileVo.setUuid(uuid);
        if (StringUtils.isNotEmpty(msgFile.getExtension())) {
            fileVo.setName(msgFile.getName() + "." + msgFile.getExtension());
        }

        Message messageData = messageMapper.selectMessageById(msgFile.getMsgId());
        fileVo.setMessage(messageData);

        MessageFileUploadedEvent event = new MessageFileUploadedEvent();
        event.setData(fileVo);
        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        broadcast.setChannelId(msgFile.getChannelId());
        event.setBroadcast(broadcast);
        chatEventService.doProcessEvent(event);

    }


    @Override
    public AjaxResult deleteMsgFile(Long id) {
        return AjaxResult.error("暂不支持删除文件");
    }


    @Override
    public void downloadFile(HttpServletResponse response, Long id) {

        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new BaseException("下载失败：没有权限");
        }

        MsgFile msgFile = selectMsgFileById(id);
        if (msgFile == null || msgFile.getDeleteTime() > -1) {
            throw new BaseException("下载失败: 文件不存在或已删除");
        }
        ChannelMember condition = new ChannelMember();
        condition.setUserId(userId);
        condition.setChannelId(msgFile.getChannelId());

        List<ChannelMember> channelMembers = channelMemberMapper.selectChannelMemberList(condition);
        if (CollectionUtils.isEmpty(channelMembers)) {
            throw new BaseException("下载失败：没有权限");
        }

        try (InputStream is = fileStorageService.getFileInputStream(msgFile, FILE_TYPE_ORIGINAL)) {
            if (is == null) {
                throw new BaseException("下载失败：获取文件失败");
            }
            String fileName = URLEncoder.encode(msgFile.getName() + "." + msgFile.getExtension(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            int length = is.available();
            if (length > 0) {
                response.addHeader("Content-Length", "" + is.available());
            }
            response.setContentType("application/octet-stream; charset=UTF-8");
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(is, outputStream, 1024 * 8);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
    }


    @Override
    public void previewFile(HttpServletResponse response, Long id) {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new BaseException("获取预览文件失败：没有权限");
        }

        MsgFile msgFile = selectMsgFileById(id);
        if (msgFile == null || msgFile.getDeleteTime() > -1) {
            throw new BaseException("获取预览文件失败：文件不存在或已删除");
        }
        ChannelMember condition = new ChannelMember();
        condition.setUserId(userId);
        condition.setChannelId(msgFile.getChannelId());

        List<ChannelMember> channelMembers = channelMemberMapper.selectChannelMemberList(condition);
        if (CollectionUtils.isEmpty(channelMembers)) {
            throw new BaseException("获取预览文件失败：没有权限");
        }

        try (InputStream is = fileStorageService.getFileInputStream(msgFile, FILE_TYPE_PREVIEW)) {
            if (is == null) {
                throw new BaseException("获取预览文件失败");
            }
            String fileName = URLEncoder.encode(msgFile.getName() + "." + msgFile.getExtension(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            int length = is.available();
            if (length > 0) {
                response.addHeader("Content-Length", String.valueOf(length));
            } else {
                response.addHeader("Transfer-Encoding", "chunked");
            }
            log.debug("================文件预览==============={}", length);
            response.setContentType(msgFile.getMimeType());
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(is, outputStream, 1024 * 8);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
    }


    @Override
    public void thumbnailFile(HttpServletResponse response, Long id) {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new BaseException("获取缩略图文件失败：没有权限");
        }

        MsgFile msgFile = selectMsgFileById(id);
        if (msgFile == null || msgFile.getDeleteTime() > -1) {
            throw new BaseException("获取缩略图文件失败：文件不存在或已删除");
        }
        ChannelMember condition = new ChannelMember();
        condition.setUserId(userId);
        condition.setChannelId(msgFile.getChannelId());

        List<ChannelMember> channelMembers = channelMemberMapper.selectChannelMemberList(condition);
        if (CollectionUtils.isEmpty(channelMembers)) {
            throw new BaseException("获取缩略图文件失败：没有权限");
        }

        try (InputStream is = fileStorageService.getFileInputStream(msgFile, FILE_TYPE_THUMBNAIL)) {
            if (is == null) {
                throw new BaseException("获取缩略图文件失败");
            }
            String fileName = URLEncoder.encode(msgFile.getName() + "." + msgFile.getExtension(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            int length = is.available();
            if (length > 0) {
                response.addHeader("Content-Length", "" + is.available());
            }
            response.setContentType(msgFile.getMimeType());
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(is, outputStream, 1024 * 8);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
    }


    @Override
    public AjaxResult getFileInfo(Long id) {

        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new BaseException("获取文件失败：没有权限");
        }

        MsgFile msgFile = selectMsgFileById(id);
        if (msgFile == null || msgFile.getDeleteTime() > -1) {
            throw new BaseException("获取文件失败：文件不存在或已删除");
        }
        ChannelMember condition = new ChannelMember();
        condition.setUserId(userId);
        condition.setChannelId(msgFile.getChannelId());

        List<ChannelMember> channelMembers = channelMemberMapper.selectChannelMemberList(condition);
        if (CollectionUtils.isEmpty(channelMembers)) {
            throw new BaseException("获取文件失败：没有权限");
        }
        msgFile.setPath(null);
        msgFile.setExternalId(null);
        msgFile.setPreviewPath(null);
        msgFile.setThumbnailPath(null);
        return AjaxResult.success(msgFile);
    }


    @Override
    public AjaxResult<List<MsgFile>> selectNormalFile(MsgFile msgFile) {
        if (msgFile == null) {
            throw new BaseException("获取缩文件失败：查询条件不能为空");
        }
        Long channelId = msgFile.getChannelId();
        List<Long> channelIds = new ArrayList<>();
        if (channelId == null) {
            Long userId = SecurityUtils.getUserId();

            List<ChannelMember> members = channelMemberMapper.selectChannelByUserId(userId);
            if (CollectionUtils.isNotEmpty(members)) {
                members.forEach(member -> {
                    channelIds.add(member.getChannelId());
                });
            }
        }
        if (CollectionUtils.isEmpty(channelIds) && channelId == null) {
            AjaxResult result = AjaxResult.success(new ArrayList<>());
            result.putExtData("hastNext", Boolean.FALSE);
            return result;
        }

        startPage();
        LambdaQueryWrapper<MsgFile> queryWrapper = getMsgFileLambdaQueryWrapper(msgFile, channelId, channelIds, null);
        List<MsgFile> msgFileList = msgFileMapper.selectList(queryWrapper);
        AjaxResult<List<MsgFile>> result = AjaxResult.success(msgFileList);

        if (CollectionUtils.isEmpty(msgFileList)) {
            result.putExtData("hastNext", Boolean.FALSE);
            return result;
        }

        Set<Long> userIds = msgFileList.stream().map(item -> item.getCreateBy()).collect(Collectors.toSet());
        Map<Long, String> userNameMap = nickNameService.getNickNameByIds(new ArrayList<>(userIds));
        msgFileList.forEach(item -> {
            item.setCreateUser(userNameMap.get(item.getCreateBy()));
        });


        Integer pageSize = PageUtils.pageSize();
        Integer pageNumber = PageUtils.pageNum();
        if (pageNumber == null) {
            pageNumber = 1;
        }
        boolean hasNext = Boolean.FALSE;
        if (pageSize != null) {
            PageInfo pageInfo = new PageInfo(msgFileList, pageSize);
            long total = pageInfo.getTotal();
            hasNext = Long.valueOf(pageSize) * pageNumber < total;
            result.putExtData("hastNext", hasNext);
        } else {
            result.putExtData("hastNext", hasNext);
        }

        if (hasNext) {
            Long nextMessageId = processNextFile(msgFileList, msgFile, channelIds);
            result.putExtData("nextId", nextMessageId);
        }
        return result;

    }


    @Override
    public AjaxResult<List<MsgFile>> selectMediaFile(MsgFile msgFile) {
        if (msgFile == null) {
            throw new BaseException("获取文件失败：查询条件不能为空");
        }
        Long channelId = msgFile.getChannelId();
        List<Long> channelIds = new ArrayList<>();
        if (channelId == null) {
            Long userId = SecurityUtils.getUserId();

            List<ChannelMember> members = channelMemberMapper.selectChannelByUserId(userId);
            if (CollectionUtils.isNotEmpty(members)) {
                members.forEach(member -> {
                    channelIds.add(member.getChannelId());
                });
            }
        }


        if (CollectionUtils.isEmpty(channelIds) && channelId == null) {
            AjaxResult result = AjaxResult.success(new ArrayList<>());
            result.putExtData("hastNext", Boolean.FALSE);
            return result;
        }

        startPage();
        LambdaQueryWrapper<MsgFile> queryWrapper = getMsgMediaLambdaQueryWrapper(msgFile, channelId, channelIds, null);

        List<MsgFile> msgFileList = msgFileMapper.selectList(queryWrapper);

        AjaxResult<List<MsgFile>> result = AjaxResult.success(msgFileList);
        if (CollectionUtils.isEmpty(msgFileList)) {
            result.putExtData("hasNext", Boolean.FALSE);
            return result;
        }

        Set<Long> userIds = msgFileList.stream().map(item -> item.getCreateBy()).collect(Collectors.toSet());
        Map<Long, String> userNameMap = nickNameService.getNickNameByIds(new ArrayList<>(userIds));
        msgFileList.forEach(item -> {
            item.setCreateUser(userNameMap.get(item.getCreateBy()));
        });


        Integer pageSize = PageUtils.pageSize();
        Integer pageNumber = PageUtils.pageNum();
        if (pageNumber == null) {
            pageNumber = 1;
        }
        PageInfo pageInfo = new PageInfo(msgFileList, pageSize);
        long total = pageInfo.getTotal();
        boolean hasNext = Long.valueOf(pageSize) * pageNumber < total;
        result.putExtData("hastNext", hasNext);

        if (hasNext) {
            Long nextMessageId = processNextMedia(msgFileList, msgFile, channelIds);
            result.putExtData("nextId", nextMessageId);
        }

        return result;
    }

    @NotNull
    private static LambdaQueryWrapper<MsgFile> getMsgMediaLambdaQueryWrapper(MsgFile msgFile, Long channelId, List<Long> channelIds, Long msgFileId) {
        LambdaQueryWrapper<MsgFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(MsgFile::getId, MsgFile::getChannelId, MsgFile::getName, MsgFile::getExtension, MsgFile::getPreviewPath,
                MsgFile::getCreateBy, MsgFile::getCreateTime, MsgFile::getMimeType, MsgFile::getExternalId, MsgFile::getSize, MsgFile::getMiniPreview);
        if (channelId != null) {
            queryWrapper.eq(MsgFile::getChannelId, channelId);
        } else if (CollectionUtils.isNotEmpty(channelIds)) {
            queryWrapper.in(MsgFile::getChannelId, channelIds);
        } else {
            throw new BaseException("查询文件信息失败：无法判断用户所在群组");
        }
        queryWrapper.isNotNull(MsgFile::getWidth);

        if (StringUtils.isNotEmpty(msgFile.getName())) {
            queryWrapper.like(MsgFile::getName, msgFile.getName());
        }

        String beginTimeStr = (String) msgFile.getParams().get("beginTime");
        Date beginTime = DateUtils.parseDate(beginTimeStr);
        if (beginTime != null) {
            queryWrapper.ge(MsgFile::getCreateTime, beginTime);
        }
        String endTimeStr = (String) msgFile.getParams().get("endTime");
        Date endTime = DateUtils.parseDate(endTimeStr);
        if (endTime != null) {
            queryWrapper.ge(MsgFile::getCreateTime, endTime);
        }
        queryWrapper.orderByAsc(MsgFile::getId);
        if (msgFileId != null) {
            queryWrapper.gt(MsgFile::getId, msgFileId);
            queryWrapper.last(" limit 1");
        }

        return queryWrapper;
    }

    @NotNull
    private static LambdaQueryWrapper<MsgFile> getMsgFileLambdaQueryWrapper(MsgFile msgFile, Long channelId, List<Long> channelIds, Long msgFileId) {

        LambdaQueryWrapper<MsgFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(MsgFile::getId, MsgFile::getChannelId, MsgFile::getName, MsgFile::getExtension, MsgFile::getCreateBy,
                MsgFile::getMimeType, MsgFile::getExternalId, MsgFile::getSize, MsgFile::getMiniPreview, MsgFile::getCreateTime);
        if (channelId != null) {
            queryWrapper.eq(MsgFile::getChannelId, channelId);
        } else if (CollectionUtils.isNotEmpty(channelIds)) {
            queryWrapper.in(MsgFile::getChannelId, channelIds);
        } else {
            throw new BaseException("查询文件信息失败：无法判断用户所在群组");
        }
        queryWrapper.isNull(MsgFile::getWidth);
        if (StringUtils.isNotEmpty(msgFile.getName())) {
            queryWrapper.like(MsgFile::getName, msgFile.getName());
        }
        List<Long> userIds = (List<Long>) msgFile.getParams().get("userIds");
        if (CollectionUtils.isNotEmpty(userIds)) {
            queryWrapper.in(MsgFile::getCreateBy, userIds);
        }

        String beginTimeStr = (String) msgFile.getParams().get("beginTime");
        Date beginTime = DateUtils.parseDate(beginTimeStr);
        if (beginTime != null) {
            queryWrapper.ge(MsgFile::getCreateTime, beginTime);
        }
        String endTimeStr = (String) msgFile.getParams().get("endTime");
        Date endTime = DateUtils.parseDate(endTimeStr);
        if (endTime != null) {
            queryWrapper.ge(MsgFile::getCreateTime, endTime);
        }
        queryWrapper.orderByAsc(MsgFile::getId);
        if (msgFileId != null) {
            queryWrapper.gt(MsgFile::getId, msgFileId);
            queryWrapper.last(" limit 1");
        }
        return queryWrapper;
    }

    private Long processNextFile(List<MsgFile> msgFileList, MsgFile msgFile, List<Long> channelIds) {

        Long msgFileId = msgFileList.get(msgFileList.size() - 1).getId();
        LambdaQueryWrapper<MsgFile> queryWrapper = getMsgFileLambdaQueryWrapper(msgFile, msgFile.getChannelId(), channelIds, msgFileId);
        List<MsgFile> fileList = msgFileMapper.selectList(queryWrapper);
        Long nextFileId = null;
        if (CollectionUtils.isNotEmpty(fileList)) {
            nextFileId = fileList.get(0).getId();
        }
        return nextFileId;
    }

    private Long processNextMedia(List<MsgFile> msgFileList, MsgFile msgFile, List<Long> channelIds) {

        Long msgFileId = msgFileList.get(msgFileList.size() - 1).getId();
        LambdaQueryWrapper<MsgFile> queryWrapper = getMsgFileLambdaQueryWrapper(msgFile, msgFile.getChannelId(), channelIds, msgFileId);
        List<MsgFile> fileList = msgFileMapper.selectList(queryWrapper);
        Long nextFileId = null;
        if (CollectionUtils.isNotEmpty(fileList)) {
            nextFileId = fileList.get(0).getId();
        }
        return nextFileId;
    }


    private void saveFile(MultipartFile file, MsgFile msgFile) {
        log.debug("开始创建保存文件任务。。。。");
        if (fileStorageService == null) {
            throw new BaseException("保存文件失败，文件存储服务不存在");
        }
        File tmpDir = new File(soflyImFileConfig.getMsgFileTmpDir());
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        File tmp = new File(tmpDir, UUID.randomUUID().toString());
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), tmp);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        msgFile.getParams().put("originalFileName", file.getOriginalFilename());
        msgFile.getParams().put("token", SecurityUtils.getToken());
        PreviewFileStoreTask task = new PreviewFileStoreTask(imageFileService, fileStorageService, chatEventService, tmp, msgFile);
        task.setMessageMapper(messageMapper);
        log.debug("保存文件任务创建完成。。。。");
        taskExecutor.execute(task);
    }


    private void processFile(MultipartFile file, MsgFile msgFile) {
        log.debug("开始解析文件信息");
        try {
            log.debug("开始计算md5");
            InputStream is = file.getInputStream();
            String md5 = DigestUtil.md5Hex(is);
            msgFile.setHashMd5(md5);
            is.close();
            log.debug("开始计算sha1");

            is = file.getInputStream();
            String sha1 = DigestUtil.sha1Hex(is);
            msgFile.setHashSha1(sha1);
            is.close();
            log.debug("开始计算sha256");

            is = file.getInputStream();
            String sha256 = DigestUtil.sha256Hex(is);
            msgFile.setHashSha256(sha256);
            is.close();
            log.debug("开始识别文件类型");
            is = file.getInputStream();
            String type = FileTypeUtil.getType(is, file.getOriginalFilename());
            is.close();
            msgFile.setHasPreviewImage(PREVIEW_FLAG_FALSE);
            if (isImage(type)) {
                log.debug("开始识别图片分辨率");
                msgFile.setHasPreviewImage(PREVIEW_FLAG_TRUE);
                try (InputStream isTmp = file.getInputStream()) {
                    log.debug("开始读取图片信息");
                    FastImageInfo fastImageInfo = ImageUtils.getFastImageInfo(isTmp);

                    log.debug("开始获取文件分辨率");
                    msgFile.setWidth(Long.valueOf(fastImageInfo.getWidth()));
                    msgFile.setHeight(Long.valueOf(fastImageInfo.getHeight()));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        log.debug("文件解析完成");
    }

    private boolean isImage(String type) {
        return imageType.contains(type);
    }


    @Override
    public int updateMsgFile(MsgFile msgFile) {
        msgFile.setUpdateTime(DateUtils.getNowDate());

        MsgFile fileInDb = msgFileMapper.selectMsgFileById(msgFile.getId());
        if (fileInDb == null) {
            return 0;
        }
        Long revision = fileInDb.getRevision();
        if (revision == null) {
            revision = 0L;
        }
        revision++;
        msgFile.setRevision(revision);

        return msgFileMapper.updateMsgFile(msgFile);
    }


    @Override
    public int deleteMsgFileByIds(Long[] ids) {
        return msgFileMapper.deleteMsgFileByIds(ids);
    }


    @Override
    public int deleteMsgFileById(Long id) {
        return msgFileMapper.deleteMsgFileById(id);
    }


    public AjaxResult checkClientFile(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            return AjaxResult.error("客户端文件id不能为空");
        }
        MsgFile condition = new MsgFile();
        condition.setUuid(uuid);
        List<MsgFile> clientFiles = msgFileMapper.selectMsgFileList(condition);

        if (CollectionUtils.isNotEmpty(clientFiles)) {
            MsgFile file = clientFiles.get(0);
            if (StringUtils.isNotEmpty(file.getPath())) {
                MessageFileVo fileVo = new MessageFileVo();
                BeanUtils.copyBeanProp(fileVo, file);
                fileVo.setPath(null);
                fileVo.setPreviewPath(null);
                fileVo.setThumbnailPath(null);
                fileVo.setHashMd5(null);
                fileVo.setHashSha1(null);
                fileVo.setHashSha256(null);
                fileVo.setUuid(uuid);
                fileVo.getParams().clear();
                if (StringUtils.isNotEmpty(file.getExtension())) {
                    fileVo.setName(file.getName() + "." + file.getExtension());
                }
                Message messageData = messageMapper.selectMessageById(file.getMsgId());
                fileVo.setMessage(messageData);
                return AjaxResult.success(fileVo);
            } else {
                return AjaxResult.error("文件处理中");
            }
        } else {
            return AjaxResult.error("客户端文件不存在");
        }
    }


    @PostConstruct
    private void init() {
        imageType.add("png");
        imageType.add("jpg");
        imageType.add("jpeg");
        imageType.add("bpm");
        imageType.add("gif");
    }

    @Autowired
    public void setSoflyImFileConfig(SoflyImFileConfig soflyImFileConfig) {
        this.soflyImFileConfig = soflyImFileConfig;
    }

    @Autowired(required = false)
    public void setFileStorageService(MinioStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Autowired
    public void setMsgFileMapper(MsgFileMapper msgFileMapper) {
        this.msgFileMapper = msgFileMapper;
    }

    @Autowired
    public void setImageFileService(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }


    @Qualifier("SoflyExecutor")
    @Autowired
    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Autowired
    public void setChannelMemberMapper(ChannelMemberMapper channelMemberMapper) {
        this.channelMemberMapper = channelMemberMapper;
    }

    @Autowired
    public void setChatEventService(ChatEventService chatEventService) {
        this.chatEventService = chatEventService;
    }


    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }
}
