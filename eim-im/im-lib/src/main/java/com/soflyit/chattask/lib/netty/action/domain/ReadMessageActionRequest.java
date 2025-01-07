package com.soflyit.chattask.lib.netty.action.domain;

import com.alibaba.fastjson.JSONObject;
import com.soflyit.chattask.lib.netty.action.ChatActionRequest;
import com.soflyit.chattask.lib.netty.common.ChatActionName;

/**
 * 消息<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-05 13:49
 */
public class ReadMessageActionRequest extends ChatActionRequest<JSONObject, Long> {

    @Override
    public String getActionName() {
        return ChatActionName.READ_MESSAGE;
    }
}
