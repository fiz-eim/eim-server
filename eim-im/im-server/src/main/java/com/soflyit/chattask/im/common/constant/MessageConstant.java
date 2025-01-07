package com.soflyit.chattask.im.common.constant;

/**
 * 消息常量<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-21 17:52
 */
public interface MessageConstant {


    Short ACK_REQUIRED_TRUE = 1;


    Short ACK_REQUIRED_FALSE = 2;


    Short MESSAGE_TYPE_USER = 1;

    Short MESSAGE_TYPE_SYSTEM = 2;


    Short MESSAGE_TYPE_BOT = 3;


    Short MESSAGE_TYPE_APP = 4;


    String THREAD_QUERY_DIRECTION_UP = "up";
    String THREAD_QUERY_DIRECTION_DOWN = "down";



    Short FORWARD_TYPE_SINGLE = 1;


    Short FORWARD_TYPE_MERGE = 2;


    Integer FORWARD_CHANNEL_ID_TYPE_CHANNEL = 1;
    Integer FORWARD_CHANNEL_ID_TYPE_USER = 2;


    Short DELETE_FLAG_TRUE = 1;
    Short DELETE_FLAG_FALSE = 2;


    String ACK_TYPE_ACK = "ack";
    String ACK_TYPE_CANCEL = "cancel";

    String SYSTEM_MESSAGE_TYPE_ADD_MEMBER = "add_member";
    String SYSTEM_MESSAGE_TYPE_REMOVE_MEMBER = "remove_member";
    String SYSTEM_MESSAGE_TYPE_LEAVE_CHANNEL = "leave_channel";
    String SYSTEM_MESSAGE_TYPE_RECALL_MESSAGE = "recall_message";

    String SYSTEM_MESSAGE_TYPE_CHANGE_OWNER = "change_owner";
    String SYSTEM_MESSAGE_TYPE_CHANNEL_ADD_MANAGER = "channel_manager_add";
    String SYSTEM_MESSAGE_TYPE_CHANNEL_DELETE_MANAGER = "channel_manager_delete";


}
