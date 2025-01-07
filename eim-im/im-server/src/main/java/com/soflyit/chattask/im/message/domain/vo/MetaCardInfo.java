package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.chattask.im.channel.domain.param.CardData;
import lombok.Data;

import java.util.Map;

/**
 * 元数据卡片信息<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-10-14 08:43
 */
@Data
public class MetaCardInfo {


    private CardData cardData;


    private Map<String, Object> cardInfo;


}
