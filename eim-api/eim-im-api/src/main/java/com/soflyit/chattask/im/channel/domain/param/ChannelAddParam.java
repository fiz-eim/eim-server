package com.soflyit.chattask.im.channel.domain.param;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.system.api.model.LoginUser;
import lombok.Data;

import java.util.List;

/**
 * 频道添加请求参数<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-16 16:33
 */
@Data
public class ChannelAddParam extends ChatChannel {


    private List<Long> channelMembers;


    private List<ChannelMember> members;


    private Long businessId;

    private Integer businessType;


    private Long dxFolderId;


    private String businessData;


    private LoginUser loginUser;


}
