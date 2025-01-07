package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息文件Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:18
 */
@Repository
public interface MsgFileMapper extends BaseMapper<MsgFile> {

    public MsgFile selectMsgFileById(Long id);


    public List<MsgFile> selectMsgFileList(MsgFile msgFile);


    public int insertMsgFile(MsgFile msgFile);


    public int updateMsgFile(MsgFile msgFile);


    public int deleteMsgFileById(Long id);


    public int deleteMsgFileByIds(Long[] ids);


    void batchInsert(List<MsgFile> msgFiles);
}
