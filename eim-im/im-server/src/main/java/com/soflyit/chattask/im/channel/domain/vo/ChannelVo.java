package com.soflyit.chattask.im.channel.domain.vo;

import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.param.ImCardVo;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.vo.MsgTopVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 频道响应<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-28 10:07
 */
@Data
@ApiModel
public class ChannelVo extends ChatChannel {


    @ApiModelProperty("最新消息")
    private Message lastMessage;

    @ApiModelProperty("第一条消息")
    private Message firstMessage;


    private String directChannelName;
    private String directChannelIcon;

    private Integer pinnedFlag;

    private Integer dndFlag;

    private Short collapse;


    private List<MsgTopVo> msgTops;


    private List<ImCardVo> topCards;


    private String displayName;


    private ChannelExtData ext;

}
