package com.soflyit.chattask.im.message.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 话题消息扩展配置
 */
@Data
public class MsgReplyExt implements Serializable {

    private List<Long> availableUsers;

}
