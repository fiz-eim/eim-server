package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

import static com.soflyit.chattask.lib.netty.common.ChatEventName.MESSAGE_PINNED;

/**
 * 消息收藏事件<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-30 17:31
 */
public class MessagePinnedEvent extends BaseImEvent<MessageVo, ChatBroadcast<Long, Long, ChannelId>> {


    public MessagePinnedEvent() {
        setEvent(MESSAGE_PINNED);
    }
}
