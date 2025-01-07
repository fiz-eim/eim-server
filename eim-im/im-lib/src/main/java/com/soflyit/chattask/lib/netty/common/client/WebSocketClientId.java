package com.soflyit.chattask.lib.netty.common.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户端Id
 *
 * @param <T> 通道Id
 */
@AllArgsConstructor
@Data
public class WebSocketClientId<T extends Serializable> {
    private T clientId;


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WebSocketClientId)) {
            return false;
        }
        WebSocketClientId wClientId = (WebSocketClientId) obj;
        if (wClientId == null) {
            return false;
        }
        if (wClientId == null && wClientId.getClientId() == null) {
            return true;
        }
        return clientId != null && clientId.equals(wClientId.getClientId());
    }


    public int hashCode() {
        if (clientId == null) {
            return -1;
        }
        return clientId.hashCode();
    }

}
