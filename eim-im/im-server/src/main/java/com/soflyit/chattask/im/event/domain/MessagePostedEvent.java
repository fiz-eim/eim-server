package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

/**
 * 消息事件<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-23 20:04
 */
public class MessagePostedEvent extends BaseImEvent<MessageVo, ChatBroadcast<Long, Long, ChannelId>> {

    public MessagePostedEvent() {
        setEvent(ChatEventName.MESSAGE_POSTED);
    }
}
