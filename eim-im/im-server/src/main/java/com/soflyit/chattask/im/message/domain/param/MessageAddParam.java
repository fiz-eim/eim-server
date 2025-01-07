package com.soflyit.chattask.im.message.domain.param;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.vo.MessageProp;
import com.soflyit.chattask.im.message.domain.vo.TaskDataVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 消息添加参数<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-22 18:22
 */
@Data
@ApiModel
public class MessageAddParam extends Message {

    @ApiModelProperty("文件Id列表")
    private List<Long> fileIds;

    @ApiModelProperty("待上传文件")
    private List<MsgFileUploadParam> clientFiles;

    @ApiModelProperty("@用户列表")
    private List<Long> mentionUserList;

    @ApiModelProperty("是否@所有人")
    private Boolean mentionAll;

    @ApiModelProperty("@机器人列表")
    private List<Long> mentionBotList;

    private Long replyMsgId;

    private MessageProp props;


    private List<TaskDataVo> taskList;


}
