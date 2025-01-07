package com.soflyit.chattask.lib.netty.action.domain;

import com.alibaba.fastjson.JSONObject;
import com.soflyit.chattask.lib.netty.action.ChatActionResponse;

import static com.soflyit.chattask.lib.netty.common.ChatActionName.READ_MESSAGE;

/**
 * 心跳响应<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-28 15:28
 */
public class ReadMessageActionResponse extends ChatActionResponse<JSONObject> {

    public ReadMessageActionResponse() {
        super();
        super.setClientAction(READ_MESSAGE);
    }
}
