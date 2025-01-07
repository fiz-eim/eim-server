package com.soflyit.chattask.im.channel.domain.param;

import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 频道查询参数
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Data
@ApiModel
public class ChannelQueryParam extends ChatChannel {


    private Boolean includeDeleted;


    private Boolean includeDirect;

}
