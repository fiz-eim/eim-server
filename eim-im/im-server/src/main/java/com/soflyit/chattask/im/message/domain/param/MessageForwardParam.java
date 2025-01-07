package com.soflyit.chattask.im.message.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 消息转发参数<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-01 17:39
 */
@ApiModel
@Data
public class MessageForwardParam {

    @ApiModelProperty("被转发消息Idl列表")
    private List<Long> forwardMsgIds;

    @ApiModelProperty(value = "转发类型", dataType = "Integer", allowableValues = "1, 2", example = "1")
    private Short forwardType;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "频道信息")
    private List<ForwardChannelInfo> channelIds;

    private Boolean replyFlag;

}
