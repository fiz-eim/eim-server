package com.soflyit.chattask.im.action.handler;

import com.soflyit.chattask.lib.netty.action.ChatActionRequest;
import com.soflyit.chattask.lib.netty.action.ChatActionResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ActionHandlerManager<REQ extends ChatActionRequest> implements ApplicationContextAware {

    private ApplicationContext ctx;


    private final Map<Class<REQ>, ChatActionHandler> handlerMap = new HashMap<>();


    private final Map<String, Class<REQ>> actionNameRequestClassMap = new HashMap<>();


    public <RESP extends ChatActionResponse, REQ extends ChatActionRequest, D> ChatActionHandler<REQ, RESP, D> getHandler(Class<REQ> requestClass) {
        return handlerMap.get(requestClass);
    }


    public Class<REQ> getActionRequest(String action) {
        return actionNameRequestClassMap.get(action);
    }


    @PostConstruct
    private void registerHandlers() {
        try {
            Map<String, ChatActionHandler> handlerMapper = ctx.getBeansOfType(ChatActionHandler.class);
            if (MapUtils.isNotEmpty(handlerMapper)) {
                handlerMapper.forEach((key, handler) -> {
                    registerHandler(handler);
                });
            }

        } catch (BeansException e) {
            log.warn("请求处理器注册失败" + e.getMessage(), e);
        }
    }


    private void registerHandler(ChatActionHandler handler) {
        Class<REQ> clazz = handler.getActionType();
        try {
            String actionName = clazz.newInstance().getActionName();
            actionNameRequestClassMap.put(actionName, clazz);
        } catch (InstantiationException e) {
            log.warn(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.warn(e.getMessage(), e);
        }
        if (handlerMap.containsKey(clazz)) {
            log.warn("注册失败：请求类型{}，已注册处理器{}", clazz.getName(), handler.getClass().getName());
        } else {
            handlerMap.put(clazz, handler);
            log.debug("注册成功：请求类型{}，已注册处理器{}", clazz.getName(), handler.getClass().getName());
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
