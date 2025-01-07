package com.soflyit.chattask.im.channel.domain.param;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import lombok.Data;

import java.util.List;

/**
 * 删除频道成员<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-09 09:54
 */
@Data
public class MemberRemoveParam {

    private Long channelId;


    private List<ChannelMember> memberList;

}
