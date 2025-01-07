package com.soflyit.chattask.lib.netty.action.domain;

import com.soflyit.chattask.lib.netty.action.ChatActionResponse;

import java.util.HashMap;

import static com.soflyit.chattask.lib.netty.common.ChatActionName.AUTH_REQ;

/**
 * 认证处理请求
 */
public class AuthActionResponse extends ChatActionResponse<HashMap<String, String>> {

    public AuthActionResponse() {
        super();
        super.setClientAction(AUTH_REQ);
    }
}
