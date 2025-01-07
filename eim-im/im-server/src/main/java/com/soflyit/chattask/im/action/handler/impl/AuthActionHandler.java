package com.soflyit.chattask.im.action.handler.impl;


import com.soflyit.chattask.im.action.handler.ChatActionHandler;
import com.soflyit.chattask.lib.netty.action.domain.AuthActionRequest;
import com.soflyit.chattask.lib.netty.action.domain.AuthActionResponse;
import com.soflyit.chattask.lib.netty.action.domain.AuthActionResult;
import com.soflyit.chattask.lib.netty.server.domain.MessageProcessResult;
import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import com.soflyit.chattask.lib.netty.server.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.soflyit.chattask.lib.netty.common.ActionStatusConstant.FAIL;
import static com.soflyit.chattask.lib.netty.common.ActionStatusConstant.OK;

/**
 * 认证处理器
 */
@Component
public class AuthActionHandler implements ChatActionHandler<AuthActionRequest, AuthActionResponse, AuthActionResult> {

    private WebSocketService webSocketService;

    @Override
    public Class<AuthActionRequest> getActionType() {
        return AuthActionRequest.class;
    }


    @Override
    public AuthActionResponse process(AuthActionRequest request, MessageProcessResult<AuthActionRequest, AuthActionResponse, AuthActionResult> processResult) {

        Map<String, String> requestData = request.getData();
        String token = requestData.get("token");

        AuthActionResponse response = new AuthActionResponse();
        response.setSeqReply(request.getSeq());
        WebsocketUserId websocketUserId = webSocketService.validateToken(token);

        AuthActionResult result = new AuthActionResult();
        result.setToken(token);
        result.setWebsocketUserId(websocketUserId);
        processResult.setData(result);
        if (websocketUserId != null) {
            response.setStatus(OK);
        } else {
            response.setStatus(FAIL);
            String errorMsg = "登录失败，token已过期";
            response.setMessage(errorMsg);
        }
        return response;
    }


    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }
}
