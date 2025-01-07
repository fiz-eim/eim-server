package com.soflyit.chattask.im.action.handler;

import com.soflyit.chattask.lib.netty.action.ChatActionRequest;
import com.soflyit.chattask.lib.netty.action.ChatActionResponse;
import com.soflyit.chattask.lib.netty.server.domain.MessageProcessResult;

/**
 * 客户端请求处理器
 *
 * @param <REQ>  请求类型
 * @param <RESP> 响应类型
 * @param <D>    业务数据类型
 */
public interface ChatActionHandler<REQ extends ChatActionRequest, RESP extends ChatActionResponse, D> {



    Class<REQ> getActionType();


    RESP process(REQ request, MessageProcessResult<REQ, RESP, D> processResult);


}
