package com.soflyit.chattask.im.netty.service;

import com.alibaba.fastjson.JSON;
import com.soflyit.chattask.im.action.handler.ActionHandlerManager;
import com.soflyit.chattask.im.action.handler.ChatActionHandler;
import com.soflyit.chattask.lib.netty.action.ChatActionRequest;
import com.soflyit.chattask.lib.netty.action.ChatActionResponse;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClientId;
import com.soflyit.chattask.lib.netty.server.domain.MessageProcessResult;
import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import com.soflyit.chattask.lib.netty.server.service.WebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * websocket 消息服务
 */
@Slf4j
@Component
public class ChatWebSocketService extends WebSocketService<Channel, ChannelId, String, Long> {


    private ActionHandlerManager actionHandlerManager;


    @Override
    public MessageProcessResult processMessage(WebSocketClientId channelId, String message, String token) {
        log.debug("接收到客户端消息：{}， {}", channelId, message);
        ChatActionRequest request = processActionRequest(message, JSON.parseObject(message, ChatActionRequest.class));
        WebsocketUserId websocketUserId = getUserIdByChannelId(channelId);
        request.setWebsocketUserId(websocketUserId);

        ChatActionHandler handler = actionHandlerManager.getHandler(request.getClass());
        MessageProcessResult result = new MessageProcessResult();
        request.setToken(token);
        try {
            ChatActionResponse response = handler.process(request, result);
            result.setRequestData(request);
            result.setResponseData(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.debug("客户端消息处理完成：{}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public void doConnect(WebSocketClientId clientId, WebsocketUserId<Long> token) {
        log.debug("客户端已接入：{}, token:{}", clientId.getClientId(), token);
    }

    @Override
    public void doDisconnect(WebSocketClientId channelId) {
        log.debug("客户端已断开：{}", channelId.getClientId());
    }

    @Autowired
    public void setActionHandlerManager(ActionHandlerManager actionHandlerManager) {
        this.actionHandlerManager = actionHandlerManager;
    }


    private ChatActionRequest processActionRequest(String message, ChatActionRequest chatActionRequest) {

        Class<ChatActionRequest> clazz = actionHandlerManager.getActionRequest(chatActionRequest.getAction());
        if (clazz != null) {
            return JSON.parseObject(message, clazz);
        }
        return chatActionRequest;
    }

}
