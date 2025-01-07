package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

public class MessageTextTagEvent extends BaseImEvent<MessageVo, ChatBroadcast<Long, Long, ChannelId>> {

    public MessageTextTagEvent() {
        setEvent(ChatEventName.MESSAGE_TEXT_TAG);
    }

}

