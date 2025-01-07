package com.soflyit.chattask.im.message.domain.param;

import com.soflyit.chattask.im.message.domain.vo.SystemMessageData;
import lombok.Data;

/**
 * 系统消息 <br>
 * 详细说明
 *
 * @param <DATA> 数据类型
 * @author Toney
 * @date 2023-11-29 10:07
 */
@Data
public class SystemMessage<DATA extends SystemMessageData> {

    private Long userId;


    private Long channelId;

    private DATA data;


    private Long messageId;


    private Long originalId;
}
