package com.soflyit.chattask.lib.netty.common.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户端
 *
 * @param <C>   通道类型
 * @param <CID> 通道Id类型
 * @param <M>   消息类型
 */
@AllArgsConstructor
@Data
public class WebSocketClient<C, CID extends Serializable, M> {
    private C client;

    private WebSocketClientId<CID> clientId;


    public void sendMessage(M message) {
        throw new UnsupportedOperationException("通用客户端不支持消息发送");
    }
}
