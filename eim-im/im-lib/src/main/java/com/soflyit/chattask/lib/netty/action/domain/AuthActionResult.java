package com.soflyit.chattask.lib.netty.action.domain;

import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import lombok.Data;

/**
 * 认证结果数据<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-24 14:23
 */
@Data
public class AuthActionResult {

    private String token;

    private WebsocketUserId websocketUserId;
}
