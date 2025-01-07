package com.soflyit.chattask.im.channel.domain.vo;

import lombok.Data;

/**
 * 用户频道通知配置<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-08 17:43
 */
@Data
public class MemberNotifyProp {

    private String email;

    private String push;

    private String desktop;

    private String markUnread;

}
