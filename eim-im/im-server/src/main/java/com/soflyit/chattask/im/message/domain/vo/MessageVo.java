package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import com.soflyit.chattask.im.message.domain.param.MsgFileUploadParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 消息返回数据 <br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-21 17:23
 */
@Data
@ApiModel
public class MessageVo extends Message {

    @ApiModelProperty("消息属性")
    private MessageProp props;

    @ApiModelProperty("元数据")
    private MessageMetadata metaData;

    @ApiModelProperty("消息文件列表")
    private List<MsgFile> files;

    @ApiModelProperty("待上传文件")
    private List<MsgFileUploadParam> clientFiles;

}
