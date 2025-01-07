package com.soflyit.chattask.im.event.service;

import com.soflyit.chattask.lib.netty.event.ChatEvent;

/**
 * 聊天事件服务
 */
public interface ChatEventService {

    void doProcessEvent(ChatEvent event);

}
