package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

/**
 * 频道创建事件
 */
public class ChannelCreatedEvent extends BaseImEvent<ChatChannel, ChatBroadcast<Long, Long, ChannelId>> {
    public ChannelCreatedEvent() {
        super();
        setEvent(ChatEventName.CHANNEL_CREATE_EVENT);
    }
}
