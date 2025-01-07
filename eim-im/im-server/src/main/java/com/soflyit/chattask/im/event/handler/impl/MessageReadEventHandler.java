package com.soflyit.chattask.im.event.handler.impl;

import com.soflyit.chattask.im.config.SoflyImConfig;
import com.soflyit.chattask.im.event.domain.MessageReadEvent;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClientId;
import com.soflyit.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.soflyit.chattask.lib.netty.common.ChatEventName.MESSAGE_READ;

/**
 * 消息读取事件处理器<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-06 09:51
 */
@Slf4j
@Component
public class MessageReadEventHandler extends DefaultEventHandler<MessageReadEvent> {

    private SoflyImConfig soflyImConfig;


    @Override
    protected void doFilterClientIds(List<WebSocketClientId> clientIds, MessageReadEvent event) {

        if (soflyImConfig.getEchoPush() != null && soflyImConfig.getEchoPush()) {
            return;
        }

        if (StringUtils.isNotBlank(event.getToken())) {
            WebSocketClientId clientId = chatWebSocketService.getClientIdByToken(event.getToken());
            if (clientId != null) {
                clientIds.remove(clientId);
            }
        }
    }

    @Override
    public String getEvent() {
        return MESSAGE_READ;
    }

    @Autowired
    public void setSoflyImConfig(SoflyImConfig soflyImConfig) {
        this.soflyImConfig = soflyImConfig;
    }
}
