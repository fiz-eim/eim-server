package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.message.domain.entity.MsgFavorite;
import lombok.Data;

@Data
public class MsgFavoriteVo extends MsgFavorite {


    private ChatChannel channel;

    private MessageVo message;

}
