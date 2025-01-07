package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.chattask.im.channel.domain.param.CardPrivateData;
import lombok.Data;

import java.util.Map;

@Data
public class MessageExtDataVo {

    private Short cardType;

    private Long cardTemplateId;

    private Map<String, Object> data;

    private CardPrivateData privateData;

}
