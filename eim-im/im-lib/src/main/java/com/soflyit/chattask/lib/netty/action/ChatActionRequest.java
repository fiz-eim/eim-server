package com.soflyit.chattask.lib.netty.action;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import lombok.Data;

import java.io.Serializable;

/**
 * websocket 请求消息
 *
 * @param <T>   数据类型
 * @param <UID> 用户Id类型
 */
@Data
public class ChatActionRequest<T extends Serializable, UID extends Serializable> implements ActionRequest {


    @JSONField(deserialize = false, serialize = false)
    @JsonIgnore
    private String token;

    private WebsocketUserId<UID> websocketUserId;


    private String action;


    private T data;


    private Integer seq;

    @Override
    public String getActionName() {
        return getAction();
    }

}
