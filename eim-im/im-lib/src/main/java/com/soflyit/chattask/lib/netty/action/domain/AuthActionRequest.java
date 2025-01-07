package com.soflyit.chattask.lib.netty.action.domain;

import com.soflyit.chattask.lib.netty.action.ChatActionRequest;
import com.soflyit.chattask.lib.netty.common.ChatActionName;

import java.util.HashMap;

/**
 * 客户端认证请求
 */
public class AuthActionRequest extends ChatActionRequest<HashMap<String, String>, Long> {

    @Override
    public String getActionName() {
        return ChatActionName.AUTH_REQ;
    }
}
