package com.soflyit.chattask.lib.netty.common;

import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import io.netty.util.AttributeKey;

/**
 * 频道属性名常量
 */
public interface ChannelAttrKeyConstant {

    AttributeKey<WebsocketUserId> USER_ID_ATTRIBUTE_KEY = AttributeKey.valueOf("userId");

    AttributeKey<String> TOKEN_ATTRIBUTE_KEY = AttributeKey.valueOf("token");


}
