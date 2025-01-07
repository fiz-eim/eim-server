package com.soflyit.chattask.lib.netty.action.domain;

import com.soflyit.chattask.lib.netty.action.ChatActionResponse;

import static com.soflyit.chattask.lib.netty.common.ChatActionName.PING;

/**
 * 心跳响应<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-28 15:28
 */
public class PingActionResponse extends ChatActionResponse<String> {

    public PingActionResponse() {
        super();
        super.setClientAction(PING);
    }
}
