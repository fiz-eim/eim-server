package com.soflyit.chattask.lib.netty.server.service;

/**
 * websocket服务回调接口<br>
 * 1. 启动后回调
 * 2. 服务停止回调
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-27 17:43
 */
public interface ChatWebSocketServerCallBack {


    void start(int port);

}
