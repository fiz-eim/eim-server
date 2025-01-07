package com.soflyit.chattask.im.message.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提及查询参数<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-29 14:45
 */
@Data
@ApiModel("提及消息查询参数")
public class MessageMentionQueryParam {

    @ApiModelProperty(value = "频道Id", required = true)
    private Long channelId;

    @ApiModelProperty(value = "是否包含已读消息", required = true)
    private Boolean containReadMessage;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

}
