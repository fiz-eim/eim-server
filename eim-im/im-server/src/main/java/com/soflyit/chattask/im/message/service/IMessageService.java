package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.param.*;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.lib.netty.action.domain.ReadMessageActionRequest;
import com.soflyit.common.core.web.domain.AjaxResult;

import java.util.List;

/**
 * 消息Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IMessageService {

    Message selectMessageById(Long id);


    List<Message> selectMessageList(Message message);


    int insertMessage(Message message);


    int updateMessage(Message message);


    int deleteMessageByIds(Long[] ids);


    AjaxResult deleteMessageById(Long id);


    AjaxResult<List<MessageVo>> selectChannelMessageList(MessageQueryParam message);


    AjaxResult<List<MessageVo>> selectMessageList(MessageQueryParam message);


    AjaxResult<List<Long>> selectMentionMessageIdList(MessageMentionQueryParam message);


    AjaxResult<List<MessageVo>> selectUserChannelMessageList(UserMessageQueryParam param);


    AjaxResult<MessageVo> createMessage(MessageAddParam message);


    void generateSystemMessage(SystemMessage message, Long messageId);


    AjaxResult pinnedMessage(Long msgId);

    AjaxResult unpinnedMessage(Long msgId);


    AjaxResult selectThreadMessages(Long msgId, ThreadQueryParam threadQueryParam);


    AjaxResult readMessage(Message message, ReadMessageActionRequest request);


    AjaxResult ackMessage(Long msgId);


    AjaxResult cancelAckMessage(Long msgId);


    AjaxResult createTopic(Long msgId);

    AjaxResult markTop(Long msgId);

    AjaxResult cancelTop(Long msgTopId);


    AjaxResult closeTop(Long msgId);

    AjaxResult markTag(Long msgId);

    AjaxResult unmarkTag(Long msgId);


    AjaxResult getPinnedMessages(Message message);

}
