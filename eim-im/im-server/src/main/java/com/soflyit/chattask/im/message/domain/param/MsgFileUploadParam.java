package com.soflyit.chattask.im.message.domain.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 消息文件预处理信息 <br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-21 16:43
 */
@Data
@ApiModel("消息文件预处理信息")
public class MsgFileUploadParam {


    private String name;


    private String uuid;


    private Long size;

}
