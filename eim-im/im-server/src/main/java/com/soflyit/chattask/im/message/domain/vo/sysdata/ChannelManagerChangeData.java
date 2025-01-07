package com.soflyit.chattask.im.message.domain.vo.sysdata;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.message.domain.vo.SystemMessageData;
import lombok.Data;

import java.util.List;

@Data
public class ChannelManagerChangeData extends SystemMessageData {

    private List<ChannelMember> managers;

    private ChannelMember owner;


}
