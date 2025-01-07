package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.MsgUnread;
import com.soflyit.chattask.im.message.domain.vo.sysdata.RecallMessageData;

import java.util.List;

/**
 * 消息未读明细Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IMsgUnreadService {

    MsgUnread selectMsgUnreadById(Long id);


    List<MsgUnread> selectMsgUnreadList(MsgUnread msgUnread);


    int insertMsgUnread(MsgUnread msgUnread);


    int updateMsgUnread(MsgUnread msgUnread);


    int deleteMsgUnreadByIds(Long[] ids);


    int deleteMsgUnreadById(Long id);


    void processRecallMessageUnread(RecallMessageData messageData);
}
