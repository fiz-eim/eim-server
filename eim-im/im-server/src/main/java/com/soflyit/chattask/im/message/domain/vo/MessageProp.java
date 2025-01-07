package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.system.api.domain.SysUser;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 消息属性数据<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-21 17:24
 */
@Data
public class MessageProp<T extends SystemMessageData> {

    private Boolean fromBot;


    private String userName;


    private Map<String, Object> messageParams;


    private String contentType;


    private SysUser addedUser;


    private SysUser deletedUser;


    private Boolean impersonate;


    private SysUser impersonateUser;


    private Long botId;


    private T systemData;


    private Boolean forwardFlag;

    private Boolean replyFlag;

    private Long replyMsgId;

    private Boolean topicFlag;

    private Boolean tagFlag;


    private List<Long> forwardMsgIds;


    private Boolean pinnedFlag;
    private Long pinnedUserId;
    private String pinnedUser;


    private List<Long> metionBotList;

}
