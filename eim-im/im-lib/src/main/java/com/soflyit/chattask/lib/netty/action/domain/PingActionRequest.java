package com.soflyit.chattask.lib.netty.action.domain;

import com.soflyit.chattask.lib.netty.action.ChatActionRequest;
import com.soflyit.chattask.lib.netty.common.ChatActionName;

/**
 * 心跳请求<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-28 15:27
 */
public class PingActionRequest extends ChatActionRequest<String, Long> {
    @Override
    public String getActionName() {
        return ChatActionName.PING;
    }
}
