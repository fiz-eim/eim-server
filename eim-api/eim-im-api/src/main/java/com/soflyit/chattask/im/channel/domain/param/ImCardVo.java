package com.soflyit.chattask.im.channel.domain.param;

import com.soflyit.chattask.im.channel.domain.entity.ImCard;
import lombok.Data;

@Data
public class ImCardVo extends ImCard {

    private CardData data;

}
