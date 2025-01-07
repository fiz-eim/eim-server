package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.MsgFailedFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 失败文件Mapper接口
 *
 * @author soflyit
 * @date 2024-01-10 13:52:41
 */
@Repository
public interface MsgFailedFileMapper extends BaseMapper<MsgFailedFile> {

    public MsgFailedFile selectMsgFailedFileById(Long id);


    public List<MsgFailedFile> selectMsgFailedFileList(MsgFailedFile msgFailedFile);


    public int insertMsgFailedFile(MsgFailedFile msgFailedFile);


    public int updateMsgFailedFile(MsgFailedFile msgFailedFile);


    public int deleteMsgFailedFileById(Long id);


    public int deleteMsgFailedFileByIds(Long[] ids);
}
