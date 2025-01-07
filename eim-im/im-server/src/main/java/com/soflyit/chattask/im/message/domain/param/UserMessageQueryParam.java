package com.soflyit.chattask.im.message.domain.param;

import com.soflyit.chattask.im.message.domain.entity.Message;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户未读消息查询 <br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-22 14:48
 */
@ApiModel
@Data
public class UserMessageQueryParam extends Message {

    private Long channelId;

    @ApiModelProperty("已读消息数量")
    private Integer limitBefore;

    @ApiModelProperty("未读消息数量")
    private Integer limitAfter;

    @ApiModelProperty("是否返回回复消息")
    private Boolean skipFetchThreads;

    @ApiModelProperty("是否折叠消息")
    private Boolean collapsedThreads;

    @ApiModelProperty("是否返回用户信息")
    private Boolean collapsedThreadsExtended;

}
