package com.soflyit.chattask.lib.netty.server.service;

import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;

import java.io.Serializable;

/**
 * Im 业务接口，用于websocket调用业务服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-05 14:03
 */
public interface ImBusinessService<UID extends Serializable> {


    WebsocketUserId<UID> validateToken(String token);


}
