package com.soflyit.chattask.im.message.domain.vo;

import lombok.Data;

/**
 * 元数据标志状态<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-10-14 08:45
 */
@Data
public class MetaStatusFlag {


    private Boolean readFlag = Boolean.FALSE;


    private Boolean mentionMe = Boolean.FALSE;


    private Boolean replyFlag = Boolean.FALSE;


    private Boolean forwardFlag = Boolean.FALSE;


    private Boolean tagFlag = Boolean.FALSE;


    private Boolean pinnedFlag = Boolean.FALSE;


    private Boolean editFlag = Boolean.FALSE;

    private Boolean urgentFlag = Boolean.FALSE;


}
