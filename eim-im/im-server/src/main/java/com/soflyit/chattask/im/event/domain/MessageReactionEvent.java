package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.message.domain.vo.MessageReactionEventData;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

public class MessageReactionEvent extends BaseImEvent<MessageReactionEventData, ChatBroadcast<Long, Long, ChannelId>> {

    public MessageReactionEvent() {
        setEvent(ChatEventName.MESSAGE_REACTION);
    }
}
