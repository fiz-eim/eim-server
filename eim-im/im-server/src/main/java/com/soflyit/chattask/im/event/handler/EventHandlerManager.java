package com.soflyit.chattask.im.event.handler;

import com.soflyit.chattask.im.event.handler.impl.DefaultEventHandler;
import com.soflyit.common.core.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件处理器管理服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-17 09:59
 */
@Component
@Slf4j
public class EventHandlerManager {


    public static final String DEFAULT_EVENT = "default";

    private final Map<String, Class<ChatEventHandler>> eventHandlerMap = new HashMap<>();


    public ChatEventHandler getHandler(String eventName) {
        return getHandler(eventName, null);
    }


    public ChatEventHandler getHandler(String eventName, Class<ChatEventHandler> clazz) {
        Class<ChatEventHandler> handlerClass = eventHandlerMap.get(eventName);
        if (handlerClass == null) {
            handlerClass = clazz;
        }
        if (handlerClass == null) {
            handlerClass = eventHandlerMap.get(DEFAULT_EVENT);
        }
        if (handlerClass == null) {
            return null;
        }


        try {
            if (handlerClass.equals(DefaultEventHandler.class)) {
                return SpringUtils.getBean("defaultEventHandler");
            }
            return SpringUtils.getBean(handlerClass);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }


    public void registerEventHandlerClass(String name, Class clazz) {
        if (ChatEventHandler.class.isAssignableFrom(clazz)) {
            eventHandlerMap.put(name, clazz);
            log.debug("事件处理器注册成功：{} - {}", name, clazz.getName());
        } else {
            log.warn("事件处理器注册失败：{} - {}", name, clazz.getName());
        }
    }
}
