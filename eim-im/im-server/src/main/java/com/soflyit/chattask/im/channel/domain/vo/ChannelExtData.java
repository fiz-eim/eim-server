package com.soflyit.chattask.im.channel.domain.vo;

import lombok.Data;

@Data
public class ChannelExtData {

    private Boolean ownerManageFlag;


    private Boolean shortcutBarFlag = Boolean.TRUE;


    private Boolean ownerTopMsgFlag;



    private Boolean ownerMentionAllFlag;


    private Boolean memberReviewFlag;


    private BanConfig banConfig;


    private Boolean memberAppFlag = Boolean.TRUE;



    private Long userId;


}
