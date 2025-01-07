package com.soflyit.chattask.im.message.domain.vo.sysdata;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.message.domain.vo.SystemMessageData;
import lombok.Data;

import java.util.List;

import static com.soflyit.chattask.im.common.constant.MessageConstant.SYSTEM_MESSAGE_TYPE_ADD_MEMBER;

/**
 * 添加成员<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-27 13:48
 */
@Data
public class AddMemberData<T> extends SystemMessageData {

    public AddMemberData() {
        super.setMessageType(SYSTEM_MESSAGE_TYPE_ADD_MEMBER);
    }

    private Long userId;

    private String realName;

    private List<ChannelMember> memberList;

    private Short joinType;

    private T extData;

}
