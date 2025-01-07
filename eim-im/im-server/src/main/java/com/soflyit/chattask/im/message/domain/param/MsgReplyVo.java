package com.soflyit.chattask.im.message.domain.param;

import com.soflyit.chattask.im.message.domain.entity.MsgReply;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import lombok.Data;

/**
 * 主题查询参数
 */
@Data
public class MsgReplyVo extends MsgReply {


    private Integer mentionCount;


    private Integer unreadCount;


    private String content;

    private MessageVo message;


}
