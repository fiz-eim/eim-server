package com.soflyit.chattask.im.channel.domain.vo;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import lombok.Data;

import java.util.List;

@Data
public class DirectChannelVo extends ChannelVo {
    private List<ChannelMember> memberList;

}
