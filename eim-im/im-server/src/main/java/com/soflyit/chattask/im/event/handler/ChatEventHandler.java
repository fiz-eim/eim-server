package com.soflyit.chattask.im.event.handler;

import com.soflyit.chattask.lib.netty.event.ChatEvent;

/**
 * 事件处理器接口<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-17 10:00:39
 */
public interface ChatEventHandler<T extends ChatEvent> {


    void processEvent(T event);

    String getEvent();
}
