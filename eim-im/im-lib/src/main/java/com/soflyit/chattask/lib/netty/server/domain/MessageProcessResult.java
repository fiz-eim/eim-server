package com.soflyit.chattask.lib.netty.server.domain;

import com.soflyit.chattask.lib.netty.action.ChatActionRequest;
import com.soflyit.chattask.lib.netty.action.ChatActionResponse;
import lombok.Data;

/**
 * 请求处理结果
 *
 * @param <REQ> 请求
 * @param <D>   结果数据类型
 */
@Data
public class MessageProcessResult<REQ extends ChatActionRequest, RESP extends ChatActionResponse, D> {


    private REQ requestData;


    private RESP responseData;


    private D data;
}
