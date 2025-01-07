package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.channel.domain.vo.ChannelOwnerChangeVo;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

public class ChannelOwnerChangeEvent extends BaseImEvent<ChannelOwnerChangeVo, ChatBroadcast<Long, Long, ChannelId>> {

    public ChannelOwnerChangeEvent() {
        setEvent(ChatEventName.CHANNEL_OWNER_CHANGE_EVENT);
    }


}
