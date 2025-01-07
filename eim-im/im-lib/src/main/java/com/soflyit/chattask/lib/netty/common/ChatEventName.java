package com.soflyit.chattask.lib.netty.common;

/**
 * 事件名称常量
 */
public interface ChatEventName {


    String HELLO = "hello";


    String AUTH_REQ = "auth_req";


    String MESSAGE_POSTED = "message_posted";


    String MESSAGE_READ = "message_read";


    String MESSAGE_ACK = "message_ack";


    String MESSAGE_DELETED = "message_deleted";

    String MESSAGE_EDIT = "message_edit";

    String MESSAGE_REACTION = "message_reaction";
    String MESSAGE_TOP = "message_top";
    String MESSAGE_CANCEL_TOP = "message_cancel_top";

    String MESSAGE_TAG = "message_tag";
    String MESSAGE_TEXT_TAG = "message_text_tag";
    String MESSAGE_UNMARK_TAG = "message_cancel_tag";


    String TOPIC_CREATE = "topic_create";

    String TOPIC_CONFIG = "topic_config";


    String MESSAGE_PINNED = "message_pinned";


    String MESSAGE_UNPINNED = "message_unpinned";


    String CHANNEL_CREATE_EVENT = "channel_created";

    String CHANNEL_UPDATE_EVENT = "channel_update";


    String CHANNEL_DELETE_EVENT = "channel_delete";

    String CHANNEL_DISPLAY_NAME_EVENT = "channel_display_name";


    String CHANNEL_RESTORE_EVENT = "channel_restore";

    String CHANNEL_MEMBER_DELETE_EVENT = "member_delete";

    String CHANNEL_MEMBER_LEAVE_EVENT = "member_leave";

    String CHANNEL_MEMBER_UPDATE_NOTIFY_EVENT = "member_update_notify";

    String CHANNEL_MEMBER_CHANGE_ROLE_EVENT = "member_change_role";

    String CHANNEL_MEMBER_ADD_EVENT = "member_add";
    String CHANNEL_MEMBER_DELETE_MSG_EVENT = "member_del_msg";


    String CHANNEL_OWNER_CHANGE_EVENT = "channel_owner_change";


    String MESSAGE_FILE_DELETE = "message_file_delete";


    String MESSAGE_FILE_UPLOADED = "message_file_uploaded";
}
