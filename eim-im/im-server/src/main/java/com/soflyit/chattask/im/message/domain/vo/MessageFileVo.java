package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import lombok.Data;

/**
 * 消息文件<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-23 11:01
 */
@Data
public class MessageFileVo extends MsgFile {


    private Message message;

}
