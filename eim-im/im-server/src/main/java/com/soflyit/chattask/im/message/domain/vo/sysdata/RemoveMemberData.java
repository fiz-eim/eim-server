package com.soflyit.chattask.im.message.domain.vo.sysdata;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.message.domain.vo.SystemMessageData;
import lombok.Data;

import java.util.List;

import static com.soflyit.chattask.im.common.constant.MessageConstant.SYSTEM_MESSAGE_TYPE_REMOVE_MEMBER;

/**
 * 移除频道成员数据 <br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-27 14:55
 */
@Data
public class RemoveMemberData<T> extends SystemMessageData {

    public RemoveMemberData() {
        super.setMessageType(SYSTEM_MESSAGE_TYPE_REMOVE_MEMBER);
    }

    private Long userId;

    private String realName;

    private List<ChannelMember> memberList;

    private T extData;
}
