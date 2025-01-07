package com.soflyit.chattask.im.channel.domain.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 用户频道查询参数
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Data
@ApiModel
public class UserChannelQueryParam extends ChannelQueryParam {

    private List<Long> channelIds;

}
