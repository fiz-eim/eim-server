package com.soflyit.chattask.im.message.domain.vo;

import lombok.Data;

/**
 * 元数据数量信息<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-10-14 08:44
 */
@Data
public class MetaCount {

    private Integer replyCount;

    private Integer unreadCount;

    private Integer unAckCount;

}
