package com.soflyit.chattask.lib.netty.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatEvent<D extends Serializable, B extends ChatBroadcast> implements Serializable {

    private int seq;

    private String event;

    private D data;

    private B broadcast;

    private String token;

    private Long userId;

}
