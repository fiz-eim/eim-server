package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.message.domain.vo.MsgTopVo;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

/**
 * 读取消息事件<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-06 09:47
 */
public class MessageTopEvent extends BaseImEvent<MsgTopVo, ChatBroadcast<Long, Long, ChannelId>> {

    public MessageTopEvent() {
        setEvent(ChatEventName.MESSAGE_TOP);
    }
}
