package com.soflyit.system.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soflyit.common.core.web.domain.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @author soflyit
 * @Description: 用户信息简单实体类
 * @date 2022/11/7 14:00
 */
@ApiModel
@Data
public class SysUserSimpleVO extends BaseVO {
    private static final long serialVersionUID = 2005714939245812588L;


    private Long userId;


    private Long deptId;


    private String userName;


    private String nickName;


    private String email;


    private String phonenumber;


    private String sex;


    private String avatar;


    private String status;


    private String delFlag;


    private String loginIp;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;


    private Long ext1;

}
