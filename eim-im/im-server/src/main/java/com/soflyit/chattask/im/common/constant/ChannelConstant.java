package com.soflyit.chattask.im.common.constant;

/**
 * 频道常量类<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-08 17:14
 */
public interface ChannelConstant {



    Integer CHANNEL_TYPE_DIRECT = 1;


    Integer CHANNEL_TYPE_DEPART = 2;


    Integer CHANNEL_TYPE_PROJECT = 3;


    Integer CHANNEL_TYPE_TASK = 4;


    Integer CHANNEL_TYPE_DISCUSSION = 5;


    Integer CHANNEL_TYPE_BOT_DIRECT = 6;



    Integer PINNED_FLAG_TRUE = 1;
    Integer PINNED_FLAG_FALSE = 2;


    Integer DND_FLAG_TRUE = 1;
    Integer DND_FLAG_FALSE = 2;


    Short COLLAPSE_FLAG_TRUE = 1;
    Short COLLAPSE_FLAG_FALSE = 2;


    Integer MEMBER_ROLE_OWNER = 1;

    Integer MEMBER_ROLE_MANAGER = 2;

    Integer MEMBER_ROLE_MEMBER = 3;


    String NOTICE_DEFAULT = "default";
    String NOTICE_EMAIL_TRUE = "true";
    String NOTICE_EMAIL_FALSE = "false";

    String NOTICE_PUSH_ALL = "all";
    String NOTICE_PUSH_MENTION = "mention";
    String NOTICE_PUSH_NONE = "none";

    String NOTICE_MARK_UNREAD_ALL = "all";
    String NOTICE_MARK_UNREAD_MENTION = "mention";


    Short MEMBER_TYPE_USER = 1;
    Short MEMBER_TYPE_BOT = 2;


}
