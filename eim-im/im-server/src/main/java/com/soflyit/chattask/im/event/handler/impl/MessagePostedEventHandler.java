package com.soflyit.chattask.im.event.handler.impl;

import com.alibaba.fastjson.JSON;
import com.soflyit.chattask.im.common.util.MessageMetaUtil;
import com.soflyit.chattask.im.event.domain.MessagePostedEvent;
import com.soflyit.chattask.im.message.domain.vo.MessageMetadata;
import com.soflyit.chattask.im.message.domain.vo.MessageProp;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.im.message.domain.vo.MetaStatusFlag;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClientId;
import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import com.soflyit.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.soflyit.chattask.lib.netty.common.ChatEventName.MESSAGE_POSTED;

/**
 * 消息推送事件处理<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-23 20:28
 */
@Component
@Slf4j
public class MessagePostedEventHandler extends DefaultEventHandler<MessagePostedEvent> {


    @Override
    protected String eventToMessage(MessagePostedEvent event, WebSocketClientId webSocketClientId, List<WebSocketClientId> currentUserClientIds) {
        MessageVo message = event.getData();
        String messageStr = null;
        MessageMetadata messageMetadata = message.getMetaData();
        if (messageMetadata != null) {
            MessageProp messageProp = message.getProps();

            MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(messageMetadata);
            statusFlag.setReadFlag(Boolean.TRUE);
            List<Long> mentionUsers = messageMetadata.getMentionUsers();

            if ((messageProp == null || messageProp.getSystemData() == null) && (CollectionUtils.isEmpty(currentUserClientIds) || !currentUserClientIds.contains(webSocketClientId))) {
                statusFlag.setReadFlag(Boolean.FALSE);
                messageMetadata.setUnAckUserList(null);
            }

            if (mentionUsers == null) {
                mentionUsers = new ArrayList<>();
            }
            messageMetadata.setMentionUsers(null);

            WebsocketUserId<Long> websocketUserId = chatWebSocketService.getUserIdByChannelId(webSocketClientId);
            if (websocketUserId != null && websocketUserId.getUserId() != null) {
                statusFlag.setMentionMe(mentionUsers.contains(websocketUserId.getUserId()));
            }
            message.setProps(null);
            messageStr = super.eventToMessage(event, webSocketClientId, currentUserClientIds);
            messageMetadata.setMentionUsers(mentionUsers);
        }

        if (StringUtils.isEmpty(messageStr)) {
            messageStr = super.eventToMessage(event, webSocketClientId, currentUserClientIds);
        }
        return messageStr;
    }


    @Override
    protected void doFilterClientIds(List<WebSocketClientId> clientIds, MessagePostedEvent event) {

        if (soflyImConfig.getEchoPush() != null && soflyImConfig.getEchoPush()) {
            return;
        }
        String clientIdStrs = JSON.toJSONString(clientIds.stream().map(WebSocketClientId::getClientId).collect(Collectors.toList()));
        log.debug("过滤客户端，token:{}, 客户端列表：{}", event.getToken(), clientIdStrs);
        if (StringUtils.isNotBlank(event.getToken())) {
            WebSocketClientId clientId = chatWebSocketService.getClientIdByToken(event.getToken());
            if (clientId != null) {
                log.debug("过滤客户端，clientId:{}, 客户都列表：{}", clientId.getClientId(), clientIdStrs);
                clientIds.remove(clientId);
            }
        }
    }

    @Override
    public String getEvent() {
        return MESSAGE_POSTED;
    }


}
