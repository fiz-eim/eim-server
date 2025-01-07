package com.soflyit.chattask.im.message.domain.vo.sysdata;

import com.soflyit.chattask.im.message.domain.vo.SystemMessageData;
import lombok.Data;

import static com.soflyit.chattask.im.common.constant.MessageConstant.SYSTEM_MESSAGE_TYPE_LEAVE_CHANNEL;

/**
 * 成员离开频道数据<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-27 15:25
 */
@Data
public class LeaveMemberData<T> extends SystemMessageData {

    public LeaveMemberData() {
        super.setMessageType(SYSTEM_MESSAGE_TYPE_LEAVE_CHANNEL);
    }

    private Long userId;

    private String realName;

    private Short joinType;

    private T extData;
}
