package com.soflyit.chattask.im.common.util;

import com.soflyit.chattask.im.message.domain.vo.MessageMetadata;
import com.soflyit.chattask.im.message.domain.vo.MetaCardInfo;
import com.soflyit.chattask.im.message.domain.vo.MetaCount;
import com.soflyit.chattask.im.message.domain.vo.MetaStatusFlag;

public class MessageMetaUtil {

    public static MetaStatusFlag getStatusFlag(MessageMetadata metadata) {
        MetaStatusFlag metaStatusFlag = metadata.getStatus();
        if (metaStatusFlag == null) {
            metaStatusFlag = new MetaStatusFlag();
            metadata.setStatus(metaStatusFlag);
        }
        return metaStatusFlag;
    }

    public static MetaCount getCount(MessageMetadata metadata) {
        MetaCount count = metadata.getCount();
        if (count == null) {
            count = new MetaCount();
            metadata.setCount(count);
        }
        return count;
    }

    public static MetaCardInfo getMetaCard(MessageMetadata metadata) {
        MetaCardInfo metaCard = metadata.getCard();
        if (metaCard == null) {
            metaCard = new MetaCardInfo();
            metadata.setCard(metaCard);
        }
        return metaCard;
    }
}
