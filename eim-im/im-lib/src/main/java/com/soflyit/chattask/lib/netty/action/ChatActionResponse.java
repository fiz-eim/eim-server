package com.soflyit.chattask.lib.netty.action;

import lombok.Data;

import java.io.Serializable;

/**
 * websocket 响应信息
 *
 * @param <T>
 */
@Data
public class ChatActionResponse<T extends Serializable> {

    private String clientAction;


    private String status;


    private Integer seqReply;


    private T data;

    private String message;

}
