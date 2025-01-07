package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

/**
 * 频道创建事件
 */
public class ChannelMemberAddEvent extends BaseImEvent<ChannelVo, ChatBroadcast<Long, Long, ChannelId>> {

    public ChannelMemberAddEvent() {
        setEvent(ChatEventName.CHANNEL_MEMBER_ADD_EVENT);
    }
}
