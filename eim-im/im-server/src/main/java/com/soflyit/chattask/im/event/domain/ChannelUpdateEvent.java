package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

/**
 * 频道创建事件
 */
public class ChannelUpdateEvent extends BaseImEvent<ChannelVo, ChatBroadcast<Long, Long, ChannelId>> {

    public ChannelUpdateEvent() {
        setEvent(ChatEventName.CHANNEL_UPDATE_EVENT);
    }
}
