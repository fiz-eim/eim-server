package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

/**
 * 频道创建事件
 */
public class ChannelMemberDeleteEvent extends BaseImEvent<ChannelMember, ChatBroadcast<Long, Long, ChannelId>> {

    public ChannelMemberDeleteEvent() {
        setEvent(ChatEventName.CHANNEL_MEMBER_DELETE_EVENT);
    }
}
