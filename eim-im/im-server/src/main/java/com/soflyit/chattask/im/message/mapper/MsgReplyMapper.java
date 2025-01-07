package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.MsgReply;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息回复统计Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface MsgReplyMapper extends BaseMapper<MsgReply> {

    MsgReply selectMsgReplyById(Long id);


    List<MsgReply> selectMsgReplyList(MsgReply msgReply);


    int insertMsgReply(MsgReply msgReply);


    int updateMsgReply(MsgReply msgReply);


    int deleteMsgReplyById(Long id);


    int deleteMsgReplyByIds(Long[] ids);
}
