package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.im.message.domain.vo.MessageFileVo;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;

/**
 * 文件上传成功事件<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-23 10:43
 */
public class MessageFileUploadedEvent extends BaseImEvent<MessageFileVo, ChatBroadcast<Long, Long, ChannelId>> {

    public MessageFileUploadedEvent() {
        setEvent(ChatEventName.MESSAGE_FILE_UPLOADED);
    }

}
