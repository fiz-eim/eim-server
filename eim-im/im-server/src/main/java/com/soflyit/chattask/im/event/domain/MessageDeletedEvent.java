package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

/**
 * 消息删除事件<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-30 14:33
 */
public class MessageDeletedEvent extends BaseImEvent<MessageVo, ChatBroadcast<Long, Long, ChannelId>> {

    public MessageDeletedEvent() {
        setEvent(ChatEventName.MESSAGE_DELETED);
    }
}
