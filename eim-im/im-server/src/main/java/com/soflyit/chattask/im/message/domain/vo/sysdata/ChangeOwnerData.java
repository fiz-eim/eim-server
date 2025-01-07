package com.soflyit.chattask.im.message.domain.vo.sysdata;

import com.soflyit.chattask.im.message.domain.vo.SystemMessageData;
import lombok.Data;

import static com.soflyit.chattask.im.common.constant.MessageConstant.SYSTEM_MESSAGE_TYPE_CHANGE_OWNER;

/**
 * 群主变更消息数据<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-27 15:33
 */
@Data
public class ChangeOwnerData extends SystemMessageData {

    public ChangeOwnerData() {
        super.setMessageType(SYSTEM_MESSAGE_TYPE_CHANGE_OWNER);
    }

    private Long oldOwnerId;

    private String oldOwnerUser;

    private Long newOwnerId;

    private String newOwnerUser;
}
