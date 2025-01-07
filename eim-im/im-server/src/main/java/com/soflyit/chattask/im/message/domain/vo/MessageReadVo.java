package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 消息读取信息<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-06 09:50
 */
@Data
public class MessageReadVo extends Message {

    private Long readBy;

    @ApiModelProperty("元数据")
    private MessageMetadata metaData;

    @ApiModelProperty("消息文件列表")
    private List<MsgFile> files;

}
