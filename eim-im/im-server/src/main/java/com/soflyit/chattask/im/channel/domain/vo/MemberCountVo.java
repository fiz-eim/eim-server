package com.soflyit.chattask.im.channel.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 成员数量对象<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-08 17:19
 */
@ApiModel("频道成员数量")
@Data
public class MemberCountVo {


    @ApiModelProperty("频道Id")
    private Long id;


    @ApiModelProperty("成员数量")
    private Integer memberCount;
}
