package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.MsgUnread;
import com.soflyit.chattask.im.message.domain.param.MessageQueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息未读明细Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface MsgUnreadMapper extends BaseMapper<MsgUnread> {

    MsgUnread selectMsgUnreadById(Long id);


    List<MsgUnread> selectMsgUnreadList(MsgUnread msgUnread);


    int insertMsgUnread(MsgUnread msgUnread);


    int updateMsgUnread(MsgUnread msgUnread);


    int deleteMsgUnreadById(Long id);


    int deleteMsgUnreadByIds(Long[] ids);


    void batchInsert(List<MsgUnread> unreadList);



    List<MsgUnread> selectMentionMessageIds(MessageQueryParam message);


    List<MsgUnread> selectUnreadTopicMsg(MsgUnread condition);
}
