package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgAck;
import com.soflyit.common.core.web.domain.AjaxResult;

import java.util.List;

/**
 * 消息确认Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IMsgAckService {

    MsgAck selectMsgAckById(Long id);


    List<MsgAck> selectMsgAckList(MsgAck msgAck);


    int insertMsgAck(MsgAck msgAck);


    int updateMsgAck(MsgAck msgAck);


    int deleteMsgAckByIds(Long[] ids);


    int deleteMsgAckById(Long id);


    AjaxResult ackMessage(Message message, Long userId);


    AjaxResult cancelAckMessage(Message message, Long userId);
}
