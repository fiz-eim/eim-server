package com.soflyit.chattask.im.event.handler.impl;

import com.alibaba.fastjson.JSON;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.im.event.domain.ChannelCreatedEvent;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClientId;
import com.soflyit.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;
import static com.soflyit.chattask.lib.netty.common.ChatEventName.CHANNEL_CREATE_EVENT;

/**
 * 频道<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-24 19:10
 */
@Slf4j
@Component
public class ChannelCreateEventHandler extends DefaultEventHandler<ChannelCreatedEvent> {

    protected String eventToMessage(ChannelCreatedEvent event, WebSocketClientId webSocketClientId, List<WebSocketClientId> currentUserClientIds) {
        ChatChannel channel = event.getData();
        String channelName = channel.getName();
        if (channel instanceof ChannelVo) {
            if (CollectionUtils.isNotEmpty(currentUserClientIds) && currentUserClientIds.contains(webSocketClientId)) {
                Long currentUserId = event.getUserId();
                Long userId = getUserIdFromClientId(webSocketClientId);
                String directChannelName = ((ChannelVo) channel).getDirectChannelName();

                if (userId != null && currentUserId != null && currentUserId.equals(userId) && StringUtils.isNotEmpty(directChannelName)) {
                    channel.setName(directChannelName);
                }
            }
        }
        String data = JSON.toJSONString(event, WriteMapNullValue);
        channel.setName(channelName);
        return data;
    }


    @Override
    public String getEvent() {
        return CHANNEL_CREATE_EVENT;
    }

}
