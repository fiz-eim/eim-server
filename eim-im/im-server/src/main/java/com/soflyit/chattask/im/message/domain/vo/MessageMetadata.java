package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.chattask.im.message.domain.entity.LinkMeta;
import com.soflyit.chattask.im.message.domain.entity.MsgAck;
import com.soflyit.chattask.im.message.domain.entity.MsgReaction;
import com.soflyit.chattask.im.message.domain.entity.MsgUnread;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 消息元数据 <br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-21 17:26
 */
@Data
public class MessageMetadata {



    private MessageVo replyMsgData;


    private List<MessageVo> msgForwardData;


    private List<MsgReaction> reactions;


    private List<TextTagData.TextTag> textTags;


    private List<Long> availableReplyUsers;


    private SystemMessageData systemData;


    private String contentType = "text";


    private MetaCardInfo card;


    private MetaCount count;


    private MetaStatusFlag status;


    private List<MsgUnread> unreadUserList;


    private List<MsgAck> unAckUserList;

    private List<Long> mentionUsers;

    private Long replyMsgId;
    private Long pinnedUserId;
    private String pinnedUser;

    private List<EmbedData> embeds;

    private List<EmojiData> emojis;

    private Map<String, ImageData> images;

    private List<LinkMeta> links;
}
