package com.soflyit.chattask.im.message.domain.vo;

import lombok.Data;

/**
 * 成员加入频道消息<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-29 14:06
 */
@Data
public class JoinChannelMessageData extends SystemMessageData {


    private Long userId;

    private String userRealName;


    private Long addedUserId;


    private String addedUserName;


    private Integer joinType = 1;

}
