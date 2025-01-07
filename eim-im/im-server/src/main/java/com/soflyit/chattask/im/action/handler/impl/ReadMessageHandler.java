package com.soflyit.chattask.im.action.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soflyit.chattask.im.action.handler.ChatActionHandler;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.service.IMessageService;
import com.soflyit.chattask.lib.netty.action.domain.ReadMessageActionRequest;
import com.soflyit.chattask.lib.netty.action.domain.ReadMessageActionResponse;
import com.soflyit.chattask.lib.netty.server.domain.MessageProcessResult;
import com.soflyit.common.core.web.domain.AjaxResult;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.soflyit.chattask.lib.netty.common.ActionStatusConstant.FAIL;
import static com.soflyit.chattask.lib.netty.common.ActionStatusConstant.OK;

/**
 * 阅读消息<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-05 13:54
 */
@Component
public class ReadMessageHandler implements ChatActionHandler<ReadMessageActionRequest, ReadMessageActionResponse, JSONObject> {

    private IMessageService messageService;

    @Override
    public Class<ReadMessageActionRequest> getActionType() {
        return ReadMessageActionRequest.class;
    }

    @Override
    public ReadMessageActionResponse process(ReadMessageActionRequest request, MessageProcessResult<ReadMessageActionRequest,
            ReadMessageActionResponse, JSONObject> processResult) {
        ReadMessageActionResponse response = new ReadMessageActionResponse();
        response.setStatus(FAIL);
        JSONObject data = request.getData();
        if (data == null) {
            String msg = "消息读取失败:消息不存在";
            response.setMessage(msg);
            return response;
        }
        Message message = JSON.parseObject(JSON.toJSONString(data), Message.class);
        if (message.getId() == null) {
            String msg = "读取消息失败，消息Id不能为空";
            response.setMessage(msg);
            return response;
        }

        AjaxResult result = messageService.readMessage(message, request);
        if (result.getCode() == HttpStatus.SC_OK) {
            response.setStatus(OK);
            response.setData(request.getData());
        }
        if (request.getSeq() != null) {
            response.setSeqReply(request.getSeq());
        }
        return response;
    }

    @Autowired
    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }
}
