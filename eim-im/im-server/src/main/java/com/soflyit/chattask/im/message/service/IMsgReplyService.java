package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgReply;
import com.soflyit.chattask.im.message.domain.param.MessageAddParam;
import com.soflyit.chattask.im.message.domain.vo.MsgReplyExt;
import com.soflyit.common.core.web.domain.AjaxResult;

import java.util.List;

/**
 * 消息回复统计Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IMsgReplyService {

    MsgReply selectMsgReplyById(Long id);


    List<MsgReply> selectMsgReplyList(MsgReply msgReply);


    int insertMsgReply(MsgReply msgReply);


    int updateMsgReply(MsgReply msgReply);


    int deleteMsgReplyByIds(Long[] ids);


    int deleteMsgReplyById(Long id);


    void processReplyMessage(MessageAddParam message);


    AjaxResult getReplyInfo(Long messageId);


    AjaxResult topicMsgConfig(Long messageId, MsgReplyExt replyExt);


    AjaxResult selectTopicList(MsgReply reply);


    AjaxResult selectMentionMeTopicList(MsgReply reply);


    AjaxResult selectMentionMeList(Message condition);
}
