package com.soflyit.chattask.im.message.domain.vo.sysdata;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.vo.SystemMessageData;
import lombok.Data;

import static com.soflyit.chattask.im.common.constant.MessageConstant.SYSTEM_MESSAGE_TYPE_RECALL_MESSAGE;

/**
 * 删除消息<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-27 11:48
 */
@Data
public class RecallMessageData extends SystemMessageData {

    public RecallMessageData() {
        super.setMessageType(SYSTEM_MESSAGE_TYPE_RECALL_MESSAGE);
    }

    private String deleteByUser;

    private Long deleteBy;

    private Message deletedMessage;

}
