package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.chattask.im.message.domain.entity.MsgReaction;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MessageReactionEventData implements Serializable {

    private Long channelId;

    private Long msgId;

    private List<MsgReaction> reactions;
}
