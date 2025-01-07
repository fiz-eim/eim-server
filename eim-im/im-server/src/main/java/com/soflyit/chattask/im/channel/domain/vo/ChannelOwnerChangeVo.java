package com.soflyit.chattask.im.channel.domain.vo;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChannelOwnerChangeVo implements Serializable {

    private ChannelMember owner;

    private ChannelMember member;

}
