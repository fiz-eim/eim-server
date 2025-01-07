package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

public class TopicCreateEvent extends BaseImEvent<Long, ChatBroadcast<Long, Long, ChannelId>> {

    public TopicCreateEvent() {
        setEvent(ChatEventName.TOPIC_CREATE);
    }
}
