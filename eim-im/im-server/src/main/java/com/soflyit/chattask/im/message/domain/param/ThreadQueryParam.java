package com.soflyit.chattask.im.message.domain.param;

import com.soflyit.chattask.im.message.domain.entity.Message;
import lombok.Data;

import java.util.Date;

/**
 * 回复消息（消息串）查询参数<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-01 14:21
 */
@Data
public class ThreadQueryParam extends Message {


    private Date fromCreateTime;

    private Long fromMessageId;


    private Boolean skipFetchThreads;


    private Boolean collapsedThreads;


    private Boolean collapsedThreadsExtended;


    private String direction;


}
