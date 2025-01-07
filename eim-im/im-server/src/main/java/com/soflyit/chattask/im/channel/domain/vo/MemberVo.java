package com.soflyit.chattask.im.channel.domain.vo;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import lombok.Data;

/**
 * 成员信息<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-08 17:42
 */
@Data
public class MemberVo extends ChannelMember {


    private MemberNotifyProp notifyPropData;


    private String avatar;

    private MemberExtData memberExtData;


    private Boolean banFlag;

}
