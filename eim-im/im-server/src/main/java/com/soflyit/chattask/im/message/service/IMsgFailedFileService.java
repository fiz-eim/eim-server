package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.MsgFailedFile;
import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import com.soflyit.chattask.lib.im.fs.service.FailedFileService;

import java.io.File;
import java.util.List;

/**
 * 失败文件Service接口
 *
 * @author soflyit
 * @date 2024-01-10 13:52:41
 */
public interface IMsgFailedFileService extends FailedFileService {

    public MsgFailedFile selectMsgFailedFileById(Long id);


    public List<MsgFailedFile> selectMsgFailedFileList(MsgFailedFile msgFailedFile);


    public int insertMsgFailedFile(MsgFailedFile msgFailedFile);


    public int updateMsgFailedFile(MsgFailedFile msgFailedFile);


    public int deleteMsgFailedFileByIds(Long[] ids);


    public int deleteMsgFailedFileById(Long id);


    void saveFailedFileData(File file, MsgFile msgFile, Short fileType);

}
