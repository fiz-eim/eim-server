package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import com.soflyit.common.core.web.domain.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 消息文件Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:18
 */
public interface IMsgFileService {

    public MsgFile selectMsgFileById(Long id);


    public List<MsgFile> selectMsgFileList(MsgFile msgFile);


    public int insertMsgFile(MsgFile msgFile);


    public int updateMsgFile(MsgFile msgFile);


    public int deleteMsgFileByIds(Long[] ids);


    public int deleteMsgFileById(Long id);



    AjaxResult<MsgFile> uploadFile(MultipartFile msgFile, Long channelId, Long messageId, String uuid);


    AjaxResult deleteMsgFile(Long id);


    void downloadFile(HttpServletResponse response, Long id);


    void previewFile(HttpServletResponse response, Long id);


    void thumbnailFile(HttpServletResponse response, Long id);


    AjaxResult getFileInfo(Long id);


    AjaxResult<List<MsgFile>> selectNormalFile(MsgFile msgFile);

    AjaxResult<List<MsgFile>> selectMediaFile(MsgFile msgFile);


    AjaxResult checkClientFile(String uuid);
}
