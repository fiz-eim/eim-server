package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.chattask.im.message.domain.entity.MsgTop;
import lombok.Data;

@Data
public class MsgTopVo extends MsgTop {

    private MessageVo messageData;

}
