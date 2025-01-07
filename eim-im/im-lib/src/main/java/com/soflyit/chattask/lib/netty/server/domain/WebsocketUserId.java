package com.soflyit.chattask.lib.netty.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户Id封装
 *
 * @param <T>
 */
@AllArgsConstructor
@Data
public class WebsocketUserId<T extends Serializable> implements Serializable {
    private T userId;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WebsocketUserId)) {
            return Boolean.FALSE;
        }
        WebsocketUserId wUserId = (WebsocketUserId) obj;

        if (wUserId == null) {
            return false;
        }
        if (userId == null && wUserId.getUserId() == null) {
            return true;
        }
        return userId != null && userId.equals(wUserId.getUserId());
    }


    @Override
    public int hashCode() {
        if (userId == null) {
            return -1;
        }
        return userId.hashCode();
    }

}
