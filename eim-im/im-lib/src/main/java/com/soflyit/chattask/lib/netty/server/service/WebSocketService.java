package com.soflyit.chattask.lib.netty.server.service;

import com.soflyit.chattask.lib.netty.cache.WebSocketClientCache;
import com.soflyit.chattask.lib.netty.common.client.ClientPlatform;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClient;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClientId;
import com.soflyit.chattask.lib.netty.server.domain.MessageProcessResult;
import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import com.soflyit.chattask.lib.netty.server.exception.ExceptionCode;
import com.soflyit.chattask.lib.netty.server.exception.WebSocketException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * websocket 服务
 */
@Component
@Slf4j
public abstract class WebSocketService<C, CID extends Serializable, M, UID extends Serializable> {


    private ImBusinessService imBusinessService;



    private WebSocketClientCache<CID, UID, C> cache;


    public abstract MessageProcessResult processMessage(WebSocketClientId clientId, M message, String token);


    public abstract void doConnect(WebSocketClientId clientId, WebsocketUserId<UID> userId);


    public abstract void doDisconnect(WebSocketClientId clientId);


    public void sendMessageToClient(WebSocketClientId clientId, M message) {
        if (cache == null) {
            throw new WebSocketException(ExceptionCode.SEND_FAILED_CACHE_NOT_EXIST);
        }
        WebSocketClient client = cache.getClient(clientId);
        if (client == null) {
            throw new WebSocketException(ExceptionCode.SEND_FAILED_CHANNEL_NOT_EXIST);
        }
        client.sendMessage(message);
    }


    public void sendMessageToUser(WebsocketUserId websocketUserId, M message) {
        if (cache == null) {
            throw new WebSocketException(ExceptionCode.SEND_FAILED_CACHE_NOT_EXIST);
        }
        List<WebSocketClient> clients = cache.getClientsByUserId(websocketUserId);
        if (CollectionUtils.isEmpty(clients)) {
            throw new WebSocketException(ExceptionCode.SEND_FAILED_CHANNEL_NOT_EXIST);
        }
        clients.forEach(client -> {
            if (client == null) {
                log.warn("发送消息失败：通道为空");
            } else {
                client.sendMessage(message);
            }
        });
    }


    public void sendMessageToUser(WebsocketUserId websocketUserId, ClientPlatform platform, M message) {
        if (cache == null) {
            throw new WebSocketException(ExceptionCode.SEND_FAILED_CACHE_NOT_EXIST);
        }
        List<WebSocketClient> clients = cache.getClientsByUserId(websocketUserId, platform);
        if (CollectionUtils.isEmpty(clients)) {
            throw new WebSocketException(ExceptionCode.SEND_FAILED_CACHE_NOT_EXIST);
        } else {
            clients.forEach(client -> {
                client.sendMessage(message);
            });
        }
    }


    public void doCacheClient(WebSocketClient client, WebsocketUserId userId, String token) {
        cache.addClient(client, userId, token);
    }


    public void removeChannel(WebSocketClient channel) {
        cache.removeClient(channel);
    }



    public WebsocketUserId<Long> validateToken(String token) {
        return imBusinessService.validateToken(token);
    }

    @Autowired
    public void setCache(WebSocketClientCache cache) {
        this.cache = cache;
    }


    public WebSocketClientId getClientId(CID id) {
        return cache.getClientId(id);
    }


    public WebsocketUserId getUserId(UID id) {
        return cache.getUserId(id);
    }


    public WebSocketClient getClientByReal(C channel, CID id) {
        WebSocketClientId clientId = getClientId(id);
        return cache.getClientByReal(channel, clientId);
    }

    public List<WebSocketClientId> getClientIdByUserId(UID userId) {
        WebsocketUserId websocketUserId = cache.getUserId(userId);
        if (websocketUserId == null) {
            return null;
        }

        return cache.getClientIdsByUserId(websocketUserId);
    }


    public WebSocketClientId getClientIdByToken(String token) {
        return cache.getClientIdByToken(token);
    }

    @Autowired
    public void setImBusinessService(ImBusinessService imBusinessService) {
        this.imBusinessService = imBusinessService;
    }


    public WebsocketUserId getUserIdByChannelId(WebSocketClientId channelId) {
        return cache.getUserIdBChannelId(channelId);
    }
}
