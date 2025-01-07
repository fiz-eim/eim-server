package com.soflyit.common.core.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author soflyit
 * @Description:
 * @date 2022/11/17 18:53
 */
@ApiModel
@Data
public class BaseVO implements Serializable {

    private static final long serialVersionUID = 2310290218336138956L;


    @ApiModelProperty("创建者")
    private String createBy;


    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    @ApiModelProperty("更新者")
    private String updateBy;


    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    @ApiModelProperty("备注")
    private String remark;

}
