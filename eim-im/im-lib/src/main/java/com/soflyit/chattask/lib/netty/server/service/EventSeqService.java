package com.soflyit.chattask.lib.netty.server.service;

import com.soflyit.chattask.lib.netty.common.client.WebSocketClientId;
import com.soflyit.chattask.lib.netty.config.WebSocketConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件序号管理器
 */
@Component
public class EventSeqService {

    private WebSocketConfig webSocketConfig;

    public Map<WebSocketClientId, Integer> enventSeqMap = null;


    public int nextSeq(WebSocketClientId clientId) {
        Integer seq = enventSeqMap.computeIfAbsent(clientId, key -> Integer.valueOf(0));
        seq++;
        enventSeqMap.put(clientId, seq);
        return seq;
    }

    @Autowired
    public void setWebSocketConfig(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    @PostConstruct
    private void initMap() {
        enventSeqMap = new HashMap<>(webSocketConfig.getMaxClientCount());
    }

}
