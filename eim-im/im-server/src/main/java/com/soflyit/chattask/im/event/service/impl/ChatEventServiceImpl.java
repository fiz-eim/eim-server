package com.soflyit.chattask.im.event.service.impl;

import com.soflyit.chattask.im.event.handler.ChatEventHandler;
import com.soflyit.chattask.im.event.handler.EventHandlerManager;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.lib.netty.event.ChatEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatEventServiceImpl implements ChatEventService {


    private EventHandlerManager eventHandlerManager;

    @Override
    public void doProcessEvent(ChatEvent event) {
        ChatEventHandler eventHandler = eventHandlerManager.getHandler(event.getEvent());
        if (eventHandler == null) {
            log.error("事件处理失败，找不到对应的事件处理器：{}", event.getEvent());
            return;
        }
        eventHandler.processEvent(event);
    }

    @Autowired
    public void setEventHandlerManager(EventHandlerManager eventHandlerManager) {
        this.eventHandlerManager = eventHandlerManager;
    }
}
