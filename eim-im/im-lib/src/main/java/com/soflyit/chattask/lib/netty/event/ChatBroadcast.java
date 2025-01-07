package com.soflyit.chattask.lib.netty.event;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 消息广播属性
 *
 * @param <UID> 用户Id类型
 * @param <C>   频道Id类型
 * @param <CID> 客户端Id类型
 */
@Data
public class ChatBroadcast<UID extends Serializable, C extends Serializable, CID extends Serializable> implements Serializable {

    private UID userId;

    private Map<UID, Boolean> omitUsers;

    private List<UID> userIds;

    private C channelId;

    private CID clientId;

    private CID omitClientId;


    private Boolean containsSanitizedData;


    private Boolean containsSensitiveData;


}
