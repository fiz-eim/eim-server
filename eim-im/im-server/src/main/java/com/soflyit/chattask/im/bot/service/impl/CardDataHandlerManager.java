package com.soflyit.chattask.im.bot.service.impl;

import com.soflyit.chattask.im.bot.service.CardDataHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CardDataHandlerManager {

    private static final Map<Long, CardDataHandler> handlerMap = new HashMap();

    public CardDataHandler getHandler(Long templateId) {
        CardDataHandler handler = handlerMap.get(templateId);
        if (handler == null) {
            handler = getDefaultHandler();
        }
        return handler;
    }

    public static void registerHandler(Long templateId, CardDataHandler handler) {
        handlerMap.put(templateId, handler);
    }

    private CardDataHandler getDefaultHandler() {
        return handlerMap.get(-1L);
    }

}
