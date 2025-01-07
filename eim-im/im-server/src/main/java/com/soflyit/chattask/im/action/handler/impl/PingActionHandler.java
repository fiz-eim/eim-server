package com.soflyit.chattask.im.action.handler.impl;

import com.soflyit.chattask.im.action.handler.ChatActionHandler;
import com.soflyit.chattask.lib.netty.action.domain.PingActionRequest;
import com.soflyit.chattask.lib.netty.action.domain.PingActionResponse;
import com.soflyit.chattask.lib.netty.server.domain.MessageProcessResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.soflyit.chattask.lib.netty.common.ActionStatusConstant.OK;

/**
 * 心跳请求处理<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-28 15:27
 */
@Component
@Slf4j
public class PingActionHandler implements ChatActionHandler<PingActionRequest, PingActionResponse, String> {
    @Override
    public Class<PingActionRequest> getActionType() {
        return PingActionRequest.class;
    }

    @Override
    public PingActionResponse process(PingActionRequest request, MessageProcessResult<PingActionRequest, PingActionResponse, String> processResult) {

        PingActionResponse response = new PingActionResponse();
        response.setStatus(OK);
        response.setData("pong");
        if (request.getSeq() != null) {
            response.setSeqReply(request.getSeq());
        }
        return response;
    }
}
