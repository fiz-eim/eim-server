package com.soflyit.chattask.lib.netty.cache;

import com.soflyit.chattask.lib.netty.common.client.ClientPlatform;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClient;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClientId;
import com.soflyit.chattask.lib.netty.config.WebSocketConfig;
import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import com.soflyit.chattask.lib.netty.server.exception.ExceptionCode;
import com.soflyit.chattask.lib.netty.server.exception.WebSocketException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 服务缓存:用于认证成功后，缓存通道与用户的对应关系<br/>
 * <p>
 * <li>用户id与通道Id关系、</li>
 * <li>通道Id与用户Id关系、</li>
 * <li>原始用户Id与通用用户Id关系、</li>
 * <li>原始通道Id与通用通道Id关系</li>
 *
 * @param <CID> 通道Id类型
 * @param <UID> 用户Id类型
 * @param <C>   通道类型
 */
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
@Slf4j
public class WebSocketClientCache<CID extends Serializable, UID extends Serializable, C> {

    private WebSocketConfig webSocketConfig;


    private Map<WebSocketClientId, WebSocketClient> clientMap = null;


    private Map<WebSocketClientId, WebsocketUserId> clientIdUserIdMap = null;


    private Map<WebsocketUserId, List<WebSocketClientId>> userIdClientMap = null;


    private Map<CID, WebSocketClientId> cidMap = null;


    private Map<UID, WebsocketUserId> uidMap = null;


    private Map<C, WebSocketClient> clientRealMap = null;

    private Map<String, WebSocketClientId> tokenClientIdMap = null;



    public void addClient(WebSocketClient client, WebsocketUserId userId, String token) {
        if (client == null) {
            log.warn("client 为空，不需要缓存");
            return;
        }

        if (clientMap.size() > webSocketConfig.getMaxClientCount()) {
            throw new WebSocketException(ExceptionCode.MAX_CHANNEL_COUNT);
        }

        WebSocketClientId clientId = client.getClientId();
        if (clientId == null) {
            log.warn("clientId 为空，不需要缓存");
            return;
        }

        clientMap.put(clientId, client);
        clientIdUserIdMap.put(clientId, userId);
        userIdClientMap.computeIfAbsent(userId, key -> new ArrayList<>()).add(clientId);
        tokenClientIdMap.put(token, clientId);
    }


    public void removeClient(WebSocketClient client) {
        if (client == null) {
            return;
        }
        WebSocketClientId clientId = client.getClientId();
        if (clientId == null) {
            return;
        }
        cidMap.remove(clientId.getClientId());
        clientMap.remove(clientId);

        clientRealMap.remove(client.getClient());

        WebsocketUserId userId = clientIdUserIdMap.remove(clientId);
        if (userId != null) {
            log.debug("关闭客户端：{}", userId.getUserId());
            List<WebSocketClientId> clientIds = userIdClientMap.computeIfAbsent(userId, key -> new ArrayList<>());
            clientIds.remove(clientId);
            if (clientIds.isEmpty()) {
                userIdClientMap.remove(userId);
                uidMap.remove(userId.getUserId());
            }

        }
    }


    public void removeClientByUserId(WebsocketUserId userId) {
        if (userId == null) {
            return;
        }

        List<WebSocketClientId> clientIds = userIdClientMap.remove(userId);
        if (CollectionUtils.isNotEmpty(clientIds)) {
            clientIds.forEach(clientId -> {
                clientMap.remove(clientId);
                clientIdUserIdMap.remove(clientId);
            });
            clientIds.clear();
        }
    }


    public WebSocketClient getClient(WebSocketClientId clientId) {
        return clientMap.get(clientId);
    }


    public Collection<WebSocketClient> getClients() {
        return clientMap.values();
    }


    public List<WebSocketClient> getClientsByUserId(WebsocketUserId websocketUserId) {
        return null;
    }


    public List<WebSocketClient> getClientsByUserId(WebsocketUserId websocketUserId, ClientPlatform platform) {
        return null;
    }

    @Autowired
    public void setWebSocketConfig(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    @PostConstruct
    private void init() {
        this.clientMap = new ConcurrentHashMap<>(webSocketConfig.getMaxClientCount());
        this.clientIdUserIdMap = new ConcurrentHashMap<>(webSocketConfig.getMaxClientCount());
        this.userIdClientMap = new ConcurrentHashMap<>(webSocketConfig.getMaxClientCount());


        this.cidMap = new ConcurrentHashMap<>(webSocketConfig.getMaxClientCount());
        this.uidMap = new ConcurrentHashMap<>(webSocketConfig.getMaxClientCount());
        this.clientRealMap = new ConcurrentHashMap<>(webSocketConfig.getMaxClientCount());
        this.tokenClientIdMap = new ConcurrentHashMap<>(webSocketConfig.getMaxClientCount());

    }

    @PreDestroy
    private void destroy() {
        this.clientMap.clear();
        this.clientIdUserIdMap.clear();
        this.userIdClientMap.forEach((key, val) -> {
            if (CollectionUtils.isNotEmpty(val)) {
                val.clear();
            }
        });
        this.clientIdUserIdMap.clear();

        this.cidMap.clear();
        this.uidMap.clear();
        this.clientRealMap.clear();
        this.tokenClientIdMap.clear();
    }


    public WebSocketClientId getClientId(CID id) {
        if (cidMap == null) {
            return null;
        }
        return cidMap.computeIfAbsent(id, key -> new WebSocketClientId(key));
    }

    public WebsocketUserId getUserId(UID id) {
        if (uidMap == null || id == null) {
            return null;
        }
        return uidMap.computeIfAbsent(id, key -> new WebsocketUserId(key));
    }

    public WebSocketClient getClientByReal(C channel, WebSocketClientId clientId) {
        if (clientRealMap == null) {
            return null;
        }
        return clientRealMap.computeIfAbsent(channel, key -> new WebSocketClient(key, clientId));
    }


    public List<WebSocketClientId> getClientIdsByUserId(WebsocketUserId websocketUserId) {

        List<WebSocketClientId> result = new ArrayList<>();

        if (MapUtils.isNotEmpty(userIdClientMap)) {
            result = userIdClientMap.get(websocketUserId);
        }

        return result;
    }

    public WebSocketClientId getClientIdByToken(String token) {
        return tokenClientIdMap.get(token);
    }


    public WebsocketUserId getUserIdBChannelId(WebSocketClientId clientId) {
        return clientIdUserIdMap.get(clientId);

    }
}
