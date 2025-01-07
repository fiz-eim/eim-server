package com.soflyit.chattask.im.message.domain.param;

import com.soflyit.chattask.im.message.domain.entity.Message;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 消息查询参数<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-21 16:16
 */
@Data
public class MessageQueryParam extends Message {

    private Date since;

    private Date endTime;

    private Long msgIdBefore;

    private Long msgIdAfter;

    private Long msgIdAround;

    private Boolean includeDeleted;

    private Boolean fetchBefore;

    private Boolean queryReplyMessage;

    private List<Long> channelIds;

    private List<Long> exceptMsgIds;

    private Boolean excludeNextMsg;


}
